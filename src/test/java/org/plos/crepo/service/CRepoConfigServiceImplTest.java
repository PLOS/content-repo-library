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
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, Gson.class})
public class CRepoConfigServiceImplTest extends BaseServiceTest {

  private ContentRepoService cRepoConfigServiceImpl;

  @Mock
  private ContentRepoConfigDao contentRepoConfigDao;

  @Before
  public void setUp() {
    cRepoConfigServiceImpl = new TestContentRepoServiceBuilder()
        .setGson(gson)
        .setConfigDao(contentRepoConfigDao)
        .build();
    Whitebox.setInternalState(cRepoConfigServiceImpl, "gson", gson);
  }

  @Test
  public void hasXReproxyTest() throws IOException {
    CloseableHttpResponse httpResponse = mockJsonResponse(true);
    when(contentRepoConfigDao.hasReProxy()).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    boolean hasXReproxy = cRepoConfigServiceImpl.hasXReproxy();

    verify(contentRepoConfigDao).hasReProxy();
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertTrue(hasXReproxy);
  }

  @Test
  public void hasXReproxyThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mockJsonResponse(true);
    when(contentRepoConfigDao.hasReProxy()).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    boolean hasXReproxy = false;
    try {
      hasXReproxy = cRepoConfigServiceImpl.hasXReproxy();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoConfigDao).hasReProxy();
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertFalse(hasXReproxy);
  }

  @Test
  public void getRepoConfigTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(TEST_METADATA);
    when(contentRepoConfigDao.getRepoConfig()).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> configResponse = cRepoConfigServiceImpl.getRepoConfig();

    verify(contentRepoConfigDao).getRepoConfig();
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(configResponse);
    assertEquals(expectedResponse, configResponse);
  }

  @Test
  public void getRepoConfigThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoConfigDao.getRepoConfig()).thenReturn(httpResponse);
    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> configResponse = null;
    try {
      configResponse = cRepoConfigServiceImpl.getRepoConfig();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoConfigDao).getRepoConfig();
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(configResponse);
  }

  @Test
  public void getRepoStatusTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(TEST_METADATA);
    when(contentRepoConfigDao.getRepoStatus()).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> statusResponse = cRepoConfigServiceImpl.getRepoStatus();

    verify(contentRepoConfigDao).getRepoStatus();
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(statusResponse);
    assertEquals(expectedResponse, statusResponse);
  }

  @Test
  public void getRepoStatusThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoConfigDao.getRepoStatus()).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> statusResponse = null;
    try {
      statusResponse = cRepoConfigServiceImpl.getRepoStatus();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoConfigDao).getRepoStatus();
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(statusResponse);
  }

}
