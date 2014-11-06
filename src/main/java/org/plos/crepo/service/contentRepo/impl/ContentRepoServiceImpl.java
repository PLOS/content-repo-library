package org.plos.crepo.service.contentRepo.impl;

import org.plos.crepo.dao.buckets.ContentRepoBucketsDao;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.service.buckets.CRepoBucketService;
import org.plos.crepo.service.buckets.impl.CRepoBucketServiceImpl;
import org.plos.crepo.service.collections.CRepoCollectionService;
import org.plos.crepo.service.collections.impl.CRepoCollectionServiceImpl;
import org.plos.crepo.service.config.CRepoConfigService;
import org.plos.crepo.service.config.impl.CRepoConfigServiceImpl;
import org.plos.crepo.service.objects.CRepoObjectService;
import org.plos.crepo.service.objects.impl.CRepoObjectsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Facade used as an entry point to all the content repo services.
 */
@Service
public class ContentRepoServiceImpl implements CRepoObjectService, CRepoConfigService, CRepoCollectionService, CRepoBucketService {

  private static final Logger log = LoggerFactory.getLogger(ContentRepoServiceImpl.class);

  @Autowired
  private ContentRepoBucketsDao contentRepoBucketDao;

  @Autowired
  private CRepoBucketServiceImpl cRepoBucketService;

  @Autowired
  private CRepoCollectionServiceImpl cRepoCollectionService;

  @Autowired
  private CRepoConfigServiceImpl cRepoConfigService;

  @Autowired
  private CRepoObjectsServiceImpl cRepoObjectService;

  @Autowired
  public ContentRepoServiceImpl(ContentRepoBucketsDao contentRepoBucketDao,
                                @Value("${crepo.bucketName}") String bucketName) throws Exception {

    this.contentRepoBucketDao = contentRepoBucketDao;

    try {
      this.contentRepoBucketDao.getBucket(bucketName);
    } catch(ContentRepoException ce){
      // if it does not exist, create it
      log.debug("The bucket did not exist. Creating the bucket...", ce);
      this.contentRepoBucketDao.createBucket(bucketName);
    }

  }

  public Boolean hasXReproxy() {
    return cRepoConfigService.hasXReproxy();
  }

  @Override
  public Map<String, Object> getRepoConfig() {
    return cRepoConfigService.getRepoConfig();
  }

  @Override
  public Map<String, Object> getRepoStatus() {
    return cRepoConfigService.getRepoStatus();
  }

  public URL[] getRepoObjRedirectURL(String key){ return cRepoObjectService.getRepoObjRedirectURL(key); }

  @Override
  public URL[] getRepoObjRedirectURL(String key, String versionChecksum){
    return cRepoObjectService.getRepoObjRedirectURL(key, versionChecksum);
  }

  @Override
  public InputStream getLatestRepoObjStream(String key){
    return cRepoObjectService.getLatestRepoObjStream(key);
  }

  @Override
  public byte[] getLatestRepoObjByteArray(String key){
   return cRepoObjectService.getLatestRepoObjByteArray(key);
  }

  @Override
  public InputStream getRepoObjStreamUsingVersionCks(String key, String versionChecksum) {
    return cRepoObjectService.getRepoObjStreamUsingVersionCks(key, versionChecksum);
  }

  @Override
  public byte[] getRepoObjByteArrayUsingVersionCks(String key, String versionChecksum) {
    return cRepoObjectService.getRepoObjByteArrayUsingVersionCks(key, versionChecksum);
  }


  @Override
  public InputStream getRepoObjStreamUsingVersionNum(String key, int versionNumber) {
    return cRepoObjectService.getRepoObjStreamUsingVersionNum(key, versionNumber);
  }

  @Override
  public byte[] getRepoObjByteArrayUsingVersionNum(String key, int versionNumber) {
    return cRepoObjectService.getRepoObjByteArrayUsingVersionNum(key, versionNumber);
  }

