/*
 * Copyright 2017 Public Library of Science
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

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
  private static final String OBJECT_USING_VERSION_UUID_URL = "${repoServer}/objects/${bucketName}?key=${objectKey}&uuid=${uuid}";
  private static final String GET_LATEST_OBJECT_META_URL = "${repoServer}/objects/meta/${bucketName}?key=${objectKey}";
  private static final String GET_OBJECT_META_USING_VERSION_NUM_URL = "${repoServer}/objects/meta/${bucketName}?key=${objectKey}&version=${versionNumber}";
  private static final String GET_OBJECT_META_USING_VERSIONS_CKS = "${repoServer}/objects/meta/${bucketName}?key=${objectKey}&uuid=${uuid}";
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

  public static String getObjectUsingUuidUrl(String repoServer, String bucketName, String repoObjKey, String uuid) {
    return replaceUrl(OBJECT_USING_VERSION_UUID_URL, getObjectMapWithUuid(repoServer, bucketName, repoObjKey, uuid));
  }

/*  public static String getDeleteObjectUuidUrl(String repoServer, String bucketName, String repoObjKey, String uuid) {
    return replaceUrl(OBJECT_USING_VERSION_UUID_URL, getObjectMapWithUuid(repoServer, bucketName, repoObjKey, uuid));
  }*/

  public static String getLatestObjectMetaUrl(String repoServer, String bucketName, String repoObjKey) {
    return replaceUrl(GET_LATEST_OBJECT_META_URL, getObjectBasicMap(repoServer, bucketName, repoObjKey));
  }

  public static String getObjectMetaUsingVersionNumUrl(String repoServer, String bucketName, String repoObjKey, Integer versionNumber) {
    return replaceUrl(GET_OBJECT_META_USING_VERSION_NUM_URL, getObjectMapWithVersionNum(repoServer, bucketName, repoObjKey, versionNumber));
  }

  public static String getObjectMetaUsingUuidUrl(String repoServer, String bucketName, String repoObjKey, String uuid) {
    return replaceUrl(GET_OBJECT_META_USING_VERSIONS_CKS, getObjectMapWithUuid(repoServer, bucketName, repoObjKey, uuid));
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

  private static Map<String, String> getObjectMapWithUuid(String repoServer, String bucketName, String repoObjKey, String uuid) {
    Map<String, String> values = getObjectBasicMap(repoServer, bucketName, repoObjKey);
    values.put("uuid", uuid);
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
