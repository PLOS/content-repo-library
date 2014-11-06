package org.plos.crepo.service.config.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.plos.crepo.dao.config.ContentRepoConfigDao;
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
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, Gson.class})
public class CRepoConfigServiceImplTest extends BaseServiceTest {

  @InjectMocks
  private CRepoConfigServiceImpl cRepoConfigServiceImpl;

  @Mock
  private ContentRepoConfigDao contentRepoConfigDao;

  @Before
  public void setUp(){
    cRepoConfigServiceImpl = new CRepoConfigServiceImpl();
    gson = PowerMockito.mock(Gson.class);
    initMocks(this);
  }

  @Test
  public void hasXReproxyTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoConfigDao.hasReProxy()).thenReturn(httpResponse);
    PowerMockito.mockStatic(HttpResponseUtil.class);
    Mockito.when(HttpResponseUtil.getResponseAsString(httpResponse)).thenReturn("true");

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Boolean hasXReproxy = cRepoConfigServiceImpl.hasXReproxy();

    verify(contentRepoConfigDao).hasReProxy();
    PowerMockito.verifyStatic();

    assertTrue(hasXReproxy);
  }

  @Test
  public void getRepoConfigTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoConfigDao.getRepoConfig()).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> configResponse = cRepoConfigServiceImpl.getRepoConfig();

    verify(contentRepoConfigDao).getRepoConfig();
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(configResponse);
    assertEquals(expectedResponse, configResponse);
  }

  @Test
  public void getRepoStatusTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoConfigDao.getRepoStatus()).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> statusResponse = cRepoConfigServiceImpl.getRepoStatus();

    verify(contentRepoConfigDao).getRepoStatus();
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(statusResponse);
    assertEquals(expectedResponse, statusResponse);
  }


}
