package org.plos.crepo.dao;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.internal.util.reflection.Whitebox;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.URI;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpResponseUtil.class)
public class ContentRepoBaseDaoTest extends BaseDaoTest{

  @InjectMocks
  private TestContentRepoBaseDaoImpl contentRepoBaseDao;
  private URI uri = URI.create("http://testUri");

  @Before
  public void setUp(){
    contentRepoBaseDao = new TestContentRepoBaseDaoImpl();
    initMocks(this);
    Whitebox.setInternalState(contentRepoBaseDao, "repoServer", REPO_SERVER);
  }

  @Test
  public void executeRequestTest() throws IOException {
    HttpRequestBase httpRequest = mock(HttpRequestBase.class);
    when(httpRequest.getURI()).thenReturn(uri);

    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoBaseDao.executeRequest(httpRequest, ErrorType.ErrorFetchingBucketMeta);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    assertEquals(uri, httpRequest.getURI());

    verify(httpClient).execute(httpRequest);
    verify(mockResponse).getStatusLine();
    verify(statusLine).getStatusCode();

  }

  @Test
  public void executeRequestThrowsExcTest() throws IOException {
    HttpRequestBase httpRequest = mock(HttpRequestBase.class);
    when(httpRequest.getURI()).thenReturn(uri);

    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;

    try{
      response = contentRepoBaseDao.executeRequest(httpRequest, ErrorType.ErrorFetchingBucketMeta);
    } catch(ContentRepoException ex) {
      verifyException(ex, response, ErrorType.ErrorFetchingBucketMeta);
    }

    verify(httpClient).execute(httpRequest);
    verify(mockResponse, times(2)).getStatusLine();
    verify(statusLine, times(2)).getStatusCode();
    verify(httpRequest, times(2)).getURI();

    assertNull(response);
    assertEquals(uri, httpRequest.getURI());

  }

  @Test
  public void executeRequestIOThrowsExcTest() throws IOException {
    HttpRequestBase httpRequest = mock(HttpRequestBase.class);
    when(httpRequest.getURI()).thenReturn(uri);
    IOException exception = mock(IOException.class);
    when(httpClient.execute(isA(HttpRequestBase.class))).thenThrow(exception);

    HttpResponse response = null;

    try{
      response = contentRepoBaseDao.executeRequest(httpRequest, ErrorType.ErrorFetchingBucketMeta);
    } catch(ContentRepoException ex) {
      assertNull(response);
      assertEquals(ErrorType.ErrorFetchingBucketMeta, ex.getErrorType());
      assertTrue(ex.getMessage().contains(uri.toString()));
      assertEquals(exception, ex.getCause());
    }

    verify(httpClient).execute(httpRequest);
    verify(httpRequest, times(2)).getURI();

    assertEquals(uri, httpRequest.getURI());

  }

}
