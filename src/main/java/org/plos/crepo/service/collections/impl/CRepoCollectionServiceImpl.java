package org.plos.crepo.service.collections.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.plos.crepo.dao.collections.ContentRepoCollectionsDao;
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.validator.RepoObjectValidator;
import org.plos.crepo.service.collections.CRepoCollectionService;
import org.plos.crepo.util.HttpResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CRepoCollectionServiceImpl implements CRepoCollectionService {

  private static Gson gson = new Gson();

  @Autowired
  private ContentRepoCollectionsDao contentRepoCollectionsDao;

  @Autowired
  private RepoObjectValidator repoObjectValidator;

  @Value("${crepo.bucketName}")
  private String bucketName;

  @Override
  public Map<String, Object> createCollection(RepoCollection repoCollection) {
    HttpResponse response = contentRepoCollectionsDao.createCollection(bucketName, repoCollection);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public Map<String, Object> versionCollection(RepoCollection repoCollection) {
    HttpResponse response = contentRepoCollectionsDao.versionCollection(bucketName, repoCollection);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public Boolean deleteCollectionUsingVersionCks(String key, String versionChecksum) {
    contentRepoCollectionsDao.deleteCollectionUsingVersionCks(bucketName, key, versionChecksum);
    return true;
  }

  @Override
  public Boolean deleteCollectionUsingVersionNumb(String key, int versionNumber) {
    contentRepoCollectionsDao.deleteCollectionUsingVersionNumb(bucketName, key, versionNumber);
    return true;
  }

  @Override
  public Map<String, Object> getCollectionUsingVersionCks(String key, String versionChecksum) {
    HttpResponse response = contentRepoCollectionsDao.getCollectionUsingVersionCks(bucketName, key, versionChecksum);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public Map<String, Object> getCollectionUsingVersionNumber(String key, int versionNumber) {
    HttpResponse response = contentRepoCollectionsDao.getCollectionUsingVersionNumber(bucketName, key, versionNumber);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public Map<String, Object> getCollectionUsingTag(String key, String tag) {
    HttpResponse response = contentRepoCollectionsDao.getCollectionUsingTag(bucketName, key, tag);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public List<Map<String, Object>> getCollectionVersions(String key) {
    HttpResponse response = contentRepoCollectionsDao.getCollectionVersions(bucketName, key);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {}.getType());
  }

  @Override
  public List<Map<String, Object>> getCollections(int offset, int limit, boolean includeDeleted, String tag) {
    HttpResponse response = null;
    if (StringUtils.isEmpty(tag)){
      response = contentRepoCollectionsDao.getCollections(bucketName, offset, limit, includeDeleted);
    } else{
      response = contentRepoCollectionsDao.getCollectionsUsingTag(bucketName, offset, limit, includeDeleted, tag);
    }
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {}.getType());
  }


}
