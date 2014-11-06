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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.plos.crepo.dao.BaseDaoTest;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.BucketUrlGenerator;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpResponseUtil.class)
public class ContentRepoBucketDaoImplTest extends BaseDaoTest{

  private static final java.lang.String SOME_URL = "http://someUrl";
  private static final String BUCKET_NAME = "bucket1";

  @Mock
  private BucketUrlGenerator bucketUrlGenerator;

  @InjectMocks
  private ContentRepoBucketDaoImpl contentRepoBucketDaoImpl;

  @Before
  public void setUp(){
    contentRepoBucketDaoImpl = new ContentRepoBucketDaoImpl();
    initMocks(this);
    Whitebox.setInternalState(contentRepoBucketDaoImpl, "repoServer", REPO_SERVER);
  }

  @Test
  public void createBucketTest() throws IOException {
    when(bucketUrlGenerator.getCreateBucketUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpPost> httpPostArgument = ArgumentCaptor.forClass(HttpPost.class);
    mockCommonCalls(HttpStatus.SC_CREATED);

    HttpResponse response = contentRepoBucketDaoImpl.createBucket(BUCKET_NAME);

    verify(bucketUrlGenerator).getCreateBucketUrl(REPO_SERVER);
    verifyCommonCalls(httpPostArgument, statusLine, 2, 2);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpPost httpPost = httpPostArgument.getValue();
    String params = EntityUtils.toString(httpPost.getEntity(), CharEncoding.UTF_8);
    assertTrue(params.contains("name=" + BUCKET_NAME));

  }

  @Test
  public void createBucketThrowsExcTest() throws IOException {
    when(bucketUrlGenerator.getCreateBucketUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpPost> httpPostArgument = ArgumentCaptor.forClass(HttpPost.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoBucketDaoImpl.createBucket(BUCKET_NAME);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorCreatingBucket);
    }

    verify(bucketUrlGenerator).getCreateBucketUrl(REPO_SERVER);
    verifyCommonCalls(httpPostArgument, statusLine, 2, 2);

    HttpPost httpPost = httpPostArgument.getValue();
    String params = EntityUtils.toString(httpPost.getEntity(), CharEncoding.UTF_8);
    assertTrue(params.contains("name=" + BUCKET_NAME));

  }

  @Test
  public void getBucketsTest() throws IOException {

    when(bucketUrlGenerator.getBucketsUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoBucketDaoImpl.getBuckets();

    assertNotNull(response);
    assertEquals(mockResponse, response);

    verify(bucketUrlGenerator).getBucketsUrl(REPO_SERVER);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

  }

  @Test
  public void getBucketsThrowsExcTest() throws IOException {

    when(bucketUrlGenerator.getBucketsUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoBucketDaoImpl.getBuckets();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingBucketMeta);
    }

    verify(bucketUrlGenerator).getBucketsUrl(REPO_SERVER);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }


  @Test
  public void getBucketTest() throws IOException {

    when(bucketUrlGenerator.getBucketUrl(REPO_SERVER, BUCKET_NAME)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoBucketDaoImpl.getBucket(BUCKET_NAME);

    assertNotNull(response);
    assertEquals(mockResponse, response);

    verify(bucketUrlGenerator).getBucketUrl(REPO_SERVER, BUCKET_NAME);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

  }

  @Test
  public void getBucketThrowsExcTest() throws IOException {

    when(bucketUrlGenerator.getBucketUrl(REPO_SERVER, BUCKET_NAME)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoBucketDaoImpl.getBucket(BUCKET_NAME);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingBucketMeta);
    }

    verify(bucketUrlGenerator).getBucketUrl(REPO_SERVER, BUCKET_NAME);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }


}
