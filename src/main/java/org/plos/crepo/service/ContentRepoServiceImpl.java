package org.plos.crepo.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.model.validator.RepoObjectValidator;
import org.plos.crepo.util.HttpResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContentRepoServiceImpl implements ContentRepoService {
  private static final Logger log = LoggerFactory.getLogger(ContentRepoServiceImpl.class);

  private final ContentRepoAccessConfig accessConfig;
  private final Gson gson;

  private final ContentRepoConfigDao contentRepoConfigDao;
  private final ContentRepoBucketsDao contentRepoBucketsDao;
  private final ContentRepoObjectDao contentRepoObjectDao;
  private final ContentRepoCollectionDao contentRepoCollectionDao;

  public ContentRepoServiceImpl(ContentRepoAccessConfig accessConfig) {
    this.accessConfig = Preconditions.checkNotNull(accessConfig);
    gson = new Gson();

    contentRepoConfigDao = new ContentRepoConfigDaoImpl(accessConfig);
    contentRepoBucketsDao = new ContentRepoBucketDaoImpl(accessConfig);
    contentRepoObjectDao = new ContentRepoObjectDaoImpl(accessConfig);
    contentRepoCollectionDao = new ContentRepoCollectionDaoImpl(accessConfig);
  }

  ContentRepoServiceImpl(TestContentRepoServiceBuilder builder) {
    this.accessConfig = builder.getAccessConfig();
    this.gson = builder.getGson();
    this.contentRepoConfigDao = builder.getConfigDao();
    this.contentRepoBucketsDao = builder.getBucketsDao();
    this.contentRepoObjectDao = builder.getObjectDao();
    this.contentRepoCollectionDao = builder.getCollectionDao();
  }

  private ContentRepoException serviceServerException(Exception e, String logMessage) {
    log.error(logMessage, e);
    return new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
        .baseException(e)
        .build();
  }

  // ------------------------ Config ------------------------

  @Override
  public boolean hasXReproxy() {
    try (CloseableHttpResponse response = contentRepoConfigDao.hasReProxy()) {
      String resString = HttpResponseUtil.getResponseAsString(response);
      return Boolean.parseBoolean(resString);
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when fetching the reproxy information. RepoMessage: ");
    }
  }

  @Override
  public Map<String, Object> getRepoConfig() {
    try (CloseableHttpResponse response = contentRepoConfigDao.getRepoConfig()) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response whenfetching the repo configuration. RepoMessage: ");
    }
  }

  @Override
  public Map<String, Object> getRepoStatus() {
    try (CloseableHttpResponse response = contentRepoConfigDao.getRepoStatus()) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when fetching the repo status information. RepoMessage: ");
    }

  }


  // ------------------------ Buckets ------------------------

  @Override
  public List<Map<String, Object>> getBuckets() {
    try (CloseableHttpResponse response = this.contentRepoBucketsDao.getBuckets()) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {
      }.getType());
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when getting all the buckets. RepoMessage: ");
    }
  }

  @Override
  public Map<String, Object> getBucket(String key) {
    validateBucketKey(key);
    try (CloseableHttpResponse response = this.contentRepoBucketsDao.getBucket(key)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
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
    try (CloseableHttpResponse response = this.contentRepoBucketsDao.createBucket(key)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when creating a bucket. RepoMessage: ");
    }

  }


  // ------------------------ Objects ------------------------

  @Override
  public List<URL> getRepoObjRedirectURL(String key) {
    validateObjectKey(key);
    return getUrlsFromMeta(this.getRepoObjMetaLatestVersion(key));
  }

  @Override
  public List<URL> getRepoObjRedirectURL(String key, String versionChecksum) {
    validateObjectKey(key);
    validateObjectCks(versionChecksum);
    return getUrlsFromMeta(this.getRepoObjMetaUsingVersionChecksum(key, versionChecksum));
  }

  private List<URL> getUrlsFromMeta(Map<String, Object> repoObjValues) {
    String paths = (String) repoObjValues.get("reproxyURL");

    if (StringUtils.isEmpty(paths)) {
      return ImmutableList.of();
    }

    return getUrls(paths);
  }

  private List<URL> getUrls(String paths) {
    String[] pathArray = paths.split("\\s");

    int pathCount = pathArray.length;
    List<URL> urls = new ArrayList<>(pathCount);

    for (int i = 0; i < pathCount; i++) {
      try {
        urls.add(new URL(pathArray[i]));
      } catch (MalformedURLException e) {
        log.error("Error trying to get the urls. paths: " + paths + " + repoMessage:  ", e);
        throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingReProxyUrl)
            .baseException(e)
            .build();
      }
    }
    return urls;
  }

  @Override
  public InputStream getLatestRepoObj(String key) {
    validateObjectKey(key);
    CloseableHttpResponse response = contentRepoObjectDao.getLatestRepoObj(accessConfig.getBucketName(), key);
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
  public InputStream getRepoObjUsingVersionCks(String key, String versionChecksum) {
    validateObjectKey(key);
    validateObjectCks(versionChecksum);
    CloseableHttpResponse response = contentRepoObjectDao.getRepoObjUsingVersionCks(accessConfig.getBucketName(), key, versionChecksum);
    try {
      return response.getEntity().getContent();
    } catch (IOException e) {
      log.error("Error getting the repoObj content from the response, when using the version checksum." +
          "  key " + key + " versionNumber: " + versionChecksum, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }

  @Override
  public InputStream getRepoObjUsingVersionNum(String key, int versionNumber) {
    validateObjectKey(key);
    CloseableHttpResponse response = contentRepoObjectDao.getRepoObjUsingVersionNum(accessConfig.getBucketName(), key, versionNumber);

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

  @Override
  public Map<String, Object> getRepoObjMetaLatestVersion(String key) {
    validateObjectKey(key);
    try (CloseableHttpResponse response = contentRepoObjectDao.getRepoObjMetaLatestVersion(accessConfig.getBucketName(), key)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when fetching a the object meta data. Key: ")
          .append(key)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public Map<String, Object> getRepoObjMetaUsingVersionChecksum(String key, String versionChecksum) {
    validateObjectKey(key);
    validateObjectCks(versionChecksum);
    try (CloseableHttpResponse response = contentRepoObjectDao.getRepoObjMetaUsingVersionChecksum(accessConfig.getBucketName(), key, versionChecksum)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when fetching a the object meta data using the version checksum. Key: ")
          .append(key)
          .append(" versionChecksum: ")
          .append(versionChecksum)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }
  }

  @Override
  public Map<String, Object> getRepoObjMetaUsingVersionNum(String key, int versionNumber) {
    validateObjectKey(key);
    try (CloseableHttpResponse response = contentRepoObjectDao.getRepoObjMetaUsingVersionNumber(accessConfig.getBucketName(), key, versionNumber)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
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
  public Map<String, Object> getRepoObjMetaUsingTag(String key, String tag) {
    validateObjectKey(key);
    validateObjectTag(tag);
    try (CloseableHttpResponse response = contentRepoObjectDao.getRepoObjMetaUsingTag(accessConfig.getBucketName(), key, tag)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
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
  public List<Map<String, Object>> getRepoObjVersions(String key) {
    validateObjectKey(key);
    try (CloseableHttpResponse response = contentRepoObjectDao.getRepoObjVersionsMeta(accessConfig.getBucketName(), key)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {
      }.getType());
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when fetching a the versions of an object. Key: ")
          .append(key)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }


  @Override
  public boolean deleteLatestRepoObj(String key) {
    validateObjectKey(key);
    Map<String, Object> repoObj = this.getRepoObjMetaLatestVersion(key);
    String versionChecksum = (String) repoObj.get("versionChecksum");
    try (CloseableHttpResponse response = contentRepoObjectDao.deleteRepoObjUsingVersionCks(accessConfig.getBucketName(), key, versionChecksum)) {
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
  public boolean deleteRepoObjUsingVersionCks(String key, String versionChecksum) {
    validateObjectKey(key);
    validateObjectCks(versionChecksum);
    try (CloseableHttpResponse response = contentRepoObjectDao.deleteRepoObjUsingVersionCks(accessConfig.getBucketName(), key, versionChecksum)) {
      return true;
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when deleting an object using the version checksum. Key: ")
          .append(key)
          .append(" versionChecksum: ")
          .append(versionChecksum)
          .append("RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public boolean deleteRepoObjUsingVersionNum(String key, int versionNumber) {
    validateObjectKey(key);
    try (CloseableHttpResponse response = contentRepoObjectDao.deleteRepoObjUsingVersionNumber(accessConfig.getBucketName(), key, versionNumber)) {
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
  public Map<String, Object> createRepoObject(RepoObject repoObject) {
    RepoObjectValidator.validate(repoObject);
    try (CloseableHttpResponse response =
             contentRepoObjectDao.createRepoObj(accessConfig.getBucketName(), repoObject, getFileContentType(repoObject, repoObject.getFileContent()))) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when creating an object. RepoMessage: ");
    }
  }

  @Override
  public Map<String, Object> versionRepoObject(RepoObject repoObject) {
    RepoObjectValidator.validate(repoObject);
    try (CloseableHttpResponse response = contentRepoObjectDao.versionRepoObj(accessConfig.getBucketName(), repoObject, getFileContentType(repoObject, repoObject.getFileContent()))) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when versioning an object. RepoMessage: ");
    }
  }

  @Override
  public Map<String, Object> autoCreateRepoObject(RepoObject repoObject) {
    RepoObjectValidator.validate(repoObject);
    try (CloseableHttpResponse response = contentRepoObjectDao.autoCreateRepoObj(accessConfig.getBucketName(), repoObject, getFileContentType(repoObject, repoObject.getFileContent()))) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when trying to auto create an object. RepoMessage: ");
    }
  }

  @Override
  public List<Map<String, Object>> getRepoObjects(int offset, int limit, boolean includeDeleted, String tag) {
    try (CloseableHttpResponse response = getObjectsCloseableResp(offset, limit, includeDeleted, tag)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {
      }.getType());
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when fetching a list of objects. RepoMessage: ");
    }

  }

  private CloseableHttpResponse getObjectsCloseableResp(int offset, int limit, boolean includeDeleted, String tag) {
    if (StringUtils.isEmpty(tag)) {
      return contentRepoObjectDao.getObjects(accessConfig.getBucketName(), offset, limit, includeDeleted);
    }
    return contentRepoObjectDao.getObjectsUsingTag(accessConfig.getBucketName(), offset, limit, includeDeleted, tag);
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

  private void validateObjectKey(String key) {
    if (StringUtils.isEmpty(key)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyObjectKey)
          .build();
    }
  }

  private void validateObjectCks(String versionChecksum) {
    if (StringUtils.isEmpty(versionChecksum)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyObjectCks)
          .build();
    }
  }

  private void validateObjectTag(String tag) {
    if (StringUtils.isEmpty(tag)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyObjectTag)
          .build();
    }
  }


  // ------------------------ Collections ------------------------

  @Override
  public Map<String, Object> createCollection(RepoCollection repoCollection) {
    validateCollectionKey(repoCollection.getKey());
    try (CloseableHttpResponse response = contentRepoCollectionDao.createCollection(accessConfig.getBucketName(), repoCollection)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when creating a collection. RepoMessage: ");
    }
  }

  @Override
  public Map<String, Object> versionCollection(RepoCollection repoCollection) {
    validateCollectionKey(repoCollection.getKey());
    try (CloseableHttpResponse response = contentRepoCollectionDao.versionCollection(accessConfig.getBucketName(), repoCollection)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when creating a collection. RepoMessage: ");
    }

  }

  @Override
  public boolean deleteCollectionUsingVersionCks(String key, String versionChecksum) {
    validateCollectionKey(key);
    validateCollectionCks(versionChecksum);
    try (CloseableHttpResponse response = contentRepoCollectionDao.deleteCollectionUsingVersionCks(accessConfig.getBucketName(), key, versionChecksum)) {
      return true;
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when deleting a collection using the version checksum. Key: ")
          .append(key)
          .append(", versionChecksum: ")
          .append(versionChecksum)
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }

  }

  @Override
  public boolean deleteCollectionUsingVersionNumb(String key, int versionNumber) {
    validateCollectionKey(key);
    try (CloseableHttpResponse response = contentRepoCollectionDao.deleteCollectionUsingVersionNumb(accessConfig.getBucketName(), key, versionNumber)) {
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
  public Map<String, Object> getCollectionUsingVersionCks(String key, String versionChecksum) {
    validateCollectionKey(key);
    validateCollectionCks(versionChecksum);
    try (CloseableHttpResponse response = contentRepoCollectionDao.getCollectionUsingVersionCks(accessConfig.getBucketName(), key, versionChecksum)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when getting a collection using the version checksum. Key: ")
          .append(key)
          .append(", versionChecksum: ")
          .append(versionChecksum)
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }
  }

  @Override
  public Map<String, Object> getCollectionUsingVersionNumber(String key, int versionNumber) {
    validateCollectionKey(key);
    try (CloseableHttpResponse response = contentRepoCollectionDao.getCollectionUsingVersionNumber(accessConfig.getBucketName(), key, versionNumber)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
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
  public Map<String, Object> getCollectionUsingTag(String key, String tag) {
    validateCollectionKey(key);
    validateCollectionTag(tag);
    try (CloseableHttpResponse response = contentRepoCollectionDao.getCollectionUsingTag(accessConfig.getBucketName(), key, tag)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
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
  public List<Map<String, Object>> getCollectionVersions(String key) {
    validateCollectionKey(key);
    try (CloseableHttpResponse response = contentRepoCollectionDao.getCollectionVersions(accessConfig.getBucketName(), key)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {
      }.getType());
    } catch (IOException e) {
      StringBuilder logMessage = new StringBuilder()
          .append("Error handling the response when getting the versions of a collection. Key: ")
          .append(key)
          .append(" RepoMessage: ");
      throw serviceServerException(e, logMessage.toString());
    }
  }

  @Override
  public List<Map<String, Object>> getCollections(int offset, int limit, boolean includeDeleted, String tag) {
    try (CloseableHttpResponse response = getCollectionsCloseableResp(offset, limit, includeDeleted, tag)) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {
      }.getType());
    } catch (IOException e) {
      throw serviceServerException(e, "Error handling the response when getting all the collections. RepoMessage: ");
    }

  }

  private CloseableHttpResponse getCollectionsCloseableResp(int offset, int limit, boolean includeDeleted, String tag) {
    if (StringUtils.isEmpty(tag)) {
      return contentRepoCollectionDao.getCollections(accessConfig.getBucketName(), offset, limit, includeDeleted);
    }
    return contentRepoCollectionDao.getCollectionsUsingTag(accessConfig.getBucketName(), offset, limit, includeDeleted, tag);
  }

  private void validateCollectionKey(String key) {
    if (StringUtils.isEmpty(key)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyCollectionKey)
          .build();
    }
  }

  private void validateCollectionCks(String versionChecksum) {
    if (StringUtils.isEmpty(versionChecksum)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyCollectionCks)
          .build();
    }
  }

  private void validateCollectionTag(String tag) {
    if (StringUtils.isEmpty(tag)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyCollectionTag)
          .build();
    }
  }


}
