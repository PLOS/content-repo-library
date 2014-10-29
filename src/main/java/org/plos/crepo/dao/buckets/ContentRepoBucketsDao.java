package org.plos.crepo.dao.buckets;


import org.apache.http.HttpResponse;
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoObject;

public interface ContentRepoBucketsDao {

  HttpResponse createBucket(String bucketName);

  HttpResponse getBuckets();

  HttpResponse getBucket(String bucketName);

}
