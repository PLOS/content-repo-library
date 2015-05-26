package org.plos.crepo.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoCollectionList;
import org.plos.crepo.model.RepoCollectionMetadata;
import org.plos.crepo.model.RepoMetadata;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.model.RepoObjectMetadata;
import org.plos.crepo.model.RepoVersion;
import org.plos.crepo.model.RepoVersionNumber;
import org.plos.crepo.model.RepoVersionTag;
import org.plos.crepo.model.Status;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * An implementation of the client-side service interface with a local, in-memory implementation behind it. Intended for
 * unit tests. The service implementation is rudimentary, is not performant, and does not guarantee a perfect simulation
 * of an actual Content Repo server in edge cases or error conditions.
 * <p>
 * Not thread-safe.
 */
public class InMemoryContentRepoService implements ContentRepoService {

  /**
   * Thrown when the service would have sent a 400-series response due to attempting an illegal or impossible action.
   * This class does not guarantee that it will simulate every real-life validation failure.
   */
  public static class InMemoryContentRepoServiceException extends RuntimeException {
    private InMemoryContentRepoServiceException() {
    }
  }

  /**
   * The hash function that the fake service uses to produce content checksums.
   */
  private static final HashFunction CONTENT_HASH_FUNCTION = Hashing.sha1();

  /**
   * Behaves the same as ImmutableMap.Builder, except that it ignores null values.
   */
  private static class NullSafeMapBuilder<K, V> {
    private final ImmutableMap.Builder<K, V> delegate = ImmutableMap.builder();

    public ImmutableMap<K, V> build() {
      return delegate.build();
    }

    public NullSafeMapBuilder<K, V> put(K key, V value) {
      Preconditions.checkNotNull(key);
      if (value != null) {
        delegate.put(key, value);
      }
      return this;
    }
  }

  /**
   * In-memory representation of a bucket.
   */
  private static class FakeBucket {
    private final ListMultimap<String, FakeCollection> collections = LinkedListMultimap.create();
    private final ListMultimap<String, FakeObject> objects = LinkedListMultimap.create();
  }

  /**
   * In-memory representation of a persisted object or collection.
   *
   * @param <M> the metadata type for output representing the entity
   */
  private abstract class FakeEntity<M extends RepoMetadata> {
    protected final RepoVersion version;
    protected final int number;
    protected final String tag;

    protected String userMetadata;
    protected Status status;
    protected final Timestamp creationDate;
    protected Timestamp timestamp;

    private FakeEntity(String key, int number, String tag) {
      this.version = RepoVersion.create(key, uuidGenerator.generate());
      this.number = number;
      this.tag = tag;
      this.status = Status.USED;
      this.creationDate = new Timestamp(new Date().getTime());
      this.timestamp = creationDate;
    }

    protected NullSafeMapBuilder<String, Object> buildMetadata() {
      return new NullSafeMapBuilder<String, Object>()
          .put("key", version.getKey())
          .put("uuid", version.getUuid().toString())
          .put("versionNumber", number)
          .put("tag", tag)
          .put("userMetadata", userMetadata)
          .put("status", status.toString())
          .put("creationDate", creationDate == null ? null : creationDate.toString())
          .put("timestamp", timestamp == null ? null : timestamp.toString());
    }

    public abstract M getMetadata();
  }

  private class FakeObject extends FakeEntity<RepoObjectMetadata> {
    private final byte[] content;
    private final HashCode contentHash;
    private String downloadName;
    private String contentType;

    private FakeObject(String key, int number, String tag, byte[] content) {
      super(key, number, tag);
      this.content = content; // must be created internally
      this.contentHash = CONTENT_HASH_FUNCTION.hashBytes(this.content);
    }

    private InputStream open() {
      return new ByteArrayInputStream(content);
    }

    @Override
    public RepoObjectMetadata getMetadata() {
      return new RepoObjectMetadata(buildMetadata()
          .put("checksum", contentHash.toString())
          .put("size", content.length)
          .put("downloadName", downloadName)
          .put("contentType", contentType)
          .build());
    }
  }

  private class FakeCollection extends FakeEntity<RepoCollectionList> {
    private final Collection<RepoVersion> objectIds = new LinkedHashSet<>();

    private FakeCollection(String key, int number, String tag) {
      super(key, number, tag);
    }

