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

package org.plos.crepo.service;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.plos.crepo.dao.buckets.ContentRepoBucketsDao;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, Gson.class})
public class CRepoBucketServiceImplTest extends BaseServiceTest {

  private ContentRepoService cRepoBucketServiceImpl;

  @Mock
  private ContentRepoBucketsDao contentRepoBucketsDao;

  @Before
  public void setUp() {
    cRepoBucketServiceImpl = new TestContentRepoServiceBuilder()
        .setGson(gson)
        .setBucketsDao(contentRepoBucketsDao)
        .build();
    Whitebox.setInternalState(cRepoBucketServiceImpl, "gson", gson);
  }

  @Test
  public void getBucketTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(TEST_METADATA);
    when(contentRepoBucketsDao.getBucket(KEY)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> bucketResponse = cRepoBucketServiceImpl.getBucket(KEY);

    verify(contentRepoBucketsDao).getBucket(KEY);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(bucketResponse);
    assertEquals(expectedResponse, bucketResponse);
  }

  @Test
  public void getBucketThrowExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoBucketsDao.getBucket(KEY)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();
    Map<String, Object> bucketResponse = null;

    try {
      bucketResponse = cRepoBucketServiceImpl.getBucket(KEY);
      fail(FAIL_MSG);
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoBucketsDao).getBucket(KEY);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(bucketResponse);
  }

  @Test
  public void getBucketsTest() throws IOException {
    List<Map<String, Object>> expectedResponse = TEST_METADATA_LIST;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoBucketsDao.getBuckets()).thenReturn(httpResponse);

    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> bucketResponse = cRepoBucketServiceImpl.getBuckets();

    verify(contentRepoBucketsDao).getBuckets();
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(bucketResponse);
    assertEquals(expectedResponse, bucketResponse);

  }

  @Test
  public void getBucketsThrowExpTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoBucketsDao.getBuckets()).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    List<Map<String, Object>> bucketResponse = null;

    try {
      bucketResponse = cRepoBucketServiceImpl.getBuckets();
      fail(FAIL_MSG);
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoBucketsDao).getBuckets();
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(bucketResponse);
  }

  @Test
  public void createBucketTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(TEST_METADATA);
    when(contentRepoBucketsDao.createBucket(KEY)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> bucketResponse = cRepoBucketServiceImpl.createBucket(KEY);

    verify(contentRepoBucketsDao).createBucket(KEY);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(bucketResponse);
    assertEquals(expectedResponse, bucketResponse);
  }

  @Test
  public void createBucketThrowsExpTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoBucketsDao.createBucket(KEY)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> bucketResponse = null;

    try {
      bucketResponse = cRepoBucketServiceImpl.createBucket(KEY);
      fail(FAIL_MSG);
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoBucketsDao).createBucket(KEY);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(bucketResponse);
  }

}
