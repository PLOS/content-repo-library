package org.plos.crepo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BucketUrlGenerator extends BaseUrlGenerator{

  @Value("${crepo.url.createBucket}")
  private String createBucket;

  @Value("${crepo.url.createBucket}")
  private String getBucketsUrl;

  @Value("${crepo.url.getBucket}")
  private String getBucketUrl;

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
