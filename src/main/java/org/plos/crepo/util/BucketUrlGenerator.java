package org.plos.crepo.util;

import static org.plos.crepo.util.BaseUrlGenerator.*;

/**
 * Generates the content repo urls for buckets services.
 */
public class BucketUrlGenerator {

  private static final String CREATE_BUCKET = "${repoServer}/buckets";
  private static final String BUCKETS_URL = "${repoServer}/buckets";
  private static final String BUCKET_URL = "${repoServer}/buckets/${bucketName}";

  public static String getCreateBucketUrl(String repoServer) {
    return replaceUrl(CREATE_BUCKET, getUrlBasicMap(repoServer));
  }

  public static String getBucketsUrl(String repoServer) {
    return replaceUrl(BUCKETS_URL, getUrlBasicMap(repoServer));
  }

  public static String getBucketUrl(String repoServer, String key) {
    return replaceUrl(BUCKET_URL, getBucketBasicMap(repoServer, key));
  }

}
