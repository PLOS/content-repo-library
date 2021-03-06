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
import org.plos.crepo.dao.collections.ContentRepoCollectionDao;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.identity.RepoId;
import org.plos.crepo.model.identity.RepoVersion;
import org.plos.crepo.model.identity.RepoVersionNumber;
import org.plos.crepo.model.identity.RepoVersionTag;
import org.plos.crepo.model.input.RepoCollectionInput;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
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
@PrepareForTest({HttpResponseUtil.class, Gson.class})
public class CRepoCollectionInputServiceImplTest extends BaseServiceTest {

  private static final String BUCKET_NAME = "bucketName";
  private static final String VERSION_UUID = "d8562db9-6974-499f-a522-9e7a945d48bb";
  private static final RepoVersion DUMMY_VERSION = RepoVersion.create(BUCKET_NAME, KEY, VERSION_UUID);
  private static final int VERSION_NUMBER = 0;
  private static final String TAG = "tag";
  private static final int OFFSET = 0;
  private static final int LIMIT = 10;
  private static final ImmutableMap<String, Object> TEST_COLL_METADATA = ImmutableMap.<String, Object>builder()
      .put("testCollField", "testCollValue")
      .put("objects", ImmutableList.of(
          ImmutableMap.of("testObjField", "testObjValue")
      ))
      .build();
  private static final ImmutableList<Map<String, Object>> TEST_COLL_METADATA_LIST = ImmutableList.<Map<String, Object>>of(TEST_COLL_METADATA);

  private ContentRepoService cRepoCollectionServiceImpl;

  @Mock
  private ContentRepoCollectionDao contentRepoCollectionDao;

  @Mock
  private ContentRepoAccessConfig repoAccessConfig;

  @Before
  public void setUp() {
    cRepoCollectionServiceImpl = new TestContentRepoServiceBuilder()
        .setAccessConfig(repoAccessConfig)
        .setCollectionDao(contentRepoCollectionDao)
        .build();
    Whitebox.setInternalState(cRepoCollectionServiceImpl, "gson", gson);
  }

