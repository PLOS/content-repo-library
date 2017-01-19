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
