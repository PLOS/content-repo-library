package org.plos.crepo.model.identity;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;

import java.util.Objects;
import java.util.UUID;

/**
 * The native identifier for a version of a repo object or collection. Uses a key and UUID.
 */
public final class RepoVersion {

  private final RepoId id;
  private final UUID uuid;

  private RepoVersion(RepoId id, UUID uuid) {
    this.id = Objects.requireNonNull(id);
    this.uuid = Objects.requireNonNull(uuid);
  }

  /**
   * Represent a version of a repo object.
   *
   * @param id   the object bucket name and key
   * @param uuid the version's UUID as a string
   * @return the repo object version
   * @throws IllegalArgumentException if {@code uuid} is not a valid UUID
   */
  public static RepoVersion create(RepoId id, String uuid) {
    if (StringUtils.isEmpty(uuid)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyUuid).build();
    }
    return create(id, UUID.fromString(uuid));
  }

  /**
   * Represent a version of a repo object.
   *
   * @param id   the object bucket name and key
   * @param uuid the version's UUID
   * @return the repo object version
   */
  public static RepoVersion create(RepoId id, UUID uuid) {
    return new RepoVersion(id, uuid);
  }

  /**
   * Represent a version of a repo object.
   *
   * @param bucketName the object bucket name
   * @param key        the object key
   * @param uuid       the version's UUID as a string
   * @return the repo object version
   * @throws IllegalArgumentException if {@code uuid} is not a valid UUID
   */
  public static RepoVersion create(String bucketName, String key, String uuid) {
    return create(RepoId.create(bucketName, key), uuid);
  }

  /**
   * Represent a version of a repo object.
   *
   * @param bucketName the object bucket name
   * @param key        the object key
   * @param uuid       the version's UUID
   * @return the repo object version
   */
  public static RepoVersion create(String bucketName, String key, UUID uuid) {
    return create(RepoId.create(bucketName, key), uuid);
  }

  public RepoId getId() {
    return id;
  }

  public UUID getUuid() {
    return uuid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoVersion that = (RepoVersion) o;

    if (!id.equals(that.id)) return false;
    if (!uuid.equals(that.uuid)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + uuid.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s(\"%s\", \"%s\", \"%s\")", getClass().getSimpleName(),
        StringEscapeUtils.escapeJava(id.getBucketName()), StringEscapeUtils.escapeJava(id.getKey()), uuid);
  }
}
