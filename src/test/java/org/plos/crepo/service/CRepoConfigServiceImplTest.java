package org.plos.crepo.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.plos.crepo.dao.config.ContentRepoConfigDao;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.service.BaseServiceTest;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, Gson.class})
public class CRepoConfigServiceImplTest extends BaseServiceTest {

  private ContentRepoService cRepoConfigServiceImpl;

  @Mock
  private ContentRepoConfigDao contentRepoConfigDao;

  @Before
  public void setUp(){
    gson = PowerMockito.mock(Gson.class);
    cRepoConfigServiceImpl = new TestContentRepoServiceBuilder()
        .setGson(gson)
        .setConfigDao(contentRepoConfigDao)
        .build();
    Whitebox.setInternalState(cRepoConfigServiceImpl, "gson", gson);
  }

  @Test
  public void hasXReproxyTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoConfigDao.hasReProxy()).thenReturn(httpResponse);
    PowerMockito.mockStatic(HttpResponseUtil.class);
    Mockito.when(HttpResponseUtil.getResponseAsString(httpResponse)).thenReturn("true");

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    boolean hasXReproxy = cRepoConfigServiceImpl.hasXReproxy();

    verify(contentRepoConfigDao).hasReProxy();
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertTrue(hasXReproxy);
  }

  @Test
  public void hasXReproxyThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoConfigDao.hasReProxy()).thenReturn(httpResponse);
    PowerMockito.mockStatic(HttpResponseUtil.class);
    Mockito.when(HttpResponseUtil.getResponseAsString(httpResponse)).thenReturn("true");

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    boolean hasXReproxy = false;
    try{
      hasXReproxy = cRepoConfigServiceImpl.hasXReproxy();
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoConfigDao).hasReProxy();
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertFalse(hasXReproxy);
  }

  @Test
  public void getRepoConfigTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoConfigDao.getRepoConfig()).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> configResponse = cRepoConfigServiceImpl.getRepoConfig();

    verify(contentRepoConfigDao).getRepoConfig();
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(configResponse);
    assertEquals(expectedResponse, configResponse);
  }

  @Test
  public void getRepoConfigThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoConfigDao.getRepoConfig()).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> configResponse = null;
    try{
      configResponse = cRepoConfigServiceImpl.getRepoConfig();
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoConfigDao).getRepoConfig();
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(configResponse);
  }

  @Test
  public void getRepoStatusTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoConfigDao.getRepoStatus()).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> statusResponse = cRepoConfigServiceImpl.getRepoStatus();

    verify(contentRepoConfigDao).getRepoStatus();
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(statusResponse);
    assertEquals(expectedResponse, statusResponse);
  }

  @Test
  public void getRepoStatusThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoConfigDao.getRepoStatus()).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> statusResponse = null;
    try{
      statusResponse = cRepoConfigServiceImpl.getRepoStatus();
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoConfigDao).getRepoStatus();
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(statusResponse);
  }


}
