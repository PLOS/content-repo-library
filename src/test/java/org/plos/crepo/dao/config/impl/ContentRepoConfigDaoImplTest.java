package org.plos.crepo.dao.config.impl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.BaseDaoTest;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.ConfigUrlGenerator;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, ConfigUrlGenerator.class})
public class ContentRepoConfigDaoImplTest extends BaseDaoTest {

  @Mock
  private ContentRepoAccessConfig repoAccessConfig;

  private ContentRepoConfigDaoImpl contentRepoConfigDaoImpl;

  @Before
  public void setUp(){
    contentRepoConfigDaoImpl = new ContentRepoConfigDaoImpl(repoAccessConfig);
    when(repoAccessConfig.getBucketName()).thenReturn(BUCKET_NAME);
    when(repoAccessConfig.getRepoServer()).thenReturn(REPO_SERVER);
    PowerMockito.mockStatic(ConfigUrlGenerator.class);
  }

  @Test
  public void hasReProxyTest() throws IOException {
    when(ConfigUrlGenerator.getHasReproxyUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGetArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoConfigDaoImpl.hasReProxy();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    verifyCommonCalls(repoAccessConfig, httpGetArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void hasReProxyThrowsExcTest() throws IOException {

    when(ConfigUrlGenerator.getHasReproxyUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGetArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoConfigDaoImpl.hasReProxy();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingReproxyData);
    }

    verifyCommonCalls(repoAccessConfig, httpGetArgument, statusLine, 2, 2);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getRepoConfigTest() throws IOException {
    when(ConfigUrlGenerator.getRepoConfigUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGetArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoConfigDaoImpl.getRepoConfig();

    verifyCommonCalls(repoAccessConfig, httpGetArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);

  }

  @Test
  public void getRepoConfigThrowsExcTest() throws IOException {

    when(ConfigUrlGenerator.getRepoConfigUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGetArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoConfigDaoImpl.getRepoConfig();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingConfig);
    }

    verifyCommonCalls(repoAccessConfig, httpGetArgument, statusLine, 2, 2);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getRepoStatusTest() throws IOException {
    when(ConfigUrlGenerator.getRepoStatusUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGetArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoConfigDaoImpl.getRepoStatus();

    verifyCommonCalls(repoAccessConfig, httpGetArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);

  }

  @Test
  public void getRepoStatusThrowsExcTest() throws IOException {

    when(ConfigUrlGenerator.getRepoStatusUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGetArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoConfigDaoImpl.getRepoStatus();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingStatus);
    }

    verifyCommonCalls(repoAccessConfig, httpGetArgument, statusLine, 2, 2);
    PowerMockito.verifyStatic();

  }

}
