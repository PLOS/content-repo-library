package org.plos.crepo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CollectionUrlGenerator extends BaseUrlGenerator{

  @Value("${crepo.url.getCollUsingVersionNumUrl}")
  private String getCollUsingVersionNumUrl;

  @Value("${crepo.url.getCollUsingTagUrl}")
  private String getCollUsingTagUrl;

  @Value("${crepo.url.getCollVersionCksUrl}")
  private String getCollVersionCksUrl;

  @Value("${crepo.url.getCollVersionsUrl}")
  private String getCollVersionsUrl;

  @Value("${crepo.url.getCollectionsUsingTagUrl}")
  private String getCollectionsUsingTagUrl;

  @Value("${crepo.url.getCollectionsUrl}")
  private String getCollectionsUrl;

  @Value("${crepo.url.deleteCollUsingVersionCksUrl}")
  private String deleteCollUsingVersionCksUrl;

  @Value("${crepo.url.deleteCollUsingVersionNumUrl}")
  private String deleteCollUsingVersionNumUrl;

  @Value("${crepo.url.createCollUrl}")
  private String createCollUrl;

  public String getGetCollUsingVersionNumUrl(String repoServer, String bucketName, String collKey, int versionNumber) {
    return replaceUrl(getCollUsingVersionNumUrl, getCollectionMapWithVersionNum(repoServer, bucketName, collKey, versionNumber));
  }

  public String getGetCollUsingTagUrl(String repoServer, String bucketName, String collKey, String tag) {
    return replaceUrl(getCollUsingTagUrl, getCollectionMapWithTag(repoServer, bucketName, collKey, tag));
  }

  public String getGetCollVersionCksUrl(String repoServer, String bucketName, String collKey, String versionChecksum) {
    return replaceUrl(getCollVersionCksUrl, getCollectionMapWithVersionCks(repoServer, bucketName, collKey, versionChecksum));
  }

  public String getGetCollVersionsUrl(String repoServer, String bucketName, String collKey) {
    return replaceUrl(getCollVersionsUrl, getCollectionBasicMap(repoServer, bucketName, collKey));
  }

  public String getDeleteCollUsingVersionCksUrl(String repoServer, String bucketName, String collKey, String versionChecksum) {
    return replaceUrl(deleteCollUsingVersionCksUrl, getCollectionMapWithVersionCks(repoServer, bucketName, collKey, versionChecksum));
  }

  public String getDeleteCollUsingVersionNumUrl(String repoServer, String bucketName, String collKey, int versionNumber) {
    return replaceUrl(deleteCollUsingVersionNumUrl, getCollectionMapWithVersionNum(repoServer, bucketName, collKey, versionNumber));
  }

  public String getGetCollectionsUrl(String repoServer, String bucketName, int offset, int limit, boolean includeDeleted, String tag){
    return replaceUrl(getCollectionsUsingTagUrl, getContentInBucketMap(repoServer, bucketName, offset, limit, includeDeleted, tag));
  }

  public String getGetCollectionsUrl(String repoServer, String bucketName, int offset, int limit, boolean includeDelete){
    return replaceUrl(getCollectionsUrl, getContentInBucketMap(repoServer, bucketName, offset, limit, includeDelete));
  }

  public String getCreateCollUrl(String repoServer) {
    return replaceUrl(createCollUrl, getUrlBasicMap(repoServer));
  }

  private Map<String, String> getCollectionBasicMap(String repoServer, String bucketName, String collKey){
    Map<String, String> values = getBucketBasicMap(repoServer, bucketName);
    values.put("collectionKey", collKey);
    return values;
  }

  private Map<String, String> getCollectionMapWithVersionCks(String repoServer, String bucketName, String collKey, String versionChecksum){
    Map<String, String> values =getCollectionBasicMap(repoServer, bucketName, collKey);
    values.put("versionChecksum", versionChecksum);
    return values;
  }

  private Map<String, String> getCollectionMapWithVersionNum(String repoServer, String bucketName, String collKey, int versionNumber){
    Map<String, String> values = getCollectionBasicMap(repoServer, bucketName, collKey);
    values.put("versionNumber", String.valueOf(versionNumber));
    return values;
  }

  private Map<String, String> getCollectionMapWithTag(String repoServer, String bucketName, String collKey, String tag){
    Map<String, String> values = getCollectionBasicMap(repoServer, bucketName, collKey);
    values.put("tag", String.valueOf(tag));
    return values;
  }

}
