package org.plos.crepo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Generates the content repo urls for objects services.
 */
@Component
public class ObjectUrlGenerator extends BaseUrlGenerator{

  private static final String getLatestObjUrl = "${repoServer}/objects/${bucketName}?key=${objectKey}";
  private static final String getObjUsingVersionNumUrl = "${repoServer}/objects/${bucketName}?key=${objectKey}&version=${versionNumber}";
  private static final String getObjVersionCksUrl = "${repoServer}/objects/${bucketName}?key=${objectKey}&versionChecksum=${versionChecksum}";
  private static final String getLatestObjMetaUrl = "${repoServer}/objects/meta/${bucketName}?key=${objectKey}";
  private static final String getObjMetaUsingVersionNumUrl = "${repoServer}/objects/meta/${bucketName}?key=${objectKey}&version=${versionNumber}";
  private static final String getObjMetaUsingVersionCksUrl = "${repoServer}/objects/meta/${bucketName}?key=${objectKey}&versionChecksum=${versionChecksum}";
  private static final String getObjMetaUsingTagUrl = "${repoServer}/objects/meta/${bucketName}?key=${objectKey}&tag=${tag}";
  private static final String getObjVersionsUrl = "${repoServer}/objects/versions/${bucketName}?key=${objectKey}";
  private static final String deleteObjUsingVersionCksUrl = "${repoServer}/objects/${bucketName}?key=${objectKey}&versionChecksum=${versionChecksum}";
  private static final String deleteObjUsingVersionNumUrl = "${repoServer}/objects/${bucketName}?key=${objectKey}&version=${versionNumber}";
  private static final String createObjUrl = "${repoServer}/objects";
  private static final String getObjectsUrl = "${repoServer}/objects?bucketName=${bucketName}&offset=${offset}&limit=${limit}&includeDeleted=${includeDeleted}";
  private static final String getObjectsUsingTagUrl = "${crepo.url.getObjectsUrl}&tag=${tag}";

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
