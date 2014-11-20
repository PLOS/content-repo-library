package org.plos.crepo.dao.buckets.impl;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.ContentRepoBaseDao;
import org.plos.crepo.dao.buckets.ContentRepoBucketsDao;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.BucketUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ContentRepoBucketDaoImpl extends ContentRepoBaseDao implements ContentRepoBucketsDao {

  private static final Logger log = LoggerFactory.getLogger(ContentRepoBucketDaoImpl.class);

  public ContentRepoBucketDaoImpl(ContentRepoAccessConfig accessConfig) {
    super(accessConfig);
  }

  @Override
  public CloseableHttpResponse createBucket(String bucketName) {
    HttpPost request = new HttpPost(BucketUrlGenerator.getCreateBucketUrl(getRepoServer()));

    List<NameValuePair> params = new ArrayList<>();
    params.add(new BasicNameValuePair("name", bucketName));
    request.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));

    return executeRequest(request, ErrorType.ErrorCreatingBucket);
  }

  @Override
  public CloseableHttpResponse getBuckets() {
    HttpGet request = new HttpGet(BucketUrlGenerator.getBucketsUrl(getRepoServer()));
    return executeRequest(request, ErrorType.ErrorFetchingBucketMeta);
  }

  @Override
  public CloseableHttpResponse getBucket(String bucketName) {
    HttpGet request = new HttpGet(BucketUrlGenerator.getBucketUrl(getRepoServer(), bucketName));
    return executeRequest(request, ErrorType.ErrorFetchingBucketMeta);
  }

  @Override
  public Logger getLog() {
    return log;
  }
}