    @Override
    public RepoCollectionList getMetadata() {
      List<Map<String, Object>> rawObjectMetadata = new ArrayList<>(objectIds.size());
      for (RepoVersion objectId : objectIds) {
        FakeObject fakeObject = getFrom(defaultBucket.objects, objectId);
        rawObjectMetadata.add(fakeObject.getMetadata().getMapView());
      }
      return new RepoCollectionList(buildMetadata()
          .put("objects", rawObjectMetadata)
          .build());
    }
  }


  private static interface UuidGenerator {
    public abstract UUID generate();
  }

  /**
   * Makes test-friendly UUIDs that must <em>not</em> be used for real purposes.
   * <p>
   * The first eight digits of each generated value increment from {@code 00000001}. This is a convenience for human
   * inspection during debugging. Tests should not rely on these numbers for validation (they are unstable if the system
   * under test does things in a different order).
   * <p>
   * The remaining digits are produced by {@link java.util.Random}, which avoids the overhead of {@link
   * java.security.SecureRandom} but does not adequately prevent collisions. (Of course, tampering with the first eight
   * digits also increases the risk of collisions, so if this is a concern something is wrong.)
   */
  private static class FakeUuidGenerator implements UuidGenerator {
    private final Random random = new Random(); // insecure
    private int counter = 0;

    @Override
    public UUID generate() {
      // Leave the highest 4 bytes blank to hold the counter. Randomize the next 4 bytes.
      long high = ((1L << 32) - 1) & (long) random.nextInt();

      high |= (long) (++counter) << 32; // Fill in the counter and increment
      high = high & ~(0xfL << 12) | (0x4L << 12); // Set the version number

      long low = random.nextLong(); // Randomize the lowest 8 bytes
      low = low & ~(0xcL << 60) | (0x8L << 60); // Set 2 bits to signify the variant

      return new UUID(high, low);
    }
  }

  /**
   * This can replace the FakeUuidGenerator, in case this in-memory implementation is ever needed outside of a testing
   * context for some reason.
   */
  private static final UuidGenerator REAL_UUID_GENERATOR = new UuidGenerator() {
    @Override
    public UUID generate() {
      return UUID.randomUUID(); // uses SecureRandom
    }
  };


  private final String defaultBucketKey;
  private final UuidGenerator uuidGenerator = new FakeUuidGenerator();
  private final Map<String, FakeBucket> buckets = new HashMap<>();
  private FakeBucket defaultBucket;

  public InMemoryContentRepoService(String defaultBucketKey) {
    Preconditions.checkArgument(!defaultBucketKey.isEmpty());
    this.defaultBucketKey = defaultBucketKey;
    buckets.put(defaultBucketKey, (defaultBucket = new FakeBucket()));
  }

  public void clear() {
    buckets.clear();
    buckets.put(defaultBucketKey, (defaultBucket = new FakeBucket()));
  }


  @Override
  public boolean hasXReproxy() {
    return false;
  }

  @Override
  public Map<String, Object> getRepoConfig() {
    return ImmutableMap.<String, Object>builder()
        .put("version", getClass().toString())
        .put("hasXReproxy", hasXReproxy())
        .build();
  }

  @Override
  public Map<String, Object> getRepoStatus() {
    return ImmutableMap.<String, Object>builder()
        .put("bucketCount", buckets.size())
        .build();
  }

  @Override
  public List<Map<String, Object>> getBuckets() {
    Set<String> bucketKeys = buckets.keySet();
    List<Map<String, Object>> result = new ArrayList<>(bucketKeys.size());
    for (String bucketKey : bucketKeys) {
      result.add(getBucket(bucketKey));
    }
    return result;
  }

  @Override
  public Map<String, Object> getBucket(String key) {
    FakeBucket bucket = buckets.get(key);
    if (bucket == null) {
      throw new InMemoryContentRepoServiceException();
    }
    return ImmutableMap.<String, Object>builder()
        .put("bucketName", key)
        .put("totalObjects", bucket.objects.size())
        .build();
  }

  @Override
  public Map<String, Object> createBucket(String key) {
    FakeBucket fakeBucket = new FakeBucket();
    buckets.put(key, fakeBucket);
    return getBucket(key);
  }

  private static <T extends FakeEntity> T getLatest(ListMultimap<String, ? extends T> multimap, String key) {
    List<? extends T> entities = multimap.get(key);
    for (T entity : Lists.reverse(entities)) {
      if (entity.status == Status.USED) {
        return entity;
      }
    }
    throw new InMemoryContentRepoServiceException();
  }

