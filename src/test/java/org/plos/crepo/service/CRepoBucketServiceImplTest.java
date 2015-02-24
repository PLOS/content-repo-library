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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.eq;
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
    gson = PowerMockito.mock(Gson.class);
    cRepoBucketServiceImpl = new TestContentRepoServiceBuilder()
        .setGson(gson)
        .setBucketsDao(contentRepoBucketsDao)
        .build();
    Whitebox.setInternalState(cRepoBucketServiceImpl, "gson", gson);
  }

  @Test
  public void getBucketTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoBucketsDao.getBucket(KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String, Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> bucketResponse = cRepoBucketServiceImpl.getBucket(KEY);

    verify(contentRepoBucketsDao).getBucket(KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(bucketResponse);
    assertEquals(expectedResponse, bucketResponse);
  }

  @Test
  public void getBucketThrowExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoBucketsDao.getBucket(KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String, Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();
    Map<String, Object> bucketResponse = null;

    try {
      bucketResponse = cRepoBucketServiceImpl.getBucket(KEY);
      fail(FAIL_MSG);
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoBucketsDao).getBucket(KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(bucketResponse);

  }

  @Test
  public void getBucketsTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoBucketsDao.getBuckets()).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String, Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> bucketResponse = cRepoBucketServiceImpl.getBuckets();

    verify(contentRepoBucketsDao).getBuckets();
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(bucketResponse);
    assertEquals(expectedResponse, bucketResponse);

  }

  @Test
  public void getBucketsThrowExpTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoBucketsDao.getBuckets()).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String, Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    List<Map<String, Object>> bucketResponse = null;

    try {
      bucketResponse = cRepoBucketServiceImpl.getBuckets();
      fail(FAIL_MSG);
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoBucketsDao).getBuckets();
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(bucketResponse);

  }

  @Test
  public void createBucketTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoBucketsDao.createBucket(KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String, Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> bucketResponse = cRepoBucketServiceImpl.createBucket(KEY);

    verify(contentRepoBucketsDao).createBucket(KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(bucketResponse);
    assertEquals(expectedResponse, bucketResponse);
  }

  @Test
  public void createBucketThrowsExpTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoBucketsDao.createBucket(KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String, Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> bucketResponse = null;

    try {
      bucketResponse = cRepoBucketServiceImpl.createBucket(KEY);
      fail(FAIL_MSG);
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoBucketsDao).createBucket(KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(bucketResponse);
  }

}
