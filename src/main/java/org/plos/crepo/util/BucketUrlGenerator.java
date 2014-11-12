package org.plos.crepo.util;

import static org.plos.crepo.util.BaseUrlGenerator.getBucketBasicMap;
import static org.plos.crepo.util.BaseUrlGenerator.getUrlBasicMap;
import static org.plos.crepo.util.BaseUrlGenerator.replaceUrl;

/**
 * Generates the content repo urls for buckets services.
 */
public class BucketUrlGenerator {

  private static final String createBucket = "${repoServer}/buckets";
  private static final String getBucketsUrl = "${repoServer}/buckets";
  private static final String getBucketUrl = "${repoServer}/buckets/${bucketName}";

  public static String getCreateBucketUrl(String repoServer) {
    return replaceUrl(createBucket, getUrlBasicMap(repoServer));
  }

  public static String getBucketsUrl(String repoServer) {
    return replaceUrl(getBucketsUrl, getUrlBasicMap(repoServer));
  }

  public static String getBucketUrl(String repoServer, String key) {
    return replaceUrl(getBucketUrl, getBucketBasicMap(repoServer, key));
  }

}