  private static <T extends FakeEntity> T getFrom(ListMultimap<String, ? extends T> multimap, RepoVersion version) {
    List<? extends T> entities = multimap.get(version.getKey());
    for (T entity : entities) {
      if (version.equals(entity.version)) {
        return entity;
      }
    }
    throw new InMemoryContentRepoServiceException();
  }

  private static <T extends FakeEntity> T getFrom(ListMultimap<String, ? extends T> multimap, RepoVersionNumber number) {
    List<? extends T> entities = multimap.get(number.getKey());
    int numberValue = number.getNumber();
    for (T entity : entities) {
      if (entity.number == numberValue) {
        return entity;
      }
    }
    throw new InMemoryContentRepoServiceException();
  }

  private static <T extends FakeEntity> T getFrom(ListMultimap<String, ? extends T> multimap, RepoVersionTag tagObj) {
    List<? extends T> entities = multimap.get(tagObj.getKey());
    String tag = tagObj.getTag();
    for (T entity : entities) {
      if (tag.equals(entity.tag)) {
        return entity;
      }
    }
    throw new InMemoryContentRepoServiceException();
  }

  @Override
  public InputStream getLatestRepoObject(String key) {
    return getLatest(defaultBucket.objects, key).open();
  }

  @Override
  public InputStream getRepoObject(RepoVersion version) {
    return getFrom(defaultBucket.objects, version).open();
  }

  @Override
  public InputStream getRepoObject(RepoVersionNumber number) {
    return getFrom(defaultBucket.objects, number).open();
  }

  @Override
  public RepoObjectMetadata getLatestRepoObjectMetadata(String key) {
    return getLatest(defaultBucket.objects, key).getMetadata();
  }

  @Override
  public RepoObjectMetadata getRepoObjectMetadata(RepoVersion version) {
    return getFrom(defaultBucket.objects, version).getMetadata();
  }

  @Override
  public RepoObjectMetadata getRepoObjectMetadata(RepoVersionNumber number) {
    return getFrom(defaultBucket.objects, number).getMetadata();
  }

  @Override
  public RepoObjectMetadata getRepoObjectMetadata(RepoVersionTag tagObj) {
    return getFrom(defaultBucket.objects, tagObj).getMetadata();
  }

  @Override
  public List<RepoObjectMetadata> getRepoObjectVersions(String key) {
    List<FakeObject> objects = defaultBucket.objects.get(key);
    List<RepoObjectMetadata> metadata = new ArrayList<>(objects.size());
    for (FakeObject object : objects) {
      metadata.add(object.getMetadata());
    }
    return metadata;
  }

  private boolean delete(FakeEntity entity) {
    if (entity.status != Status.USED) return false;
    entity.status = Status.DELETED;
    entity.timestamp = new Timestamp(new Date().getTime());
    return true;
  }

  @Override
  public boolean deleteLatestRepoObject(String key) {
    List<FakeObject> objects = defaultBucket.objects.get(key);
    for (FakeObject object : Lists.reverse(objects)) {
      if (delete(object)) return true;
    }
    return false;
  }

  @Override
  public boolean deleteRepoObject(RepoVersion version) {
    return delete(getFrom(defaultBucket.objects, version));
  }

  @Override
  public boolean deleteRepoObject(RepoVersionNumber number) {
    return delete(getFrom(defaultBucket.objects, number));
  }

  @Override
  public RepoObjectMetadata createRepoObject(RepoObject repoObject) {
    if (defaultBucket.objects.containsKey(repoObject.getKey())) throw new InMemoryContentRepoServiceException();
    return autoCreateRepoObject(repoObject);
  }

  @Override
  public RepoObjectMetadata versionRepoObject(RepoObject repoObject) {
    if (!defaultBucket.objects.containsKey(repoObject.getKey())) throw new InMemoryContentRepoServiceException();
    return autoCreateRepoObject(repoObject);
  }

  private static int getNextVersionNumber(List<? extends FakeEntity> entities) {
    int versionNumber;
    if (entities.isEmpty()) {
      versionNumber = 0;
    } else {
      FakeEntity lastExisting = entities.get(entities.size() - 1);
      versionNumber = lastExisting.number + 1;
    }
    return versionNumber;
  }

