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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.objects.ContentRepoObjectDao;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.model.RepoVersion;
import org.plos.crepo.model.RepoVersionNumber;
import org.plos.crepo.model.RepoVersionTag;
import org.plos.crepo.model.validator.RepoObjectValidator;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
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
@PrepareForTest({HttpResponseUtil.class, Gson.class, RepoObjectValidator.class})
public class CRepoObjectServiceImplTest extends BaseServiceTest {

  private static final String VERSION_UUID = "31a6f1cd-ef28-49fa-b811-f881ac4811f5";
  private static final RepoVersion DUMMY_VERSION = RepoVersion.create(KEY, VERSION_UUID);
  private static final String BUCKET_NAME = "bucketName";
  private static final int VERSION_NUMBER = 0;
  private static final String TAG = "tag";
  private static final int OFFSET = 0;
  private static final int LIMIT = 10;
  private static final String URL1 = "http://url1";
  private static final String URL2 = "http://url1";
  private static final String CONTENT_TYPE = "text/plain";
  private static final ImmutableList<String> URLS = ImmutableList.of(URL1, URL2);

  private ContentRepoService cRepoObjectServiceImpl;

  @Mock
  private ContentRepoObjectDao contentRepoObjectDao;

  @Mock
  private RepoObjectValidator repoObjectValidator;

  @Mock
  private ContentRepoAccessConfig repoAccessConfig;

  @Before
  public void setUp() {
    cRepoObjectServiceImpl = new TestContentRepoServiceBuilder()
        .setAccessConfig(repoAccessConfig)
        .setObjectDao(contentRepoObjectDao)
        .build();
    Whitebox.setInternalState(cRepoObjectServiceImpl, "gson", gson);
    when(repoAccessConfig.getBucketName()).thenReturn(BUCKET_NAME);

  }

  @Test
  public void getRepoObjRedirectURLTest() throws IOException {
    Map<String, Object> expectedResponse = ImmutableMap.<String, Object>of("reproxyURL", URLS);
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoObjectDao.getRepoObjMetaLatestVersion(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<URL> urls = cRepoObjectServiceImpl.getLatestRepoObjectMetadata(KEY).getReproxyUrls();

    verify(contentRepoObjectDao).getRepoObjMetaLatestVersion(BUCKET_NAME, KEY);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(urls);
    assertEquals(2, urls.size());
    assertEquals(URL1, urls.get(0).toString());
    assertEquals(URL2, urls.get(1).toString());
  }

  @Test
  public void getRepoObjRedirectURLThrowsExcTest() throws IOException {
    Map<String, Object> expectedResponse = mock(HashMap.class);
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoObjectDao.getRepoObjMetaLatestVersion(BUCKET_NAME, KEY)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    List<URL> urls = null;
    try {
      urls = cRepoObjectServiceImpl.getLatestRepoObjectMetadata(KEY).getReproxyUrls();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoObjectDao).getRepoObjMetaLatestVersion(BUCKET_NAME, KEY);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(urls);

  }

  @Test
  public void getRepoObjRedirectURLCksTest() throws IOException {
    Map<String, Object> expectedResponse = ImmutableMap.<String, Object>of("reproxyURL", URLS);
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoObjectDao.getRepoObjMetaUsingUuid(BUCKET_NAME, KEY, VERSION_UUID)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<URL> urls = cRepoObjectServiceImpl.getRepoObjectMetadata(DUMMY_VERSION).getReproxyUrls();

    verify(contentRepoObjectDao).getRepoObjMetaUsingUuid(BUCKET_NAME, KEY, VERSION_UUID);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(urls);
    assertEquals(2, urls.size());
    assertEquals(URL1, urls.get(0).toString());
    assertEquals(URL2, urls.get(1).toString());
  }

  @Test
  public void getRepoObjRedirectURLCksThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaUsingUuid(BUCKET_NAME, KEY, VERSION_UUID)).thenReturn(httpResponse);

