package org.plos.crepo.service.buckets.impl;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.plos.crepo.dao.buckets.ContentRepoBucketsDao;
import org.plos.crepo.service.BaseServiceTest;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, Gson.class})
public class CRepoBucketServiceImplTest extends BaseServiceTest {

  @InjectMocks
  private CRepoBucketServiceImpl cRepoBucketServiceImpl;

  @Mock
  private ContentRepoBucketsDao contentRepoBucketsDao;

  @Before
  public void setUp(){
    cRepoBucketServiceImpl = new CRepoBucketServiceImpl();
    gson = PowerMockito.mock(Gson.class);
    initMocks(this);
  }

  @Test
  public void getBucketTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoBucketsDao.getBucket(KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> bucketResponse = cRepoBucketServiceImpl.getBucket(KEY);

    verify(contentRepoBucketsDao).getBucket(KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(bucketResponse);
    assertEquals(expectedResponse, bucketResponse);
  }

  @Test
  public void getBucketsTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoBucketsDao.getBuckets()).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    List<Map<String, Object>> bucketResponse = cRepoBucketServiceImpl.getBuckets();

    verify(contentRepoBucketsDao).getBuckets();
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(bucketResponse);
    assertEquals(expectedResponse, bucketResponse);
  }

  @Test
  public void createBucketTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoBucketsDao.createBucket(KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> bucketResponse = cRepoBucketServiceImpl.createBucket(KEY);

    verify(contentRepoBucketsDao).createBucket(KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(bucketResponse);
    assertEquals(expectedResponse, bucketResponse);
  }

}
