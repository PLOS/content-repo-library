package org.plos.crepo.util;

import java.util.Map;

import static org.plos.crepo.util.BaseUrlGenerator.*;

/**
 * Generates the content repo urls for objects services.
 */
public class ObjectUrlGenerator {

  private static final String CREATE_OBJECT_URL = "${repoServer}/objects";
  private static final String LATEST_OBJECT_URL = "${repoServer}/objects/${bucketName}?key=${objectKey}";
  private static final String OBJECT_USING_VERSION_NUM_URL = "${repoServer}/objects/${bucketName}?key=${objectKey}&version=${versionNumber}";
  private static final String OBJECT_USING_VERSION_CKS_URL = "${repoServer}/objects/${bucketName}?key=${objectKey}&versionChecksum=${versionChecksum}";
  private static final String GET_LATEST_OBJECT_META_URL = "${repoServer}/objects/meta/${bucketName}?key=${objectKey}";
  private static final String GET_OBJECT_META_USING_VERSION_NUM_URL = "${repoServer}/objects/meta/${bucketName}?key=${objectKey}&version=${versionNumber}";
  private static final String GET_OBJECT_META_USING_VERSIONS_CKS = "${repoServer}/objects/meta/${bucketName}?key=${objectKey}&versionChecksum=${versionChecksum}";
  private static final String GET_OBJECT_META_USING_TAG_URL = "${repoServer}/objects/meta/${bucketName}?key=${objectKey}&tag=${tag}";
  private static final String GET_OBJECT_VERSIONS_URL = "${repoServer}/objects/versions/${bucketName}?key=${objectKey}";
  private static final String GET_OBJECTS_URL = "${repoServer}/objects?bucketName=${bucketName}&offset=${offset}&limit=${limit}&includeDeleted=${includeDeleted}";
  private static final String GET_OBJECTS_USING_TAG_URL = GET_OBJECTS_URL + "&tag=${tag}";

  public static String getLatestObjectUrl(String repoServer, String bucketName, String repoObjKey) {
    return replaceUrl(LATEST_OBJECT_URL, getObjectBasicMap(repoServer, bucketName, repoObjKey));
  }

  public static String getObjectUsingVersionNumUrl(String repoServer, String bucketName, String repoObjKey, int versionNumber) {
    return replaceUrl(OBJECT_USING_VERSION_NUM_URL, getObjectMapWithVersionNum(repoServer, bucketName, repoObjKey, versionNumber));
  }
/*
  public static String getDeleteObjectVersionNumUrl(String repoServer, String bucketName, String repoObjKey, int versionNumber) {
    return replaceUrl(OBJECT_USING_VERSION_NUM_URL, getObjectMapWithVersionNum(repoServer, bucketName, repoObjKey, versionNumber));
  }*/

  public static String getObjectUsingVersionCksUrl(String repoServer, String bucketName, String repoObjKey, String versionChecksum) {
    return replaceUrl(OBJECT_USING_VERSION_CKS_URL, getObjectMapWithVersionCks(repoServer, bucketName, repoObjKey, versionChecksum));
  }

/*  public static String getDeleteObjectVersionCksUrl(String repoServer, String bucketName, String repoObjKey, String versionChecksum) {
    return replaceUrl(OBJECT_USING_VERSION_CKS_URL, getObjectMapWithVersionCks(repoServer, bucketName, repoObjKey, versionChecksum));
  }*/

  public static String getLatestObjectMetaUrl(String repoServer, String bucketName, String repoObjKey) {
    return replaceUrl(GET_LATEST_OBJECT_META_URL, getObjectBasicMap(repoServer, bucketName, repoObjKey));
  }

  public static String getObjectMetaUsingVersionNumUrl(String repoServer, String bucketName, String repoObjKey, Integer versionNumber) {
    return replaceUrl(GET_OBJECT_META_USING_VERSION_NUM_URL, getObjectMapWithVersionNum(repoServer, bucketName, repoObjKey, versionNumber));
  }

  public static String getObjectMetaUsingVersionCksUrl(String repoServer, String bucketName, String repoObjKey, String versionChecksum) {
    return replaceUrl(GET_OBJECT_META_USING_VERSIONS_CKS, getObjectMapWithVersionCks(repoServer, bucketName, repoObjKey, versionChecksum));
  }

  public static String getObjectVersionsUrl(String repoServer, String bucketName, String repoObjKey) {
    return replaceUrl(GET_OBJECT_VERSIONS_URL, getObjectBasicMap(repoServer, bucketName, repoObjKey));
  }

  public static String getCreateObjectUrl(String repoServer) {
    return replaceUrl(CREATE_OBJECT_URL, getUrlBasicMap(repoServer));
  }

  public static String getGetObjMetaUsingTagUrl(String repoServer, String bucketName, String key, String tag) {
    return replaceUrl(GET_OBJECT_META_USING_TAG_URL, getObjectMapWithTag(repoServer, bucketName, key, tag));
  }

  public static String getGetObjectsUrl(String repoServer, String bucketName, int offset, int limit, boolean includeDeleted) {
    return replaceUrl(GET_OBJECTS_URL, getContentInBucketMap(repoServer, bucketName, offset, limit, includeDeleted));
  }

  public static String getGetObjectsUrl(String repoServer, String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    return replaceUrl(GET_OBJECTS_USING_TAG_URL, getContentInBucketMap(repoServer, bucketName, offset, limit, includeDeleted, tag));
  }

  private static Map<String, String> getObjectBasicMap(String repoServer, String bucketName, String repoObjKey) {
    Map<String, String> values = getBucketBasicMap(repoServer, bucketName);
    values.put("objectKey", repoObjKey);
    return values;
  }

  private static Map<String, String> getObjectMapWithVersionCks(String repoServer, String bucketName, String repoObjKey, String versionChecksum) {
    Map<String, String> values = getObjectBasicMap(repoServer, bucketName, repoObjKey);
    values.put("versionChecksum", versionChecksum);
    return values;
  }

  private static Map<String, String> getObjectMapWithTag(String repoServer, String bucketName, String collKey, String tag) {
    Map<String, String> values = getObjectBasicMap(repoServer, bucketName, collKey);
    values.put("tag", String.valueOf(tag));
    return values;
  }

  private static Map<String, String> getObjectMapWithVersionNum(String repoServer, String bucketName, String repoObjKey, int versionNumber) {
    Map<String, String> values = getObjectBasicMap(repoServer, bucketName, repoObjKey);
    values.put("versionNumber", String.valueOf(versionNumber));
    return values;
  }

}