  @Override
  public Map<String,Object> getRepoObjMetaLatestVersion(String key) {
    return cRepoObjectService.getRepoObjMetaLatestVersion(key);
  }

  @Override
  public Map<String,Object> getRepoObjMetaUsingVersionChecksum(String key, String versionChecksum) {
    return cRepoObjectService.getRepoObjMetaUsingVersionChecksum(key, versionChecksum);
  }

  @Override
  public Map<String,Object> getRepoObjMetaUsingVersionNum(String key, int versionNumber) {
    return cRepoObjectService.getRepoObjMetaUsingVersionNum(key, versionNumber);
  }

  @Override
  public Map<String, Object> getRepoObjMetaUsingTag(String key, String tag) {
    return cRepoObjectService.getRepoObjMetaUsingTag(key, tag);
  }

  @Override
  public List<Map<String, Object>> getRepoObjVersions(String key) {
    return cRepoObjectService.getRepoObjVersions(key);
  }


  @Override
  public Boolean deleteLatestRepoObj(String key) {
    return cRepoObjectService.deleteLatestRepoObj(key);
  }

  @Override
  public Boolean deleteRepoObjUsingVersionCks(String key, String versionChecksum) {
    return cRepoObjectService.deleteRepoObjUsingVersionCks(key, versionChecksum);
  }

  @Override
  public Boolean deleteRepoObjUsingVersionNum(String key, int versionNumber) {
    return cRepoObjectService.deleteRepoObjUsingVersionNum(key, versionNumber);
  }

  @Override
  public Map<String, Object> createRepoObject(RepoObject repoObject) {
    return cRepoObjectService.createRepoObject(repoObject);
  }

  @Override
  public Map<String, Object> versionRepoObject(RepoObject repoObject) {
    return cRepoObjectService.versionRepoObject(repoObject);
  }

  @Override
  public List<Map<String, Object>> getRepoObjects(int offset, int limit, boolean includeDeleted, String tag) {
    return cRepoObjectService.getRepoObjects(offset, limit, includeDeleted, tag);
  }

  public List<Map<String, Object>> getBuckets(){
    return cRepoBucketService.getBuckets();
  }

  public Map<String, Object> getBucket(String key){
    return cRepoBucketService.getBucket(key);
  }

  public Map<String, Object> createBucket(String key){
    return cRepoBucketService.createBucket(key);
  }

  @Override
  public Map<String, Object> createCollection(RepoCollection repoCollection) {
    return cRepoCollectionService.createCollection(repoCollection);
  }

  @Override
  public Map<String, Object> versionCollection(RepoCollection repoCollection) {
    return cRepoCollectionService.versionCollection(repoCollection);
  }

  @Override
  public Boolean deleteCollectionUsingVersionCks(String key, String versionChecksum) {
    return cRepoCollectionService.deleteCollectionUsingVersionCks(key, versionChecksum);
  }

  @Override
  public Boolean deleteCollectionUsingVersionNumb(String key, int versionNumber) {
    return cRepoCollectionService.deleteCollectionUsingVersionNumb(key, versionNumber);
  }

  @Override
  public Map<String, Object> getCollectionUsingVersionCks(String key, String versionChecksum) {
    return cRepoCollectionService.getCollectionUsingVersionCks(key, versionChecksum);
  }

  @Override
  public Map<String, Object> getCollectionUsingVersionNumber(String key, int versionNumber) {
    return cRepoCollectionService.getCollectionUsingVersionNumber(key, versionNumber);
  }

  @Override
  public Map<String, Object> getCollectionUsingTag(String key, String tag) {
    return cRepoCollectionService.getCollectionUsingTag(key, tag);
  }

  @Override
  public List<Map<String, Object>> getCollectionVersions(String key) {
    return cRepoCollectionService.getCollectionVersions(key);
  }

  @Override
  public List<Map<String, Object>> getCollections(int offset, int limit, boolean includeDeleted, String tag) {
    return cRepoCollectionService.getCollections( offset, limit, includeDeleted, tag);
  }

}
