package org.plos.crepo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Generates the content repo urls for buckets services.
 */
@Component
public class BucketUrlGenerator extends BaseUrlGenerator{

  private static final String createBucket = "${repoServer}/buckets";
  private static final String getBucketsUrl = "${repoServer}/buckets";
  private static final String getBucketUrl = "${repoServer}/buckets/${bucketName}";

  public String getCreateBucketUrl(String repoServer){
    return replaceUrl(createBucket, getUrlBasicMap(repoServer));
  }

  public String getBucketsUrl(String repoServer){
    return replaceUrl(getBucketsUrl, getUrlBasicMap(repoServer));
  }

  public String getBucketUrl(String repoServer, String key){
    return replaceUrl(getBucketUrl, getBucketBasicMap(repoServer, key));
  }

}
