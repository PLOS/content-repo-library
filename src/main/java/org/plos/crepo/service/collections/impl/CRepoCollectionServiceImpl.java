package org.plos.crepo.service.collections.impl;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.collections.ContentRepoCollectionDao;
import org.plos.crepo.dao.collections.impl.ContentRepoCollectionDaoImpl;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.service.collections.CRepoCollectionService;
import org.plos.crepo.util.HttpResponseUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CRepoCollectionServiceImpl implements CRepoCollectionService {

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
    HttpResponse response = contentRepoCollectionDao.createCollection(accessConfig.getBucketName(), repoCollection);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
    }.getType());
  }

  @Override
  public Map<String, Object> versionCollection(RepoCollection repoCollection) {
    validateCollectionKey(repoCollection.getKey());
    HttpResponse response = contentRepoCollectionDao.versionCollection(accessConfig.getBucketName(), repoCollection);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
    }.getType());
  }

  @Override
  public Boolean deleteCollectionUsingVersionCks(String key, String versionChecksum) {
    validateCollectionKey(key);
    validateCollectionCks(versionChecksum);
    contentRepoCollectionDao.deleteCollectionUsingVersionCks(accessConfig.getBucketName(), key, versionChecksum);
    return true;
  }

  @Override
  public Boolean deleteCollectionUsingVersionNumb(String key, int versionNumber) {
    validateCollectionKey(key);
    contentRepoCollectionDao.deleteCollectionUsingVersionNumb(accessConfig.getBucketName(), key, versionNumber);
    return true;
  }

  @Override
  public Map<String, Object> getCollectionUsingVersionCks(String key, String versionChecksum) {
    validateCollectionKey(key);
    validateCollectionCks(versionChecksum);
    HttpResponse response = contentRepoCollectionDao.getCollectionUsingVersionCks(accessConfig.getBucketName(), key, versionChecksum);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
    }.getType());
  }

  @Override
  public Map<String, Object> getCollectionUsingVersionNumber(String key, int versionNumber) {
    validateCollectionKey(key);
    HttpResponse response = contentRepoCollectionDao.getCollectionUsingVersionNumber(accessConfig.getBucketName(), key, versionNumber);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
    }.getType());
  }

  @Override
  public Map<String, Object> getCollectionUsingTag(String key, String tag) {
    validateCollectionKey(key);
    validateCollectionTag(tag);
    HttpResponse response = contentRepoCollectionDao.getCollectionUsingTag(accessConfig.getBucketName(), key, tag);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
    }.getType());
  }

  @Override
  public List<Map<String, Object>> getCollectionVersions(String key) {
    validateCollectionKey(key);
    HttpResponse response = contentRepoCollectionDao.getCollectionVersions(accessConfig.getBucketName(), key);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {
    }.getType());
  }

  @Override
  public List<Map<String, Object>> getCollections(int offset, int limit, boolean includeDeleted, String tag) {
    HttpResponse response = null;
    if (StringUtils.isEmpty(tag)) {
      response = contentRepoCollectionDao.getCollections(accessConfig.getBucketName(), offset, limit, includeDeleted);
    } else {
      response = contentRepoCollectionDao.getCollectionsUsingTag(accessConfig.getBucketName(), offset, limit, includeDeleted, tag);
    }
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {
    }.getType());
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
