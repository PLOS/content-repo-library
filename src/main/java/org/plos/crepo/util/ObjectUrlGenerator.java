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

  @Value("${crepo.url.getObjVersionsUrl}")
  private String getObjVersionsUrl;

  @Value("${crepo.url.deleteObjUsingVersionCksUrl}")
  private String deleteObjUsingVersionCksUrl;

  @Value("${crepo.url.deleteObjUsingVersionNumUrl}")
  private String deleteObjUsingVersionNumUrl;

  @Value("${crepo.url.createObjUrl}")
  private String createObjUrl;

  public String getLatestObjectUrl(String repoServer, String bucketName, String assetKey){
    return replaceUrl(getLatestObjUrl, getObjectBasicMap(repoServer, bucketName, assetKey));
  }

  public String getObjectUsingVersionNumUrl(String repoServer, String bucketName, String assetKey, int versionNumber){
    return replaceUrl(getObjUsingVersionNumUrl, getObjectMapWithVersionNum(repoServer, bucketName, assetKey, versionNumber));
  }

  public String getObjectUsingVersionCksUrl(String repoServer, String bucketName, String assetKey, String versionChecksum){
    return replaceUrl(getObjVersionCksUrl,getObjectMapWithVersionCks(repoServer, bucketName, assetKey, versionChecksum));
  }

  public String getLatestObjectMetaUrl(String repoServer, String bucketName, String assetKey){
    return replaceUrl(getLatestObjMetaUrl, getObjectBasicMap(repoServer, bucketName, assetKey));
  }

  public String getObjectMetaUsingVersionNumUrl(String repoServer, String bucketName, String assetKey, Integer versionNumber){
    return replaceUrl(getObjMetaUsingVersionNumUrl, getObjectMapWithVersionNum(repoServer, bucketName, assetKey, versionNumber));
  }

  public String getObjectMetaUsingVersionCksUrl(String repoServer, String bucketName, String assetKey, String versionChecksum){
    return replaceUrl(getObjMetaUsingVersionCksUrl,getObjectMapWithVersionCks(repoServer, bucketName, assetKey, versionChecksum));
  }

  public String getObjectVersionsUrl(String repoServer, String bucketName, String assetKey){
    return replaceUrl(getObjVersionsUrl, getObjectBasicMap(repoServer, bucketName, assetKey));
  }

  public String getDeleteObjectVersionCksUrl(String repoServer, String bucketName, String assetKey, String versionChecksum){
    return replaceUrl(deleteObjUsingVersionCksUrl, getObjectMapWithVersionCks(repoServer, bucketName, assetKey, versionChecksum));
  }

  public String getDeleteObjectVersionNumUrl(String repoServer, String bucketName, String assetKey, int versionNumber){
    return replaceUrl(deleteObjUsingVersionNumUrl, getObjectMapWithVersionNum(repoServer, bucketName, assetKey, versionNumber));
  }

  public String getCreateObjectUrl(String repoServer){
    return replaceUrl(createObjUrl, getUrlBasicMap(repoServer));
  }

  private Map<String, String> getObjectBasicMap(String repoServer, String bucketName, String assetKey){
    Map<String, String> values = getBucketBasicMap(repoServer, bucketName);
    values.put("objectKey", assetKey);
    return values;
  }

  private Map<String, String> getObjectMapWithVersionCks(String repoServer, String bucketName, String assetKey, String versionChecksum){
    Map<String, String> values =getObjectBasicMap(repoServer, bucketName, assetKey);
    values.put("versionChecksum", versionChecksum);
    return values;
  }


  private Map<String, String> getObjectMapWithVersionNum(String repoServer, String bucketName, String assetKey, int versionNumber){
    Map<String, String> values = getObjectBasicMap(repoServer, bucketName, assetKey);
    values.put("versionNumber", String.valueOf(versionNumber));
    return values;
  }

}
