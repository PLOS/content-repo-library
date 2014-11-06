package org.plos.crepo.dao.config.impl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
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
import org.plos.crepo.util.ConfigUrlGenerator;
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
public class ContentRepoConfigDaoImplTest extends BaseDaoTest {

  private static final java.lang.String SOME_URL = "http://someUrl";

  @Mock
  private ConfigUrlGenerator configUrlGenerator;

  @InjectMocks
  private ContentRepoConfigDaoImpl contentRepoConfigDaoImpl;

  @Before
  public void setUp(){
    contentRepoConfigDaoImpl = new ContentRepoConfigDaoImpl();
    initMocks(this);
    Whitebox.setInternalState(contentRepoConfigDaoImpl, "repoServer", REPO_SERVER);
  }

  @Test
  public void hasReProxyTest() throws IOException {
    when(configUrlGenerator.getHasReproxyUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGetArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoConfigDaoImpl.hasReProxy();

    verify(configUrlGenerator).getHasReproxyUrl(REPO_SERVER);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    verifyCommonCalls(httpGetArgument, statusLine, 1, 1);

  }

  @Test
  public void hasReProxyThrowsExcTest() throws IOException {

    when(configUrlGenerator.getHasReproxyUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGetArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoConfigDaoImpl.hasReProxy();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingReproxyData);
    }

    verify(configUrlGenerator).getHasReproxyUrl(REPO_SERVER);
    verifyCommonCalls(httpGetArgument, statusLine, 2, 2);

  }

  @Test
  public void getRepoConfigTest() throws IOException {
    when(configUrlGenerator.getRepoConfigUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGetArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoConfigDaoImpl.getRepoConfig();

    verify(configUrlGenerator).getRepoConfigUrl(REPO_SERVER);
    verifyCommonCalls(httpGetArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);

  }

  @Test
  public void getRepoConfigThrowsExcTest() throws IOException {

    when(configUrlGenerator.getRepoConfigUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGetArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoConfigDaoImpl.getRepoConfig();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingConfig);
    }

    verify(configUrlGenerator).getRepoConfigUrl(REPO_SERVER);
    verifyCommonCalls(httpGetArgument, statusLine, 2, 2);

  }

  @Test
  public void getRepoStatusTest() throws IOException {
    when(configUrlGenerator.getRepoStatusUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGetArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoConfigDaoImpl.getRepoStatus();

    verify(configUrlGenerator).getRepoStatusUrl(REPO_SERVER);
    verifyCommonCalls(httpGetArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);

  }

  @Test
  public void getRepoStatusThrowsExcTest() throws IOException {

    when(configUrlGenerator.getRepoStatusUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGetArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoConfigDaoImpl.getRepoStatus();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingStatus);
    }

    verify(configUrlGenerator).getRepoStatusUrl(REPO_SERVER);
    verifyCommonCalls(httpGetArgument, statusLine, 2, 2);

  }

}
