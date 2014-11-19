package org.plos.crepo.service.collections.impl;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.collections.ContentRepoCollectionDao;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.service.collections.CRepoCollectionService;
import org.plos.crepo.util.HttpResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CRepoCollectionServiceImpl implements CRepoCollectionService {

  private static final Logger log = LoggerFactory.getLogger(CRepoCollectionServiceImpl.class);

  private final Gson gson;
  private final ContentRepoCollectionDao contentRepoCollectionDao;
  private final ContentRepoAccessConfig accessConfig;

  public CRepoCollectionServiceImpl(ContentRepoAccessConfig accessConfig, ContentRepoCollectionDao contentRepoCollectionDao) {
    this.accessConfig = Preconditions.checkNotNull(accessConfig);
    this.contentRepoCollectionDao = contentRepoCollectionDao;
    gson = new Gson();
  }

  @Override
  public Map<String, Object> createCollection(RepoCollection repoCollection) {
    validateCollectionKey(repoCollection.getKey());
    try (CloseableHttpResponse response = contentRepoCollectionDao.createCollection(accessConfig.getBucketName(), repoCollection) ) {
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      log.error("Error handling the response when creating a collection. RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }
  }

  @Override
  public Map<String, Object> versionCollection(RepoCollection repoCollection) {
    validateCollectionKey(repoCollection.getKey());
    try (CloseableHttpResponse response = contentRepoCollectionDao.versionCollection(accessConfig.getBucketName(), repoCollection)){
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      log.error("Error handling the response when creating a collection. RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }

  }

  @Override
  public Boolean deleteCollectionUsingVersionCks(String key, String versionChecksum) {
    validateCollectionKey(key);
    validateCollectionCks(versionChecksum);
    try (CloseableHttpResponse response = contentRepoCollectionDao.deleteCollectionUsingVersionCks(accessConfig.getBucketName(), key, versionChecksum)){
      return true;
    } catch (IOException e) {
      log.error("Error handling the response when deleting a collection using the version checksum. Key: " + key + ", versionChecksum: "
          + versionChecksum + " RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }

  }

  @Override
  public Boolean deleteCollectionUsingVersionNumb(String key, int versionNumber) {
    validateCollectionKey(key);
    try (CloseableHttpResponse response = contentRepoCollectionDao.deleteCollectionUsingVersionNumb(accessConfig.getBucketName(), key, versionNumber)){
      return true;
    } catch (IOException e) {
      log.error("Error handling the response when deleting a collection using the version number. Key: " + key + ", versionNumber: "
          + versionNumber + " RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }
  }

  @Override
  public Map<String, Object> getCollectionUsingVersionCks(String key, String versionChecksum) {
    validateCollectionKey(key);
    validateCollectionCks(versionChecksum);
    try (CloseableHttpResponse response = contentRepoCollectionDao.getCollectionUsingVersionCks(accessConfig.getBucketName(), key, versionChecksum)){
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      log.error("Error handling the response when getting a collection using the version checksum. Key: " + key
          + ", versionChecksum: " + versionChecksum + " RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }
  }

  @Override
  public Map<String, Object> getCollectionUsingVersionNumber(String key, int versionNumber) {
    validateCollectionKey(key);
    try (CloseableHttpResponse response = contentRepoCollectionDao.getCollectionUsingVersionNumber(accessConfig.getBucketName(), key, versionNumber)){
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      log.error("Error handling the response when getting a collection using the version number. Key: " + key + ", versionNumber: "
          + versionNumber + " RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }
  }

  @Override
  public Map<String, Object> getCollectionUsingTag(String key, String tag) {
    validateCollectionKey(key);
    validateCollectionTag(tag);
    try (CloseableHttpResponse response = contentRepoCollectionDao.getCollectionUsingTag(accessConfig.getBucketName(), key, tag)){
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      log.error("Error handling the response when getting a collection using a tag. Key: "+ key + ", tag: " + tag + " RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }
  }

  @Override
  public List<Map<String, Object>> getCollectionVersions(String key) {
    validateCollectionKey(key);
    try (CloseableHttpResponse response = contentRepoCollectionDao.getCollectionVersions(accessConfig.getBucketName(), key)){
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {
      }.getType());
    } catch (IOException e) {
      log.error("Error handling the response when getting the versions of a collection. Key: " + key + "RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }
  }

  @Override
  public List<Map<String, Object>> getCollections(int offset, int limit, boolean includeDeleted, String tag) {
    try (CloseableHttpResponse response = getCollectionsCloseableResp(offset, limit, includeDeleted, tag) ){
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {
      }.getType());
    } catch (IOException e) {
      log.error("Error handling the response when getting all the collections. RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }

  }

  private CloseableHttpResponse getCollectionsCloseableResp(int offset, int limit, boolean includeDeleted, String tag){
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
