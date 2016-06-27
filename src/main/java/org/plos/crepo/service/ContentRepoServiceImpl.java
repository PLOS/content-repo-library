package org.plos.crepo.service;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.config.HttpClientFunction;
import org.plos.crepo.dao.buckets.ContentRepoBucketsDao;
import org.plos.crepo.dao.buckets.impl.ContentRepoBucketDaoImpl;
import org.plos.crepo.dao.collections.ContentRepoCollectionDao;
import org.plos.crepo.dao.collections.impl.ContentRepoCollectionDaoImpl;
import org.plos.crepo.dao.config.ContentRepoConfigDao;
import org.plos.crepo.dao.config.impl.ContentRepoConfigDaoImpl;
import org.plos.crepo.dao.objects.ContentRepoObjectDao;
import org.plos.crepo.dao.objects.impl.ContentRepoObjectDaoImpl;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.identity.RepoId;
import org.plos.crepo.model.identity.RepoVersion;
import org.plos.crepo.model.identity.RepoVersionNumber;
import org.plos.crepo.model.identity.RepoVersionTag;
import org.plos.crepo.model.input.RepoCollectionInput;
import org.plos.crepo.model.input.RepoObjectInput;
import org.plos.crepo.model.metadata.RepoCollectionList;
import org.plos.crepo.model.metadata.RepoCollectionMetadata;
import org.plos.crepo.model.metadata.RepoObjectMetadata;
import org.plos.crepo.model.validator.RepoObjectValidator;
import org.plos.crepo.util.HttpResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContentRepoServiceImpl implements ContentRepoService {
  private static final Logger log = LoggerFactory.getLogger(ContentRepoServiceImpl.class);

  private static final Type MAP_TOKEN = new TypeToken<Map<String, Object>>() {
  }.getType();
  public static final Type LIST_OF_MAPS_TOKENS = new TypeToken<List<Map<String, Object>>>() {
  }.getType();

  private final ContentRepoAccessConfig accessConfig;
  private final Gson gson;

  private final ContentRepoConfigDao configDao;
  private final ContentRepoBucketsDao bucketsDao;
  private final ContentRepoObjectDao objectDao;
  private final ContentRepoCollectionDao collectionDao;

  public ContentRepoServiceImpl(String repoServer, HttpClientFunction client) {
    this.accessConfig = new ContentRepoAccessConfig(repoServer, client);
    gson = new Gson();

    configDao = new ContentRepoConfigDaoImpl(accessConfig);
    bucketsDao = new ContentRepoBucketDaoImpl(accessConfig);
    objectDao = new ContentRepoObjectDaoImpl(accessConfig);
    collectionDao = new ContentRepoCollectionDaoImpl(accessConfig);
  }

  ContentRepoServiceImpl(TestContentRepoServiceBuilder builder) {
    this.accessConfig = builder.getAccessConfig();
    this.gson = builder.getGson();
    this.configDao = builder.getConfigDao();
    this.bucketsDao = builder.getBucketsDao();
    this.objectDao = builder.getObjectDao();
    this.collectionDao = builder.getCollectionDao();
  }

  private ContentRepoException serviceServerException(Exception e, String logMessage) {
    log.error(logMessage, e);
    return new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
        .baseException(e)
        .build();
  }

  private <T> T readJsonResponse(CloseableHttpResponse response, Type typeOfT) throws IOException {
    final Charset charset = Charsets.UTF_8; // TODO: Read from response Content-Type?
    try (InputStream stream = response.getEntity().getContent()) {
      return gson.fromJson(new InputStreamReader(stream, charset), typeOfT);
    } finally {
      response.close();
    }
  }


  // ------------------------ Config ------------------------

  @Override
  public boolean hasXReproxy() {
    try (CloseableHttpResponse response = configDao.hasReProxy()) {
      String resString = HttpResponseUtil.getResponseAsString(response);
      return Boolean.parseBoolean(resString);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when fetching the reproxy information. RepoMessage: ");
    }
  }

  @Override
  public Map<String, Object> getRepoConfig() {
    try (CloseableHttpResponse response = configDao.getRepoConfig()) {
      return readJsonResponse(response, MAP_TOKEN);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response whenfetching the repo configuration. RepoMessage: ");
    }
  }

  @Override
  public Map<String, Object> getRepoStatus() {
    try (CloseableHttpResponse response = configDao.getRepoStatus()) {
      return readJsonResponse(response, MAP_TOKEN);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when fetching the repo status information. RepoMessage: ");
    }

  }


  // ------------------------ Buckets ------------------------

  @Override
  public List<Map<String, Object>> getBuckets() {
    try (CloseableHttpResponse response = this.bucketsDao.getBuckets()) {
      return readJsonResponse(response, LIST_OF_MAPS_TOKENS);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when getting all the buckets. RepoMessage: ");
    }
  }

  @Override
  public Map<String, Object> getBucket(String key) {
    validateBucketKey(key);
    try (CloseableHttpResponse response = this.bucketsDao.getBucket(key)) {
      return readJsonResponse(response, MAP_TOKEN);
    } catch (IOException e) {
      log.error("Error handling the response when getting a bucket. Key:  " + key + " RepoMessage: ", e);
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when getting a bucket. Key:  ")
          .append(key)
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  private void validateBucketKey(String key) {
    if (StringUtils.isEmpty(key)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyBucketKey)
          .build();
    }
  }

  @Override
  public Map<String, Object> createBucket(String key) {
    try (CloseableHttpResponse response = this.bucketsDao.createBucket(key)) {
      return readJsonResponse(response, MAP_TOKEN);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when creating a bucket. RepoMessage: ");
    }

  }


  // ------------------------ Objects ------------------------

  @Override
  public InputStream getLatestRepoObject(RepoId id) {
    String key = id.getKey();
    CloseableHttpResponse response = objectDao.getLatestRepoObj(id.getBucketName(), key);
    try {
      return response.getEntity().getContent();
    } catch (IOException e) {
      log.error("Error getting the latest repoObj content from the response. key:  " + key, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }

  }

  @Override
  public InputStream getRepoObject(RepoVersion version) {
    String key = version.getId().getKey();
    String uuid = version.getUuid().toString();
    CloseableHttpResponse response = objectDao.getRepoObjUsingUuid(version.getId().getBucketName(), key, uuid);
    try {
      return response.getEntity().getContent();
    } catch (IOException e) {
      log.error("Error getting the repoObj content from the response, when using the UUID." +
          "  key " + key + " versionNumber: " + uuid, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }

  @Override
  public InputStream getRepoObject(RepoVersionNumber number) {
    String key = number.getId().getKey();
    int versionNumber = number.getNumber();
    CloseableHttpResponse response = objectDao.getRepoObjUsingVersionNum(number.getId().getBucketName(), key, versionNumber);

    try {
      return response.getEntity().getContent();
    } catch (IOException e) {
      log.error(" Error trying to get the content from the response, using version number." +
          " accessConfig.getBucketName() " + number.getId().getBucketName() + " Key: " + key + " versionNumber: " + versionNumber, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }

  private RepoObjectMetadata buildRepoObjectMetadata(String bucketName, CloseableHttpResponse response) throws IOException {
    Map<String, Object> raw = readJsonResponse(response, MAP_TOKEN);
    return new RepoObjectMetadata(bucketName, raw);
  }

  private List<RepoObjectMetadata> buildRepoObjectMetadataList(String bucketName, CloseableHttpResponse response) throws IOException {
    List<Map<String, Object>> rawList = readJsonResponse(response, LIST_OF_MAPS_TOKENS);
    List<RepoObjectMetadata> list = new ArrayList<>(rawList.size());
    for (Map<String, Object> rawObj : rawList) {
      list.add(new RepoObjectMetadata(bucketName, rawObj));
    }
    return list;
  }

  @Override
  public RepoObjectMetadata getLatestRepoObjectMetadata(RepoId id) {
    String bucketName = id.getBucketName();
    try (CloseableHttpResponse response = objectDao.getRepoObjMetaLatestVersion(bucketName, id.getKey())) {
      return buildRepoObjectMetadata(bucketName, response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when fetching a the object meta data. Key: ")
          .append(id.getKey())
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public RepoObjectMetadata getRepoObjectMetadata(RepoVersion version) {
    String key = version.getId().getKey();
    String uuid = version.getUuid().toString();
    String bucketName = version.getId().getBucketName();
    try (CloseableHttpResponse response = objectDao.getRepoObjMetaUsingUuid(bucketName, key, uuid)) {
      return buildRepoObjectMetadata(bucketName, response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when fetching a the object meta data using the UUID. Key: ")
          .append(key)
          .append(" uuid: ")
          .append(uuid)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }
  }

  @Override
  public RepoObjectMetadata getRepoObjectMetadata(RepoVersionNumber number) {
    String key = number.getId().getKey();
    int versionNumber = number.getNumber();
    String bucketName = number.getId().getBucketName();
    try (CloseableHttpResponse response = objectDao.getRepoObjMetaUsingVersionNumber(bucketName, key, versionNumber)) {
      return buildRepoObjectMetadata(bucketName, response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when fetching a the object meta data using the version number. Key: ")
          .append(key)
          .append(" versionNumber: ")
          .append(versionNumber)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public RepoObjectMetadata getRepoObjectMetadata(RepoVersionTag tagObj) {
    String key = tagObj.getId().getKey();
    String tag = tagObj.getTag();
    String bucketName = tagObj.getId().getBucketName();
    try (CloseableHttpResponse response = objectDao.getRepoObjMetaUsingTag(bucketName, key, tag)) {
      return buildRepoObjectMetadata(bucketName, response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when fetching a the object meta data using the tag. Key: ")
          .append(key)
          .append(" tag: ")
          .append(tag)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public List<RepoObjectMetadata> getRepoObjectVersions(RepoId id) {
    String bucketName = id.getBucketName();
    try (CloseableHttpResponse response = objectDao.getRepoObjVersionsMeta(bucketName, id.getKey())) {
      return buildRepoObjectMetadataList(bucketName, response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when fetching a the versions of an object. Key: ")
          .append(id.getKey())
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }


  @Override
  public boolean deleteLatestRepoObject(RepoId id) {
    RepoObjectMetadata repoObj = this.getLatestRepoObjectMetadata(id);
    String key = id.getKey();
    String uuid = repoObj.getVersion().getUuid().toString();
    String bucketName = id.getBucketName();
    try (CloseableHttpResponse response = objectDao.deleteRepoObjUsingUuid(bucketName, key, uuid)) {
      return true;
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when deleting an object. Key: ")
          .append(key)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public boolean deleteRepoObject(RepoVersion version) {
    String key = version.getId().getKey();
    String uuid = version.getUuid().toString();
    try (CloseableHttpResponse response = objectDao.deleteRepoObjUsingUuid(version.getId().getBucketName(), key, uuid)) {
      return true;
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when deleting an object using the UUID. Key: ")
          .append(key)
          .append(" uuid: ")
          .append(uuid)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public boolean deleteRepoObject(RepoVersionNumber number) {
    String key = number.getId().getKey();
    int versionNumber = number.getNumber();
    try (CloseableHttpResponse response = objectDao.deleteRepoObjUsingVersionNumber(number.getId().getBucketName(), key, versionNumber)) {
      return true;
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when deleting an object using the version number. Key: ")
          .append(key)
          .append(" versionNumber: ")
          .append(versionNumber)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public RepoObjectMetadata createRepoObject(RepoObjectInput repoObjectInput) {
    RepoObjectValidator.validate(repoObjectInput);
    String bucketName = repoObjectInput.getBucketName();
    try (CloseableHttpResponse response =
             objectDao.createRepoObj(bucketName, repoObjectInput, repoObjectInput.probeContentType())) {
      return buildRepoObjectMetadata(bucketName, response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when creating an object. RepoMessage: ");
    }
  }

  @Override
  public RepoObjectMetadata versionRepoObject(RepoObjectInput repoObjectInput) {
    RepoObjectValidator.validate(repoObjectInput);
    String bucketName = repoObjectInput.getBucketName();
    try (CloseableHttpResponse response = objectDao.versionRepoObj(bucketName, repoObjectInput, repoObjectInput.probeContentType())) {
      return buildRepoObjectMetadata(bucketName, response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when versioning an object. RepoMessage: ");
    }
  }

  @Override
  public RepoObjectMetadata autoCreateRepoObject(RepoObjectInput repoObjectInput) {
    RepoObjectValidator.validate(repoObjectInput);
    String bucketName = repoObjectInput.getBucketName();
    try (CloseableHttpResponse response = objectDao.autoCreateRepoObj(bucketName, repoObjectInput, repoObjectInput.probeContentType())) {
      return buildRepoObjectMetadata(bucketName, response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when trying to auto create an object. RepoMessage: ");
    }
  }

  @Override
  public List<RepoObjectMetadata> getRepoObjects(String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    try (CloseableHttpResponse response = getObjectsCloseableResp(bucketName, offset, limit, includeDeleted, tag)) {
      return buildRepoObjectMetadataList(bucketName, response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when fetching a list of objects. RepoMessage: ");
    }

  }

  private CloseableHttpResponse getObjectsCloseableResp(String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    if (StringUtils.isEmpty(tag)) {
      return objectDao.getObjects(bucketName, offset, limit, includeDeleted);
    }
    return objectDao.getObjectsUsingTag(bucketName, offset, limit, includeDeleted, tag);
  }


  // ------------------------ Collections ------------------------

  private RepoCollectionList buildRepoCollectionMetadata(String bucketName, HttpResponse response) {
    Map<String, Object> raw = gson.fromJson(HttpResponseUtil.getResponseAsString(response), MAP_TOKEN);
    return new RepoCollectionList(bucketName, raw);
  }

  private List<RepoCollectionList> buildRepoCollectionMetadataList(String bucketName, HttpResponse response) {
    List<Map<String, Object>> rawList = gson.fromJson(HttpResponseUtil.getResponseAsString(response), LIST_OF_MAPS_TOKENS);
    List<RepoCollectionList> list = new ArrayList<>(rawList.size());
    for (Map<String, Object> rawObj : rawList) {
      list.add(new RepoCollectionList(bucketName, rawObj));
    }
    return list;
  }

  private List<RepoCollectionMetadata> buildRepoCollectionObjectsMetadataList(String bucketName, HttpResponse response) {
    List<Map<String, Object>> rawList = gson.fromJson(HttpResponseUtil.getResponseAsString(response), LIST_OF_MAPS_TOKENS);
    List<RepoCollectionMetadata> list = new ArrayList<>(rawList.size());
    for (Map<String, Object> rawObj : rawList) {
      list.add(new RepoCollectionMetadata(bucketName, rawObj));
    }
    return list;
  }

  @Override
  public RepoCollectionList createCollection(RepoCollectionInput repoCollectionInput) {
    String bucketName = repoCollectionInput.getBucketName();
    RepoId.create(bucketName, repoCollectionInput.getKey()); // validate
    try (CloseableHttpResponse response = collectionDao.createCollection(bucketName, repoCollectionInput)) {
      return buildRepoCollectionMetadata(bucketName, response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when creating a collection. RepoMessage: ");
    }
  }

  @Override
  public RepoCollectionList versionCollection(RepoCollectionInput repoCollectionInput) {
    String bucketName = repoCollectionInput.getBucketName();
    RepoId.create(bucketName, repoCollectionInput.getKey()); // validate
    try (CloseableHttpResponse response = collectionDao.versionCollection(bucketName, repoCollectionInput)) {
      return buildRepoCollectionMetadata(bucketName, response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when creating a collection. RepoMessage: ");
    }

  }

  @Override
  public RepoCollectionList autoCreateCollection(RepoCollectionInput repoCollectionInput) {
    String bucketName = repoCollectionInput.getBucketName();
    RepoId.create(bucketName, repoCollectionInput.getKey()); // validate
    try (CloseableHttpResponse response = collectionDao.autoCreateCollection(bucketName, repoCollectionInput)) {
      return buildRepoCollectionMetadata(bucketName, response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when creating a collection. RepoMessage: ");
    }
  }

  @Override
  public boolean deleteCollection(RepoVersion version) {
    String key = version.getId().getKey();
    String uuid = version.getUuid().toString();
    try (CloseableHttpResponse response = collectionDao.deleteCollectionUsingUuid(version.getId().getBucketName(), key, uuid)) {
      return true;
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when deleting a collection using the UUID. Key: ")
          .append(key)
          .append(", uuid: ")
          .append(uuid)
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public boolean deleteCollection(RepoVersionNumber number) {
    String key = number.getId().getKey();
    int versionNumber = number.getNumber();
    try (CloseableHttpResponse response = collectionDao.deleteCollectionUsingVersionNumber(number.getId().getBucketName(), key, versionNumber)) {
      return true;
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when deleting a collection using the version number. Key: ")
          .append(key)
          .append(", versionNumber: ")
          .append(versionNumber)
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }
  }

  @Override
  public RepoCollectionList getCollection(RepoVersion version) {
    String key = version.getId().getKey();
    String uuid = version.getUuid().toString();
    String bucketName = version.getId().getBucketName();
    try (CloseableHttpResponse response = collectionDao.getCollectionUsingUuid(bucketName, key, uuid)) {
      return buildRepoCollectionMetadata(bucketName, response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when getting a collection using the UUID. Key: ")
          .append(key)
          .append(", uuid: ")
          .append(uuid)
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }
  }

  @Override
  public RepoCollectionList getCollection(RepoVersionNumber number) {
    String key = number.getId().getKey();
    int versionNumber = number.getNumber();
    String bucketName = number.getId().getBucketName();
    try (CloseableHttpResponse response = collectionDao.getCollectionUsingVersionNumber(bucketName, key, versionNumber)) {
      return buildRepoCollectionMetadata(bucketName, response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when getting a collection using the version number. Key: ")
          .append(key)
          .append(", versionNumber: ")
          .append(versionNumber)
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }
  }

  @Override
  public RepoCollectionList getCollection(RepoVersionTag tagObj) {
    String key = tagObj.getId().getKey();
    String tag = tagObj.getTag();
    String bucketName = tagObj.getId().getBucketName();
    try (CloseableHttpResponse response = collectionDao.getCollectionUsingTag(bucketName, key, tag)) {
      return buildRepoCollectionMetadata(bucketName, response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when getting a collection using the version tag. Key: ")
          .append(key)
          .append(", tag: ")
          .append(tag)
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }
  }

  @Override
  public RepoCollectionMetadata getLatestCollection(RepoId id) {
    String bucketName = id.getBucketName();
    try (CloseableHttpResponse response = collectionDao.getLatestCollection(bucketName, id.getKey())) {
      return buildRepoCollectionMetadata(bucketName, response);
    } catch (IOException e) {
      throw serviceServerException(e,
          "Error handling the response when getting the latest collection using the key. Key: " + id.getKey() + " RepoMessage: ");
    }
  }

  public List<RepoCollectionList> getCollectionVersions(RepoId id) {
    String bucketName = id.getBucketName();
    try (CloseableHttpResponse response = collectionDao.getCollectionVersions(bucketName, id.getKey())) {
      return buildRepoCollectionMetadataList(bucketName, response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when getting the versions of a collection. Key: ")
          .append(id.getKey())
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }
  }

  @Override
  public List<RepoCollectionMetadata> getCollections(String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    try (CloseableHttpResponse response = getCollectionsCloseableResp(bucketName, offset, limit, includeDeleted, tag)) {
      return buildRepoCollectionObjectsMetadataList(bucketName, response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when getting all the collections. RepoMessage: ");
    }

  }

  private CloseableHttpResponse getCollectionsCloseableResp(String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    if (StringUtils.isEmpty(tag)) {
      return collectionDao.getCollections(bucketName, offset, limit, includeDeleted);
    }
    return collectionDao.getCollectionsUsingTag(bucketName, offset, limit, includeDeleted, tag);
  }

}
