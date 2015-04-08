package org.plos.crepo.util;

import java.util.Map;

import static org.plos.crepo.util.BaseUrlGenerator.*;

/**
 * Generates the content repo urls for collections services.
 */
public class CollectionUrlGenerator {

  private static final String CREATE_COLLECTION_URL = "${repoServer}/collections";
  private static final String COLLECTION_URL = "${repoServer}/collections/${bucketName}?key=${collectionKey}";
  private static final String COLLECTION_USING_VERSION_NUM_URL = COLLECTION_URL + "&version=${versionNumber}";
  private static final String COLLECTION_USING_VERSION_CKS_URL = COLLECTION_URL + "&uuid=${uuid}";
  private static final String COLLECTION_USING_TAG_URL = COLLECTION_URL +  "&tag=${tag}";
  private static final String COLLECTION_VERSIONS_URL = "${repoServer}/collections/versions/${bucketName}?key=${collectionKey}";
  private static final String COLLECTIONS_URL = "${repoServer}/collections?bucketName=${bucketName}&offset=${offset}&limit=${limit}&includeDeleted=${includeDeleted}";
  private static final String COLLECTIONS_USING_TAG_URL = COLLECTIONS_URL + "&tag=${tag}";

  public static String getCollectionVersionNumUrl(String repoServer, String bucketName, String collKey, int versionNumber) {
    return replaceUrl(COLLECTION_USING_VERSION_NUM_URL, getCollectionMapWithVersionNumber(repoServer, bucketName, collKey, versionNumber));
  }

  public static String getCollectionTagUrl(String repoServer, String bucketName, String collKey, String tag) {
    return replaceUrl(COLLECTION_USING_TAG_URL, getCollectionMapWithTag(repoServer, bucketName, collKey, tag));
  }

  public static String getCollectionUuidUrl(String repoServer, String bucketName, String collKey, String uuid) {
    return replaceUrl(COLLECTION_USING_VERSION_CKS_URL, getCollectionMapWithUuid(repoServer, bucketName, collKey, uuid));
  }

  public static String getCollectionVersionsUrl(String repoServer, String bucketName, String collKey) {
    return replaceUrl(COLLECTION_VERSIONS_URL, getCollectionBasicMap(repoServer, bucketName, collKey));
  }


  public static String getCollectionsUsingTagUrl(String repoServer, String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    return replaceUrl(COLLECTIONS_USING_TAG_URL, getContentInBucketMap(repoServer, bucketName, offset, limit, includeDeleted, tag));
  }

  public static String getLatestCollectionUrl(String repoServer, String bucketName, String key) {
    return replaceUrl(COLLECTIONS_URL, getCollectionBasicMap(repoServer, bucketName, key));
  }

  public static String getGetCollectionsUrl(String repoServer, String bucketName, int offset, int limit, boolean includeDelete) {
    return replaceUrl(COLLECTIONS_URL, getContentInBucketMap(repoServer, bucketName, offset, limit, includeDelete));
  }

  public static String getCreateCollUrl(String repoServer) {
    return replaceUrl(CREATE_COLLECTION_URL, getUrlBasicMap(repoServer));
  }

  private static Map<String, String> getCollectionBasicMap(String repoServer, String bucketName, String collKey) {
    Map<String, String> values = getBucketBasicMap(repoServer, bucketName);
    values.put("collectionKey", collKey);
    return values;
  }

  private static Map<String, String> getCollectionMapWithUuid(String repoServer, String bucketName, String collKey, String uuid) {
    Map<String, String> values = getCollectionBasicMap(repoServer, bucketName, collKey);
    values.put("uuid", uuid);
    return values;
  }

  private static Map<String, String> getCollectionMapWithVersionNumber(String repoServer, String bucketName, String collKey, int versionNumber) {
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
