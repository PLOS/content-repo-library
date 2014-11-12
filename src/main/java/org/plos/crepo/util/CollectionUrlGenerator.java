package org.plos.crepo.util;

import java.util.Map;

import static org.plos.crepo.util.BaseUrlGenerator.getBucketBasicMap;
import static org.plos.crepo.util.BaseUrlGenerator.getContentInBucketMap;
import static org.plos.crepo.util.BaseUrlGenerator.getUrlBasicMap;
import static org.plos.crepo.util.BaseUrlGenerator.replaceUrl;

/**
 * Generates the content repo urls for collections services.
 */
public class CollectionUrlGenerator {

  private static final String getCollUsingVersionNumUrl = "${repoServer}/collections/${bucketName}?key=${collectionKey}&version=${versionNumber}";
  private static final String getCollUsingTagUrl = "${repoServer}/collections/${bucketName}?key=${collectionKey}&tag=${tag}";
  private static final String getCollVersionCksUrl = "${repoServer}/collections/${bucketName}?key=${collectionKey}&versionChecksum=${versionChecksum}";
  private static final String getCollVersionsUrl = "${repoServer}/collections/versions/${bucketName}?key=${collectionKey}";
  private static final String getCollectionsUsingTagUrl = "${crepo.url.getCollectionsUrl}&tag=${tag}";
  private static final String getCollectionsUrl = "${repoServer}/collections?bucketName=${bucketName}&offset=${offset}&limit=${limit}&includeDeleted=${includeDeleted}";
  private static final String deleteCollUsingVersionCksUrl = "${repoServer}/collections/${bucketName}?key=${collectionKey}&versionChecksum=${versionChecksum}";
  private static final String deleteCollUsingVersionNumUrl = "${repoServer}/collections/${bucketName}?key=${collectionKey}&version=${versionNumber}";
  private static final String createCollUrl = "${repoServer}/collections";


  public static String getGetCollUsingVersionNumUrl(String repoServer, String bucketName, String collKey, int versionNumber) {
    return replaceUrl(getCollUsingVersionNumUrl, getCollectionMapWithVersionNum(repoServer, bucketName, collKey, versionNumber));
  }

  public static String getGetCollUsingTagUrl(String repoServer, String bucketName, String collKey, String tag) {
    return replaceUrl(getCollUsingTagUrl, getCollectionMapWithTag(repoServer, bucketName, collKey, tag));
  }

  public static String getGetCollVersionCksUrl(String repoServer, String bucketName, String collKey, String versionChecksum) {
    return replaceUrl(getCollVersionCksUrl, getCollectionMapWithVersionCks(repoServer, bucketName, collKey, versionChecksum));
  }

  public static String getGetCollVersionsUrl(String repoServer, String bucketName, String collKey) {
    return replaceUrl(getCollVersionsUrl, getCollectionBasicMap(repoServer, bucketName, collKey));
  }

  public static String getDeleteCollUsingVersionCksUrl(String repoServer, String bucketName, String collKey, String versionChecksum) {
    return replaceUrl(deleteCollUsingVersionCksUrl, getCollectionMapWithVersionCks(repoServer, bucketName, collKey, versionChecksum));
  }

  public static String getDeleteCollUsingVersionNumUrl(String repoServer, String bucketName, String collKey, int versionNumber) {
    return replaceUrl(deleteCollUsingVersionNumUrl, getCollectionMapWithVersionNum(repoServer, bucketName, collKey, versionNumber));
  }

  public static String getGetCollectionsUsingTagUrl(String repoServer, String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    return replaceUrl(getCollectionsUsingTagUrl, getContentInBucketMap(repoServer, bucketName, offset, limit, includeDeleted, tag));
  }

  public static String getGetCollectionsUrl(String repoServer, String bucketName, int offset, int limit, boolean includeDelete) {
    return replaceUrl(getCollectionsUrl, getContentInBucketMap(repoServer, bucketName, offset, limit, includeDelete));
  }

  public static String getCreateCollUrl(String repoServer) {
    return replaceUrl(createCollUrl, getUrlBasicMap(repoServer));
  }

  private static Map<String, String> getCollectionBasicMap(String repoServer, String bucketName, String collKey) {
    Map<String, String> values = getBucketBasicMap(repoServer, bucketName);
    values.put("collectionKey", collKey);
    return values;
  }

  private static Map<String, String> getCollectionMapWithVersionCks(String repoServer, String bucketName, String collKey, String versionChecksum) {
    Map<String, String> values = getCollectionBasicMap(repoServer, bucketName, collKey);
    values.put("versionChecksum", versionChecksum);
    return values;
  }

  private static Map<String, String> getCollectionMapWithVersionNum(String repoServer, String bucketName, String collKey, int versionNumber) {
    Map<String, String> values = getCollectionBasicMap(repoServer, bucketName, collKey);
    values.put("versionNumber", String.valueOf(versionNumber));
    return values;
  }

  private static Map<String, String> getCollectionMapWithTag(String repoServer, String bucketName, String collKey, String tag) {
    Map<String, String> values = getCollectionBasicMap(repoServer, bucketName, collKey);
    values.put("tag", String.valueOf(tag));
    return values;
  }

}