  @Override
  public RepoObjectMetadata autoCreateRepoObject(RepoObject repoObject) {
    String key = repoObject.getKey();
    List<FakeObject> existing = defaultBucket.objects.get(key);
    int versionNumber = getNextVersionNumber(existing);

    byte[] content;
    try (InputStream stream = repoObject.getContentAccessor().open()) {
      content = ByteStreams.toByteArray(stream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    FakeObject created = new FakeObject(key, versionNumber, repoObject.getTag(), content);
    created.userMetadata = repoObject.getUserMetadata();
    created.downloadName = repoObject.getDownloadName();
    created.contentType = repoObject.getContentType();

    existing.add(created);
    return created.getMetadata();
  }

  private static <M extends RepoMetadata, E extends FakeEntity<? extends M>>
  List<M> getEntitySlice(Iterable<? extends E> entities, int offset, int limit, boolean includeDeleted, String tag) {
    Preconditions.checkArgument(offset >= 0);
    Preconditions.checkArgument(limit >= 0);

    List<M> result = new ArrayList<>(limit);
    // This fake implementation is O(offset + limit), though a production implementation is supposed to be O(limit).
    for (FakeEntity<? extends M> entity : entities) {
      if (result.size() >= limit) break;
      if ((includeDeleted || entity.status == Status.USED) &&
          (tag == null || tag.equals(entity.tag))) {
        if (offset-- <= 0) {
          result.add(entity.getMetadata());
        }
      }
    }
    return result;
  }

  @Override
  public List<RepoObjectMetadata> getRepoObjects(int offset, int limit, boolean includeDeleted, String tag) {
    return getEntitySlice(defaultBucket.objects.values(), offset, limit, includeDeleted, tag);
  }

  @Override
  public RepoCollectionList createCollection(RepoCollection repoCollection) {
    if (defaultBucket.objects.containsKey(repoCollection.getKey())) throw new InMemoryContentRepoServiceException();
    return autoCreateCollection(repoCollection);
  }

  @Override
  public RepoCollectionList versionCollection(RepoCollection repoCollection) {
    if (!defaultBucket.objects.containsKey(repoCollection.getKey())) throw new InMemoryContentRepoServiceException();
    return autoCreateCollection(repoCollection);
  }

  @Override
  public RepoCollectionList autoCreateCollection(RepoCollection repoCollection) {
    String key = repoCollection.getKey();
    List<FakeCollection> existing = defaultBucket.collections.get(key);
    int versionNumber = getNextVersionNumber(existing);

    FakeCollection created = new FakeCollection(key, versionNumber, repoCollection.getTag());
    for (RepoVersion objectVersion : repoCollection.getObjects()) {
      if (getRepoObjectMetadata(objectVersion) == null) throw new InMemoryContentRepoServiceException();
      created.objectIds.add(objectVersion);
    }
    created.userMetadata = repoCollection.getUserMetadata();

    existing.add(created);
    return created.getMetadata();
  }

  @Override
  public boolean deleteCollection(RepoVersion version) {
    return delete(getFrom(defaultBucket.collections, version));
  }

  @Override
  public boolean deleteCollection(RepoVersionNumber number) {
    return delete(getFrom(defaultBucket.collections, number));
  }

  @Override
  public RepoCollectionList getCollection(RepoVersion version) {
    return getFrom(defaultBucket.collections, version).getMetadata();
  }

  @Override
  public RepoCollectionList getCollection(RepoVersionNumber number) {
    return getFrom(defaultBucket.collections, number).getMetadata();
  }

  @Override
  public RepoCollectionList getCollection(RepoVersionTag tagObj) {
    return getFrom(defaultBucket.collections, tagObj).getMetadata();
  }

  @Override
  public RepoCollectionMetadata getLatestCollection(String key) {
    return getLatest(defaultBucket.collections, key).getMetadata();
  }

  @Override
  public List<RepoCollectionList> getCollectionVersions(String key) {
    List<FakeCollection> collections = defaultBucket.collections.get(key);
    List<RepoCollectionList> metadata = new ArrayList<>(collections.size());
    for (FakeCollection collection : collections) {
      metadata.add(collection.getMetadata());
    }
    return metadata;
  }

  @Override
  public List<RepoCollectionMetadata> getCollections(int offset, int limit, boolean includeDeleted, String tag) {
    return InMemoryContentRepoService.<RepoCollectionMetadata, FakeCollection>getEntitySlice(
        defaultBucket.collections.values(), offset, limit, includeDeleted, tag);
  }

}
