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

import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.BaseDaoTest;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.BucketUrlGenerator;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, BucketUrlGenerator.class})
public class ContentRepoBucketDaoImplTest extends BaseDaoTest{

  private ContentRepoBucketDaoImpl contentRepoBucketDaoImpl;

  @Mock
  private ContentRepoAccessConfig repoAccessConfig;

  @Before
  public void setUp(){
    contentRepoBucketDaoImpl = new ContentRepoBucketDaoImpl(repoAccessConfig);
    when(repoAccessConfig.getBucketName()).thenReturn(BUCKET_NAME);
    when(repoAccessConfig.getRepoServer()).thenReturn(REPO_SERVER);
    PowerMockito.mockStatic(BucketUrlGenerator.class);
  }

  @Test
  public void createBucketTest() throws IOException {
    Mockito.when(BucketUrlGenerator.getCreateBucketUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpPost> httpPostArgument = ArgumentCaptor.forClass(HttpPost.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_CREATED);

    HttpResponse response = contentRepoBucketDaoImpl.createBucket(BUCKET_NAME);

    verifyCommonCalls(repoAccessConfig, httpPostArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpPost httpPost = httpPostArgument.getValue();
    String params = EntityUtils.toString(httpPost.getEntity(), CharEncoding.UTF_8);
    assertTrue(params.contains("name=" + BUCKET_NAME));

  }

  @Test
  public void createBucketThrowsExcTest() throws IOException {
    when(BucketUrlGenerator.getCreateBucketUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpPost> httpPostArgument = ArgumentCaptor.forClass(HttpPost.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoBucketDaoImpl.createBucket(BUCKET_NAME);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorCreatingBucket);
    }

    verifyCommonCalls(repoAccessConfig, httpPostArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    HttpPost httpPost = httpPostArgument.getValue();
    String params = EntityUtils.toString(httpPost.getEntity(), CharEncoding.UTF_8);
    assertTrue(params.contains("name=" + BUCKET_NAME));

  }

  @Test
  public void getBucketsTest() throws IOException {

    when(BucketUrlGenerator.getBucketsUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoBucketDaoImpl.getBuckets();

    assertNotNull(response);
    assertEquals(mockResponse, response);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getBucketsThrowsExcTest() throws IOException {

    when(BucketUrlGenerator.getBucketsUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoBucketDaoImpl.getBuckets();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingBucketMeta);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }


  @Test
  public void getBucketTest() throws IOException {

    when(BucketUrlGenerator.getBucketUrl(REPO_SERVER, BUCKET_NAME)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoBucketDaoImpl.getBucket(BUCKET_NAME);

    assertNotNull(response);
    assertEquals(mockResponse, response);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getBucketThrowsExcTest() throws IOException {

    when(BucketUrlGenerator.getBucketUrl(REPO_SERVER, BUCKET_NAME)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoBucketDaoImpl.getBucket(BUCKET_NAME);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingBucketMeta);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

}
