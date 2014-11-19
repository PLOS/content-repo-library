package org.plos.crepo.dao.buckets;


import org.apache.http.client.methods.CloseableHttpResponse;

public interface ContentRepoBucketsDao {

  CloseableHttpResponse createBucket(String bucketName);

  CloseableHttpResponse getBuckets();

  CloseableHttpResponse getBucket(String bucketName);

}