    IOException expectedException = mock(TestExpectedException.class);
    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    List<URL> urls = null;
    try {
      urls = cRepoObjectServiceImpl.getRepoObjectMetadata(DUMMY_VERSION).getReproxyUrls();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoObjectDao).getRepoObjMetaUsingUuid(BUCKET_NAME, KEY, VERSION_UUID);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(urls);

  }

  @Test
  public void getRepoObjMetaLatestVersionTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(TEST_METADATA);
    when(contentRepoObjectDao.getRepoObjMetaLatestVersion(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.getLatestRepoObjectMetadata(KEY).getMapView();

    verify(contentRepoObjectDao).getRepoObjMetaLatestVersion(BUCKET_NAME, KEY);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getRepoObjMetaLatestVersionThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaLatestVersion(BUCKET_NAME, KEY)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> objectResponse = null;
    try {
      objectResponse = cRepoObjectServiceImpl.getLatestRepoObjectMetadata(KEY).getMapView();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoObjectDao).getRepoObjMetaLatestVersion(BUCKET_NAME, KEY);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void getObjectMetaUsingUuidTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoObjectDao.getRepoObjMetaUsingUuid(BUCKET_NAME, KEY, VERSION_UUID)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.getRepoObjectMetadata(DUMMY_VERSION).getMapView();

    verify(contentRepoObjectDao).getRepoObjMetaUsingUuid(BUCKET_NAME, KEY, VERSION_UUID);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getObjectMetaUsingUuidThrowsTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaUsingUuid(BUCKET_NAME, KEY, VERSION_UUID)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> objectResponse = null;
    try {
      objectResponse = cRepoObjectServiceImpl.getRepoObjectMetadata(DUMMY_VERSION).getMapView();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoObjectDao).getRepoObjMetaUsingUuid(BUCKET_NAME, KEY, VERSION_UUID);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void getRepoObjMetaUsingVersionNumTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoObjectDao.getRepoObjMetaUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.getRepoObjectMetadata(new RepoVersionNumber(KEY, VERSION_NUMBER)).getMapView();

    verify(contentRepoObjectDao).getRepoObjMetaUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getRepoObjMetaUsingVersionNumThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> objectResponse = null;
    try {
      objectResponse = cRepoObjectServiceImpl.getRepoObjectMetadata(new RepoVersionNumber(KEY, VERSION_NUMBER)).getMapView();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoObjectDao).getRepoObjMetaUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void getRepoObjMetaUsingTagTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoObjectDao.getRepoObjMetaUsingTag(BUCKET_NAME, KEY, TAG)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.getRepoObjectMetadata(new RepoVersionTag(KEY, TAG)).getMapView();

    verify(contentRepoObjectDao).getRepoObjMetaUsingTag(BUCKET_NAME, KEY, TAG);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getRepoObjMetaUsingTagThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaUsingTag(BUCKET_NAME, KEY, TAG)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> objectResponse = null;
    try {
      objectResponse = cRepoObjectServiceImpl.getRepoObjectMetadata(new RepoVersionTag(KEY, TAG)).getMapView();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoObjectDao).getRepoObjMetaUsingTag(BUCKET_NAME, KEY, TAG);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void getObjVersionsTest() throws IOException {
    List<Map<String, Object>> expectedResponse = TEST_METADATA_LIST;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoObjectDao.getRepoObjVersionsMeta(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> objectResponse = asRawList(cRepoObjectServiceImpl.getRepoObjectVersions(KEY));

    verify(contentRepoObjectDao).getRepoObjVersionsMeta(BUCKET_NAME, KEY);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getObjVersionsThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjVersionsMeta(BUCKET_NAME, KEY)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    List<Map<String, Object>> objectResponse = null;
    try {
      objectResponse = asRawList(cRepoObjectServiceImpl.getRepoObjectVersions(KEY));
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoObjectDao).getRepoObjVersionsMeta(BUCKET_NAME, KEY);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void getObjectsUsingTagTest() throws IOException {
    List<Map<String, Object>> expectedResponse = TEST_METADATA_LIST;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoObjectDao.getObjectsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> objectResponse = asRawList(cRepoObjectServiceImpl.getRepoObjects(OFFSET, LIMIT, true, TAG));

    verify(contentRepoObjectDao).getObjectsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getObjectsUsingTagThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getObjectsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    List<Map<String, Object>> objectResponse = null;
    try {
      objectResponse = asRawList(cRepoObjectServiceImpl.getRepoObjects(OFFSET, LIMIT, true, TAG));
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoObjectDao).getObjectsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void getObjectsTest() throws IOException {
    List<Map<String, Object>> expectedResponse = TEST_METADATA_LIST;
    CloseableHttpResponse httpResponse = mockJsonResponse(TEST_METADATA_LIST);
    when(contentRepoObjectDao.getObjects(BUCKET_NAME, OFFSET, LIMIT, true)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> objectResponse = asRawList(cRepoObjectServiceImpl.getRepoObjects(OFFSET, LIMIT, true, null));

    verify(contentRepoObjectDao).getObjects(BUCKET_NAME, OFFSET, LIMIT, true);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getObjectsThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getObjects(BUCKET_NAME, OFFSET, LIMIT, true)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    List<Map<String, Object>> objectResponse = null;
    try {
      objectResponse = asRawList(cRepoObjectServiceImpl.getRepoObjects(OFFSET, LIMIT, true, null));
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoObjectDao).getObjects(BUCKET_NAME, OFFSET, LIMIT, true);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }


  @Test
  public void deleteCollectionUsingVersionNumbTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.deleteRepoObjUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    boolean deleted = cRepoObjectServiceImpl.deleteRepoObject(new RepoVersionNumber(KEY, VERSION_NUMBER));

    verify(contentRepoObjectDao).deleteRepoObjUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    assertTrue(deleted);
  }

  @Test
  public void deleteCollectionUsingVersionNumbThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.deleteRepoObjUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);

    IOException expectedException = mock(TestExpectedException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    boolean deleted = false;
    try {
      deleted = cRepoObjectServiceImpl.deleteRepoObject(new RepoVersionNumber(KEY, VERSION_NUMBER));
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).deleteRepoObjUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    assertFalse(deleted);
  }

  @Test
  public void deleteRepoObjUsingUuidTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.deleteRepoObjUsingUuid(BUCKET_NAME, KEY, VERSION_UUID)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    boolean deleted = cRepoObjectServiceImpl.deleteRepoObject(DUMMY_VERSION);

    verify(contentRepoObjectDao).deleteRepoObjUsingUuid(BUCKET_NAME, KEY, VERSION_UUID);
    verify(httpResponse, atLeastOnce()).close();
    assertTrue(deleted);
  }

  @Test
  public void deleteRepoObjUsingUuidThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.deleteRepoObjUsingUuid(BUCKET_NAME, KEY, VERSION_UUID)).thenReturn(httpResponse);

    IOException expectedException = mock(TestExpectedException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    boolean deleted = false;
    try {
      deleted = cRepoObjectServiceImpl.deleteRepoObject(DUMMY_VERSION);
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).deleteRepoObjUsingUuid(BUCKET_NAME, KEY, VERSION_UUID);
    verify(httpResponse, atLeastOnce()).close();
    assertFalse(deleted);
  }

  @Test
  public void createRepoObjectTest() throws Exception {
    Map<String, Object> expectedResponse = TEST_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    RepoObject repoObject = mock(RepoObject.class);
    PowerMockito.mockStatic(RepoObjectValidator.class);
    PowerMockito.doNothing().when(RepoObjectValidator.class, "validate", repoObject);
    when(repoObject.probeContentType()).thenReturn(CONTENT_TYPE);
    when(contentRepoObjectDao.createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE)).thenReturn(httpResponse);

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.createRepoObject(repoObject).getMapView();

    verify(repoObject).probeContentType();
    verify(contentRepoObjectDao).createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }


  @Test
  public void createRepoObjectThrowsExcTest() throws Exception {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);

    RepoObject repoObject = mock(RepoObject.class);
    PowerMockito.mockStatic(RepoObjectValidator.class);
    PowerMockito.doNothing().when(RepoObjectValidator.class, "validate", repoObject);
    when(repoObject.probeContentType()).thenReturn(CONTENT_TYPE);
    when(contentRepoObjectDao.createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> objectResponse = null;
    try {
      objectResponse = cRepoObjectServiceImpl.createRepoObject(repoObject).getMapView();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(repoObject).probeContentType();
    verify(contentRepoObjectDao).createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void versionRepoObjectTest() throws Exception {
    Map<String, Object> expectedResponse = TEST_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    RepoObject repoObject = mock(RepoObject.class);
    PowerMockito.mockStatic(RepoObjectValidator.class);
    PowerMockito.doNothing().when(RepoObjectValidator.class, "validate", repoObject);
    when(repoObject.probeContentType()).thenReturn(CONTENT_TYPE);
    when(contentRepoObjectDao.versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE)).thenReturn(httpResponse);

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.versionRepoObject(repoObject).getMapView();

    verify(repoObject).probeContentType();
    verify(contentRepoObjectDao).versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);

  }

  @Test
  public void versionRepoObjectThrowsExcTest() throws Exception {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);

    RepoObject repoObject = mock(RepoObject.class);
    PowerMockito.mockStatic(RepoObjectValidator.class);
    PowerMockito.doNothing().when(RepoObjectValidator.class, "validate", repoObject);
    when(repoObject.probeContentType()).thenReturn(CONTENT_TYPE);
    when(contentRepoObjectDao.versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> objectResponse = null;
    try {
      objectResponse = cRepoObjectServiceImpl.versionRepoObject(repoObject).getMapView();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(repoObject).probeContentType();
    verify(contentRepoObjectDao).versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

}