  @Test
  public void createCollectionTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_COLL_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);

    RepoCollectionInput repoCollectionInput = mock(RepoCollectionInput.class);
    when(repoCollectionInput.getBucketName()).thenReturn(BUCKET_NAME);
    when(repoCollectionInput.getKey()).thenReturn("key");
    when(contentRepoCollectionDao.createCollection(BUCKET_NAME, repoCollectionInput)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.createCollection(repoCollectionInput).getMapView();

    verify(repoCollectionInput, atLeastOnce()).getKey();
    verify(contentRepoCollectionDao).createCollection(BUCKET_NAME, repoCollectionInput);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void createCollectionThrowsExpTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_COLL_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);

    RepoCollectionInput repoCollectionInput = mock(RepoCollectionInput.class);
    when(repoCollectionInput.getBucketName()).thenReturn(BUCKET_NAME);
    when(repoCollectionInput.getKey()).thenReturn("key");
    when(contentRepoCollectionDao.createCollection(BUCKET_NAME, repoCollectionInput)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> collectionResponse = null;
    try {
      collectionResponse = cRepoCollectionServiceImpl.createCollection(repoCollectionInput).getMapView();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(repoCollectionInput, atLeastOnce()).getKey();
    verify(contentRepoCollectionDao).createCollection(BUCKET_NAME, repoCollectionInput);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);

  }

  @Test
  public void versionCollectionTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_COLL_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);

    RepoCollectionInput repoCollectionInput = mock(RepoCollectionInput.class);
    when(repoCollectionInput.getBucketName()).thenReturn(BUCKET_NAME);
    when(repoCollectionInput.getKey()).thenReturn("key");
    when(contentRepoCollectionDao.versionCollection(BUCKET_NAME, repoCollectionInput)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.versionCollection(repoCollectionInput).getMapView();

    verify(repoCollectionInput, atLeastOnce()).getKey();
    verify(contentRepoCollectionDao).versionCollection(BUCKET_NAME, repoCollectionInput);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void versionCollectionThrowsExpTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_COLL_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);

    RepoCollectionInput repoCollectionInput = mock(RepoCollectionInput.class);
    when(repoCollectionInput.getBucketName()).thenReturn(BUCKET_NAME);
    when(repoCollectionInput.getKey()).thenReturn("key");
    when(contentRepoCollectionDao.versionCollection(BUCKET_NAME, repoCollectionInput)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> collectionResponse = null;
    try {
      collectionResponse = cRepoCollectionServiceImpl.versionCollection(repoCollectionInput).getMapView();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(repoCollectionInput, atLeastOnce()).getKey();
    verify(contentRepoCollectionDao).versionCollection(BUCKET_NAME, repoCollectionInput);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);
  }

  @Test
  public void deleteCollectionUsingVersionNumbTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoCollectionDao.deleteCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    boolean deleted = cRepoCollectionServiceImpl.deleteCollection(RepoVersionNumber.create(BUCKET_NAME, KEY, VERSION_NUMBER));

    verify(contentRepoCollectionDao).deleteCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(httpResponse, atLeastOnce()).close();
    assertTrue(deleted);
  }

  @Test
  public void deleteCollectionUsingVersionNumbThrowsExpTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoCollectionDao.deleteCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    boolean deleted = false;
    try {
      deleted = cRepoCollectionServiceImpl.deleteCollection(RepoVersionNumber.create(BUCKET_NAME, KEY, VERSION_NUMBER));
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoCollectionDao).deleteCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(httpResponse, atLeastOnce()).close();
    assertFalse(deleted);
  }

  @Test
  public void deleteCollectionUsingUuidTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoCollectionDao.deleteCollectionUsingUuid(BUCKET_NAME, KEY, VERSION_UUID)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    boolean deleted = cRepoCollectionServiceImpl.deleteCollection(DUMMY_VERSION);

    verify(contentRepoCollectionDao).deleteCollectionUsingUuid(BUCKET_NAME, KEY, VERSION_UUID);
    verify(httpResponse, atLeastOnce()).close();
    assertTrue(deleted);
  }

  @Test
  public void deleteCollectionUsingUuidThrowsExpTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoCollectionDao.deleteCollectionUsingUuid(BUCKET_NAME, KEY, VERSION_UUID)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    boolean deleted = false;
    try {
      deleted = cRepoCollectionServiceImpl.deleteCollection(DUMMY_VERSION);
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoCollectionDao).deleteCollectionUsingUuid(BUCKET_NAME, KEY, VERSION_UUID);
    verify(httpResponse, atLeastOnce()).close();
    assertFalse(deleted);
  }

  @Test
  public void getCollectionUsingUuidTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_COLL_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoCollectionDao.getCollectionUsingUuid(BUCKET_NAME, KEY, VERSION_UUID)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.getCollection(DUMMY_VERSION).getMapView();

    verify(contentRepoCollectionDao).getCollectionUsingUuid(BUCKET_NAME, KEY, VERSION_UUID);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionUsingUuidThrowsExpTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_COLL_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoCollectionDao.getCollectionUsingUuid(BUCKET_NAME, KEY, VERSION_UUID)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> collectionResponse = null;

    try {
      collectionResponse = cRepoCollectionServiceImpl.getCollection(DUMMY_VERSION).getMapView();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoCollectionDao).getCollectionUsingUuid(BUCKET_NAME, KEY, VERSION_UUID);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);
  }

  @Test
  public void getCollectionUsingVersionNumberTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_COLL_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoCollectionDao.getCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.getCollection(RepoVersionNumber.create(BUCKET_NAME, KEY, VERSION_NUMBER)).getMapView();

    verify(contentRepoCollectionDao).getCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionUsingVersionNumberThrowsExpTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_COLL_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoCollectionDao.getCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> collectionResponse = null;
    try {
      collectionResponse = cRepoCollectionServiceImpl.getCollection(RepoVersionNumber.create(BUCKET_NAME, KEY, VERSION_NUMBER)).getMapView();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }


    verify(contentRepoCollectionDao).getCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);
  }

  @Test
  public void getCollectionUsingTagTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_COLL_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoCollectionDao.getCollectionUsingTag(BUCKET_NAME, KEY, TAG)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.getCollection(RepoVersionTag.create(BUCKET_NAME, KEY, TAG)).getMapView();

    verify(contentRepoCollectionDao).getCollectionUsingTag(BUCKET_NAME, KEY, TAG);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionUsingTagThrowsExpTest() throws IOException {
    Map<String, Object> expectedResponse = TEST_COLL_METADATA;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoCollectionDao.getCollectionUsingTag(BUCKET_NAME, KEY, TAG)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    Map<String, Object> collectionResponse = null;
    try {
      collectionResponse = cRepoCollectionServiceImpl.getCollection(RepoVersionTag.create(BUCKET_NAME, KEY, TAG)).getMapView();
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoCollectionDao).getCollectionUsingTag(BUCKET_NAME, KEY, TAG);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);
  }

  @Test
  public void getCollectionVersionsTest() throws IOException {
    List<Map<String, Object>> expectedResponse = TEST_COLL_METADATA_LIST;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoCollectionDao.getCollectionVersions(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> collectionResponse = asRawList(cRepoCollectionServiceImpl.getCollectionVersions(RepoId.create(BUCKET_NAME, KEY)));

    verify(contentRepoCollectionDao).getCollectionVersions(BUCKET_NAME, KEY);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }


  @Test
  public void getCollectionVersionsThrowsExpTest() throws IOException {
    List<Map<String, Object>> expectedResponse = TEST_COLL_METADATA_LIST;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoCollectionDao.getCollectionVersions(BUCKET_NAME, KEY)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    List<Map<String, Object>> collectionResponse = null;
    try {
      collectionResponse = asRawList(cRepoCollectionServiceImpl.getCollectionVersions(RepoId.create(BUCKET_NAME, KEY)));
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoCollectionDao).getCollectionVersions(BUCKET_NAME, KEY);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);
  }

  @Test
  public void getCollectionsUsingTagTest() throws IOException {
    List<Map<String, Object>> expectedResponse = TEST_COLL_METADATA_LIST;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoCollectionDao.getCollectionsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> collectionResponse = asRawList(cRepoCollectionServiceImpl.getCollections(BUCKET_NAME, OFFSET, LIMIT, true, TAG));

    verify(contentRepoCollectionDao).getCollectionsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionsUsingTagThrowsExcTest() throws IOException {
    List<Map<String, Object>> expectedResponse = TEST_COLL_METADATA_LIST;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoCollectionDao.getCollectionsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    List<Map<String, Object>> collectionResponse = null;
    try {
      collectionResponse = asRawList(cRepoCollectionServiceImpl.getCollections(BUCKET_NAME, OFFSET, LIMIT, true, TAG));
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoCollectionDao).getCollectionsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);
  }


  @Test
  public void getCollectionsTest() throws IOException {
    List<Map<String, Object>> expectedResponse = TEST_COLL_METADATA_LIST;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoCollectionDao.getCollections(BUCKET_NAME, OFFSET, LIMIT, true)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> collectionResponse = asRawList(cRepoCollectionServiceImpl.getCollections(BUCKET_NAME, OFFSET, LIMIT, true, null));

    verify(contentRepoCollectionDao).getCollections(BUCKET_NAME, OFFSET, LIMIT, true);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);

  }

  @Test
  public void getCollectionsThrowsExcTest() throws IOException {
    List<Map<String, Object>> expectedResponse = TEST_COLL_METADATA_LIST;
    CloseableHttpResponse httpResponse = mockJsonResponse(expectedResponse);
    when(contentRepoCollectionDao.getCollections(BUCKET_NAME, OFFSET, LIMIT, true)).thenReturn(httpResponse);

    Mockito.doThrow(TestExpectedException.class).when(httpResponse).close();

    List<Map<String, Object>> collectionResponse = null;
    try {
      collectionResponse = asRawList(cRepoCollectionServiceImpl.getCollections(BUCKET_NAME, OFFSET, LIMIT, true, null));
    } catch (ContentRepoException exception) {
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(TestExpectedException.class, exception.getCause().getClass());
    }

    verify(contentRepoCollectionDao).getCollections(BUCKET_NAME, OFFSET, LIMIT, true);
    verify(httpResponse, atLeastOnce()).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);
  }

}
