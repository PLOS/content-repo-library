package org.plos.crepo.dao;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;


public class BaseDaoTest {

  protected static final String REPO_SERVER = "http://testServer";

  @Mock
  protected CloseableHttpClient httpClient;

  @Mock
  protected CloseableHttpResponse mockResponse;

  @Mock
  protected StatusLine statusLine;

  private static final String ERROR_MESSAGE = "bad request test";
  protected static final String SOME_URL = "http://someUrl";
  protected static final String  EXCEPTION_EXPECTED = "A Content Repo Exception was expected. ";

  protected void verifyException(ContentRepoException ex, HttpResponse response, ErrorType errorType) {
    assertNull(response);
    assertEquals(errorType, ex.getErrorType());
    assertTrue(ex.getMessage().contains(ERROR_MESSAGE));
  }

  protected void mockHttpResponseUtilCalls(CloseableHttpResponse mockResponse) {
    PowerMockito.mockStatic(HttpResponseUtil.class);
    Mockito.when(HttpResponseUtil.getErrorMessage(mockResponse)).thenReturn(ERROR_MESSAGE);
  }

  protected void mockCommonCalls(int status) throws IOException {
    when(httpClient.execute(isA(HttpRequestBase.class))).thenReturn(mockResponse);
    when(mockResponse.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(status);
  }

  protected void verifyCommonCalls(ArgumentCaptor argCaptor, StatusLine statusLine, int getStatusLineCalls, int getStatusCodeCalls) throws IOException {
    verify(httpClient).execute((HttpUriRequest) argCaptor.capture());
    verify(mockResponse, times(getStatusLineCalls)).getStatusLine();
    verify(statusLine, times(getStatusCodeCalls)).getStatusCode();
    HttpRequestBase httpRequest = (HttpRequestBase) argCaptor.getValue();
    assertEquals(SOME_URL, httpRequest.getURI().toString());
  }

}
