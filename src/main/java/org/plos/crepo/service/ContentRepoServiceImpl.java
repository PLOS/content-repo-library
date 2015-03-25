package org.plos.crepo.service;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.plos.crepo.config.ContentRepoAccessConfig;
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
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoCollectionMetadata;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.model.RepoObjectMetadata;
import org.plos.crepo.model.RepoVersion;
import org.plos.crepo.model.RepoVersionNumber;
import org.plos.crepo.model.RepoVersionTag;
import org.plos.crepo.model.validator.RepoObjectValidator;
import org.plos.crepo.util.HttpResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
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

  public ContentRepoServiceImpl(ContentRepoAccessConfig accessConfig) {
    this.accessConfig = Preconditions.checkNotNull(accessConfig);
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
  public InputStream getLatestRepoObject(String key) {
    RepoVersion.validateKey(key);
    CloseableHttpResponse response = objectDao.getLatestRepoObj(accessConfig.getBucketName(), key);
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
    String key = version.getKey();
    String uuid = version.getUuid().toString();
    CloseableHttpResponse response = objectDao.getRepoObjUsingUuid(accessConfig.getBucketName(), key, uuid);
    try {
      return response.getEntity().getContent();
    } catch (IOException e) {
      log.error("Error getting the repoObj content from the response, when using the version checksum." +
          "  key " + key + " versionNumber: " + uuid, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }

  @Override
  public InputStream getRepoObject(RepoVersionNumber number) {
    String key = number.getKey();
    int versionNumber = number.getNumber();
    CloseableHttpResponse response = objectDao.getRepoObjUsingVersionNum(accessConfig.getBucketName(), key, versionNumber);

    try {
      return response.getEntity().getContent();
    } catch (IOException e) {
      log.error(" Error trying to get the content from the response, using version number." +
          " accessConfig.getBucketName() " + accessConfig.getBucketName() + " Key: " + key + " versionNumber: " + versionNumber, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }

  private RepoObjectMetadata buildRepoObjectMetadata(CloseableHttpResponse response) throws IOException {
    Map<String, Object> raw = readJsonResponse(response, MAP_TOKEN);
    return new RepoObjectMetadata(raw);
  }

  private List<RepoObjectMetadata> buildRepoObjectMetadataList(CloseableHttpResponse response) throws IOException {
    List<Map<String, Object>> rawList = readJsonResponse(response, LIST_OF_MAPS_TOKENS);
    List<RepoObjectMetadata> list = new ArrayList<>(rawList.size());
    for (Map<String, Object> rawObj : rawList) {
      list.add(new RepoObjectMetadata(rawObj));
    }
    return list;
  }

  @Override
  public RepoObjectMetadata getLatestRepoObjectMetadata(String key) {
    RepoVersion.validateKey(key);
    try (CloseableHttpResponse response = objectDao.getRepoObjMetaLatestVersion(accessConfig.getBucketName(), key)) {
      return buildRepoObjectMetadata(response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when fetching a the object meta data. Key: ")
          .append(key)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public RepoObjectMetadata getRepoObjectMetadata(RepoVersion version) {
    String key = version.getKey();
    String uuid = version.getUuid().toString();
    try (CloseableHttpResponse response = objectDao.getRepoObjMetaUsingUuid(accessConfig.getBucketName(), key, uuid)) {
      return buildRepoObjectMetadata(response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when fetching a the object meta data using the version checksum. Key: ")
          .append(key)
          .append(" uuid: ")
          .append(uuid)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }
  }

  @Override
  public RepoObjectMetadata getRepoObjectMetadata(RepoVersionNumber number) {
    String key = number.getKey();
    int versionNumber = number.getNumber();
    try (CloseableHttpResponse response = objectDao.getRepoObjMetaUsingVersionNumber(accessConfig.getBucketName(), key, versionNumber)) {
      return buildRepoObjectMetadata(response);
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
    String key = tagObj.getKey();
    String tag = tagObj.getTag();
    try (CloseableHttpResponse response = objectDao.getRepoObjMetaUsingTag(accessConfig.getBucketName(), key, tag)) {
      return buildRepoObjectMetadata(response);
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
  public List<RepoObjectMetadata> getRepoObjectVersions(String key) {
    RepoVersion.validateKey(key);
    try (CloseableHttpResponse response = objectDao.getRepoObjVersionsMeta(accessConfig.getBucketName(), key)) {
      return buildRepoObjectMetadataList(response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when fetching a the versions of an object. Key: ")
          .append(key)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }


  @Override
  public boolean deleteLatestRepoObject(String key) {
    RepoVersion.validateKey(key);
    RepoObjectMetadata repoObj = this.getLatestRepoObjectMetadata(key);
    String uuid = repoObj.getVersion().getUuid().toString();
    try (CloseableHttpResponse response = objectDao.deleteRepoObjUsingUuid(accessConfig.getBucketName(), key, uuid)) {
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
    String key = version.getKey();
    String uuid = version.getUuid().toString();
    try (CloseableHttpResponse response = objectDao.deleteRepoObjUsingUuid(accessConfig.getBucketName(), key, uuid)) {
      return true;
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when deleting an object using the version checksum. Key: ")
          .append(key)
          .append(" uuid: ")
          .append(uuid)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public boolean deleteRepoObject(RepoVersionNumber number) {
    String key = number.getKey();
    int versionNumber = number.getNumber();
    try (CloseableHttpResponse response = objectDao.deleteRepoObjUsingVersionNumber(accessConfig.getBucketName(), key, versionNumber)) {
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
  public RepoObjectMetadata createRepoObject(RepoObject repoObject) {
    RepoObjectValidator.validate(repoObject);
    try (CloseableHttpResponse response =
             objectDao.createRepoObj(accessConfig.getBucketName(), repoObject, getFileContentType(repoObject, repoObject.getFileContent()))) {
      return buildRepoObjectMetadata(response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when creating an object. RepoMessage: ");
    }
  }

  @Override
  public RepoObjectMetadata versionRepoObject(RepoObject repoObject) {
    RepoObjectValidator.validate(repoObject);
    try (CloseableHttpResponse response = objectDao.versionRepoObj(accessConfig.getBucketName(), repoObject, getFileContentType(repoObject, repoObject.getFileContent()))) {
      return buildRepoObjectMetadata(response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when versioning an object. RepoMessage: ");
    }
  }

  @Override
  public RepoObjectMetadata autoCreateRepoObject(RepoObject repoObject) {
    RepoObjectValidator.validate(repoObject);
    try (CloseableHttpResponse response = objectDao.autoCreateRepoObj(accessConfig.getBucketName(), repoObject, getFileContentType(repoObject, repoObject.getFileContent()))) {
      return buildRepoObjectMetadata(response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when trying to auto create an object. RepoMessage: ");
    }
  }

  @Override
  public List<RepoObjectMetadata> getRepoObjects(int offset, int limit, boolean includeDeleted, String tag) {
    try (CloseableHttpResponse response = getObjectsCloseableResp(offset, limit, includeDeleted, tag)) {
      return buildRepoObjectMetadataList(response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when fetching a list of objects. RepoMessage: ");
    }

  }

  private CloseableHttpResponse getObjectsCloseableResp(int offset, int limit, boolean includeDeleted, String tag) {
    if (StringUtils.isEmpty(tag)) {
      return objectDao.getObjects(accessConfig.getBucketName(), offset, limit, includeDeleted);
    }
    return objectDao.getObjectsUsingTag(accessConfig.getBucketName(), offset, limit, includeDeleted, tag);
  }

  private String getFileContentType(RepoObject repoObject, File file) {
    String contentType = repoObject.getContentType();
    if (StringUtils.isEmpty(contentType)) {
      try {
        contentType = Files.probeContentType(file.toPath());
      } catch (IOException e) {
        e.printStackTrace();
        log.error("Error getting the content type from file. Key: " + repoObject.getKey(), e);
        throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorAccessingFile)
            .baseException(e)
            .key(repoObject.getKey())
            .build();
      }
    }
    return contentType;
  }


  // ------------------------ Collections ------------------------

  private RepoCollectionMetadata buildRepoCollectionMetadata(HttpResponse response) {
    Map<String, Object> raw = gson.fromJson(HttpResponseUtil.getResponseAsString(response), MAP_TOKEN);
    return new RepoCollectionMetadata(raw);
  }

  private List<RepoCollectionMetadata> buildRepoCollectionMetadataList(HttpResponse response) {
    List<Map<String, Object>> rawList = gson.fromJson(HttpResponseUtil.getResponseAsString(response), LIST_OF_MAPS_TOKENS);
    List<RepoCollectionMetadata> list = new ArrayList<>(rawList.size());
    for (Map<String, Object> rawObj : rawList) {
      list.add(new RepoCollectionMetadata(rawObj));
    }
    return list;
  }

  @Override
  public RepoCollectionMetadata createCollection(RepoCollection repoCollection) {
    RepoVersion.validateKey(repoCollection.getKey());
    try (CloseableHttpResponse response = collectionDao.createCollection(accessConfig.getBucketName(), repoCollection)) {
      return buildRepoCollectionMetadata(response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when creating a collection. RepoMessage: ");
    }
  }

  @Override
  public RepoCollectionMetadata versionCollection(RepoCollection repoCollection) {
    RepoVersion.validateKey(repoCollection.getKey());
    try (CloseableHttpResponse response = collectionDao.versionCollection(accessConfig.getBucketName(), repoCollection)) {
      return buildRepoCollectionMetadata(response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when creating a collection. RepoMessage: ");
    }

  }

  @Override
  public boolean deleteCollection(RepoVersion version) {
    String key = version.getKey();
    String uuid = version.getUuid().toString();
    try (CloseableHttpResponse response = collectionDao.deleteCollectionUsingUuid(accessConfig.getBucketName(), key, uuid)) {
      return true;
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when deleting a collection using the version checksum. Key: ")
          .append(key)
          .append(", uuid: ")
          .append(uuid)
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public boolean deleteCollection(RepoVersionNumber number) {
    String key = number.getKey();
    int versionNumber = number.getNumber();
    try (CloseableHttpResponse response = collectionDao.deleteCollectionUsingVersionNumber(accessConfig.getBucketName(), key, versionNumber)) {
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
  public RepoCollectionMetadata getCollection(RepoVersion version) {
    String key = version.getKey();
    String uuid = version.getUuid().toString();
    try (CloseableHttpResponse response = collectionDao.getCollectionUsingUuid(accessConfig.getBucketName(), key, uuid)) {
      return buildRepoCollectionMetadata(response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when getting a collection using the version checksum. Key: ")
          .append(key)
          .append(", uuid: ")
          .append(uuid)
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }
  }

  @Override
  public RepoCollectionMetadata getCollection(RepoVersionNumber number) {
    String key = number.getKey();
    int versionNumber = number.getNumber();
    try (CloseableHttpResponse response = collectionDao.getCollectionUsingVersionNumber(accessConfig.getBucketName(), key, versionNumber)) {
      return buildRepoCollectionMetadata(response);
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
  public RepoCollectionMetadata getCollection(RepoVersionTag tagObj) {
    String key = tagObj.getKey();
    String tag = tagObj.getTag();
    try (CloseableHttpResponse response = collectionDao.getCollectionUsingTag(accessConfig.getBucketName(), key, tag)) {
      return buildRepoCollectionMetadata(response);
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
  public List<RepoCollectionMetadata> getCollectionVersions(String key) {
    RepoVersion.validateKey(key);
    try (CloseableHttpResponse response = collectionDao.getCollectionVersions(accessConfig.getBucketName(), key)) {
      return buildRepoCollectionMetadataList(response);
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when getting the versions of a collection. Key: ")
          .append(key)
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }
  }

  @Override
  public List<RepoCollectionMetadata> getCollections(int offset, int limit, boolean includeDeleted, String tag) {
    try (CloseableHttpResponse response = getCollectionsCloseableResp(offset, limit, includeDeleted, tag)) {
      return buildRepoCollectionMetadataList(response);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when getting all the collections. RepoMessage: ");
    }

  }

  private CloseableHttpResponse getCollectionsCloseableResp(int offset, int limit, boolean includeDeleted, String tag) {
    if (StringUtils.isEmpty(tag)) {
      return collectionDao.getCollections(accessConfig.getBucketName(), offset, limit, includeDeleted);
    }
    return collectionDao.getCollectionsUsingTag(accessConfig.getBucketName(), offset, limit, includeDeleted, tag);
  }

}
