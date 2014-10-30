package org.plos.crepo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ObjectUrlGenerator extends BaseUrlGenerator{

  @Value("${crepo.url.getLatestObject}")
  private String getLatestObjUrl;

  @Value("${crepo.url.getObjUsingVersionNumUrl}")
  private String getObjUsingVersionNumUrl;

  @Value("${crepo.url.getObjVersionCksUrl}")
  private String getObjVersionCksUrl;

  @Value("${crepo.url.getLatestObjMetaUrl}")
  private String getLatestObjMetaUrl;

  @Value("${crepo.url.getObjMetaUsingVersionNumUrl}")
  private String getObjMetaUsingVersionNumUrl;

  @Value("${crepo.url.getObjMetaUsingVersionCksUrl}")
  private String getObjMetaUsingVersionCksUrl;

  @Value("${crepo.url.getObjMetaUsingTagUrl}")
  private String getObjMetaUsingTagUrl;

  @Value("${crepo.url.getObjVersionsUrl}")
  private String getObjVersionsUrl;

  @Value("${crepo.url.deleteObjUsingVersionCksUrl}")
  private String deleteObjUsingVersionCksUrl;

  @Value("${crepo.url.deleteObjUsingVersionNumUrl}")
  private String deleteObjUsingVersionNumUrl;

  @Value("${crepo.url.createObjUrl}")
  private String createObjUrl;

  @Value("${crepo.url.getObjectsUrl}")
  private String getObjectsUrl;

  @Value("${crepo.url.getObjectsUsingTagUrl}")
  private String getObjectsUsingTagUrl;

  public String getLatestObjectUrl(String repoServer, String bucketName, String repoObjKey){
    return replaceUrl(getLatestObjUrl, getObjectBasicMap(repoServer, bucketName, repoObjKey));
  }

  public String getObjectUsingVersionNumUrl(String repoServer, String bucketName, String repoObjKey, int versionNumber){
    return replaceUrl(getObjUsingVersionNumUrl, getObjectMapWithVersionNum(repoServer, bucketName, repoObjKey, versionNumber));
  }

  public String getObjectUsingVersionCksUrl(String repoServer, String bucketName, String repoObjKey, String versionChecksum){
    return replaceUrl(getObjVersionCksUrl,getObjectMapWithVersionCks(repoServer, bucketName, repoObjKey, versionChecksum));
  }

  public String getLatestObjectMetaUrl(String repoServer, String bucketName, String repoObjKey){
    return replaceUrl(getLatestObjMetaUrl, getObjectBasicMap(repoServer, bucketName, repoObjKey));
  }

  public String getObjectMetaUsingVersionNumUrl(String repoServer, String bucketName, String repoObjKey, Integer versionNumber){
    return replaceUrl(getObjMetaUsingVersionNumUrl, getObjectMapWithVersionNum(repoServer, bucketName, repoObjKey, versionNumber));
  }

  public String getObjectMetaUsingVersionCksUrl(String repoServer, String bucketName, String repoObjKey, String versionChecksum){
    return replaceUrl(getObjMetaUsingVersionCksUrl,getObjectMapWithVersionCks(repoServer, bucketName, repoObjKey, versionChecksum));
  }

  public String getObjectVersionsUrl(String repoServer, String bucketName, String repoObjKey){
    return replaceUrl(getObjVersionsUrl, getObjectBasicMap(repoServer, bucketName, repoObjKey));
  }

  public String getDeleteObjectVersionCksUrl(String repoServer, String bucketName, String repoObjKey, String versionChecksum){
    return replaceUrl(deleteObjUsingVersionCksUrl, getObjectMapWithVersionCks(repoServer, bucketName, repoObjKey, versionChecksum));
  }

  public String getDeleteObjectVersionNumUrl(String repoServer, String bucketName, String repoObjKey, int versionNumber){
    return replaceUrl(deleteObjUsingVersionNumUrl, getObjectMapWithVersionNum(repoServer, bucketName, repoObjKey, versionNumber));
  }

  public String getCreateObjectUrl(String repoServer){
    return replaceUrl(createObjUrl, getUrlBasicMap(repoServer));
  }

  public String getGetObjMetaUsingTagUrl(String repoServer, String bucketName, String key, String tag) {
    return replaceUrl(getObjMetaUsingTagUrl, getObjectMapWithTag(repoServer, bucketName, key, tag));
  }

  public String getGetObjectsUrl(String repoServer, String bucketName, int offset, int limit, boolean includeDeleted) {
    return replaceUrl(getObjectsUrl, getContentInBucketMap(repoServer, bucketName, offset, limit, includeDeleted));
  }

  public String getGetObjectsUrl(String repoServer, String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    return replaceUrl(getObjectsUsingTagUrl, getContentInBucketMap(repoServer, bucketName, offset, limit, includeDeleted, tag));
  }

  private Map<String, String> getObjectBasicMap(String repoServer, String bucketName, String repoObjKey){
    Map<String, String> values = getBucketBasicMap(repoServer, bucketName);
    values.put("objectKey", repoObjKey);
    return values;
  }

  private Map<String, String> getObjectMapWithVersionCks(String repoServer, String bucketName, String repoObjKey, String versionChecksum){
    Map<String, String> values =getObjectBasicMap(repoServer, bucketName, repoObjKey);
    values.put("versionChecksum", versionChecksum);
    return values;
  }

  private Map<String, String> getObjectMapWithTag(String repoServer, String bucketName, String collKey, String tag){
    Map<String, String> values = getObjectBasicMap(repoServer, bucketName, collKey);
    values.put("tag", String.valueOf(tag));
    return values;
  }

  private Map<String, String> getObjectMapWithVersionNum(String repoServer, String bucketName, String repoObjKey, int versionNumber){
    Map<String, String> values = getObjectBasicMap(repoServer, bucketName, repoObjKey);
    values.put("versionNumber", String.valueOf(versionNumber));
    return values;
  }

}
