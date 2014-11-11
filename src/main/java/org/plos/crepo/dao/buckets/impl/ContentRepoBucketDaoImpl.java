package org.plos.crepo.dao.buckets.impl;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.plos.crepo.dao.ContentRepoBaseDao;
import org.plos.crepo.dao.buckets.ContentRepoBucketsDao;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.BucketUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ContentRepoBucketDaoImpl extends ContentRepoBaseDao implements ContentRepoBucketsDao {

  private static final Logger log = LoggerFactory.getLogger(ContentRepoBucketDaoImpl.class);

  @Autowired
  private BucketUrlGenerator bucketUrlGenerator;

  @Override
  public HttpResponse createBucket(String bucketName) {
    HttpPost request = new HttpPost(bucketUrlGenerator.getCreateBucketUrl(getRepoServer()));

    List<NameValuePair> params = new ArrayList<>();
    params.add(new BasicNameValuePair("name", bucketName));
    request.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));

    return executeRequest(request, ErrorType.ErrorCreatingBucket);
  }

  @Override
  public HttpResponse getBuckets() {
    HttpGet request = new HttpGet(bucketUrlGenerator.getBucketsUrl(getRepoServer()));
    return executeRequest(request, ErrorType.ErrorFetchingBucketMeta);
  }

  @Override
  public HttpResponse getBucket(String bucketName){
    HttpGet request = new HttpGet(bucketUrlGenerator.getBucketUrl(getRepoServer(), bucketName));
    return executeRequest(request, ErrorType.ErrorFetchingBucketMeta);
  }

  @Override
  public Logger getLog() {
    return log;
  }
}
