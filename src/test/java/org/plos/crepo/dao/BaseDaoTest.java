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
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.*;


public class BaseDaoTest {

  protected static final String SOME_URL = "http://someUrl";
  protected static final String REPO_SERVER = "http://testServer";
  protected static final String BUCKET_NAME = "bucket1";

  @Mock
  protected CloseableHttpClient httpClient;

  @Mock
  protected CloseableHttpResponse mockResponse;

  @Mock
  protected StatusLine statusLine;

  private static final String ERROR_MESSAGE = "bad request test";
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

  protected void mockCommonCalls(ContentRepoAccessConfig accessConfig, int status) throws IOException {
    when(accessConfig.open(isA(HttpRequestBase.class))).thenReturn(mockResponse);
    when(mockResponse.getStatusLine()).thenReturn(statusLine);
    when(statusLine.getStatusCode()).thenReturn(status);
  }

  protected void verifyCommonCalls(ContentRepoAccessConfig accessConfig, ArgumentCaptor argCaptor, StatusLine statusLine, int getStatusLineCalls, int getStatusCodeCalls) throws IOException {
    verify(accessConfig).open((HttpUriRequest) argCaptor.capture());
    verify(mockResponse, times(getStatusLineCalls)).getStatusLine();
    verify(statusLine, times(getStatusCodeCalls)).getStatusCode();
    HttpRequestBase httpRequest = (HttpRequestBase) argCaptor.getValue();
    assertEquals(SOME_URL, httpRequest.getURI().toString());
  }

}
