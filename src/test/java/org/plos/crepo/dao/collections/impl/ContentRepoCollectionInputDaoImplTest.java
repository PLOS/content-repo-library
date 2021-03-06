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

package org.plos.crepo.dao.collections.impl;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.BaseDaoTest;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.identity.RepoVersion;
import org.plos.crepo.model.input.RepoCollectionInput;
import org.plos.crepo.util.CollectionUrlGenerator;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, CollectionUrlGenerator.class})
public class ContentRepoCollectionInputDaoImplTest extends BaseDaoTest {

  private static final String COLLECTION_KEY = "collectionKey";
  private static final String STRING_TIMESTAMP = "2014-09-23 11:47:15";
  private static final String TAG = "test tag";
  private static final String VERSION_UUID = "a90cdd64-0536-40c2-b07d-03f7f2ed6ee6";
  private static final int VERSION_NUMBER = 0;

  @Mock
  private ContentRepoAccessConfig repoAccessConfig;

  private ContentRepoCollectionDaoImpl contentRepoCollectionDaoImpl;

  @Before
  public void setUp() {
    contentRepoCollectionDaoImpl = new ContentRepoCollectionDaoImpl(repoAccessConfig);
    when(repoAccessConfig.getRepoServer()).thenReturn(REPO_SERVER);
    PowerMockito.mockStatic(CollectionUrlGenerator.class);
  }

  @Test
  public void versionCollectionTest() throws IOException {
    postCollectionsTest("VERSION");
  }

  @Test
  public void createCollectionTest() throws IOException {
    postCollectionsTest("NEW");
  }

  @Test
  public void createCollectionThrownExcTest() throws IOException {
    postCollectionsWithExc("NEW", ErrorType.ErrorCreatingCollection);
  }

  @Test
  public void versionCollectionThrownExcTest() throws IOException {
    postCollectionsWithExc("VERSION", ErrorType.ErrorVersioningCollection);
  }

  @Test
  public void deleteCollectionUsingUuidTest() throws IOException {

    when(CollectionUrlGenerator.getCollectionUuidUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_UUID)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.deleteCollectionUsingUuid(BUCKET_NAME, COLLECTION_KEY, VERSION_UUID);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);

  }

  @Test
  public void deleteCollectionUsingUuidThrowsExcTest() throws IOException {

    when(CollectionUrlGenerator.getCollectionUuidUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_UUID)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try {
      response = contentRepoCollectionDaoImpl.deleteCollectionUsingUuid(BUCKET_NAME, COLLECTION_KEY, VERSION_UUID);
      fail(EXCEPTION_EXPECTED);
    } catch (ContentRepoException ex) {
      verifyException(ex, response, ErrorType.ErrorDeletingCollection);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void deleteCollectionUsingVersionNumTest() throws IOException {

    when(CollectionUrlGenerator.getCollectionVersionNumUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.deleteCollectionUsingVersionNumber(BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);

  }

  @Test
  public void deleteCollectionUsingVersionNumThrowsExcTest() throws IOException {

    when(CollectionUrlGenerator.getCollectionVersionNumUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try {
      response = contentRepoCollectionDaoImpl.deleteCollectionUsingVersionNumber(BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER);
      fail(EXCEPTION_EXPECTED);
    } catch (ContentRepoException ex) {
      verifyException(ex, response, ErrorType.ErrorDeletingCollection);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getCollectionUsingUuidTest() throws IOException {

    when(CollectionUrlGenerator.getCollectionUuidUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_UUID)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getCollectionUsingUuid(BUCKET_NAME, COLLECTION_KEY, VERSION_UUID);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getCollectionUsingVersionNumThrowsExcTest() throws IOException {

    when(CollectionUrlGenerator.getCollectionUuidUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_UUID)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try {
      response = contentRepoCollectionDaoImpl.getCollectionUsingUuid(BUCKET_NAME, COLLECTION_KEY, VERSION_UUID);
      fail(EXCEPTION_EXPECTED);
    } catch (ContentRepoException ex) {
      verifyException(ex, response, ErrorType.ErrorFetchingCollection);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getCollectionVersionsTest() throws IOException {

    when(CollectionUrlGenerator.getCollectionVersionsUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getCollectionVersions(BUCKET_NAME, COLLECTION_KEY);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getCollectionVersionsThrowsExcTest() throws IOException {

    when(CollectionUrlGenerator.getCollectionVersionsUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try {
      response = contentRepoCollectionDaoImpl.getCollectionVersions(BUCKET_NAME, COLLECTION_KEY);
      fail(EXCEPTION_EXPECTED);
    } catch (ContentRepoException ex) {
      verifyException(ex, response, ErrorType.ErrorFetchingCollectionVersions);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getCollectionUsingVersionNumTest() throws IOException {

    when(CollectionUrlGenerator.getCollectionVersionNumUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getCollectionUsingVersionNumber(BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getCollectionUsingUuidThrowsExcTest() throws IOException {

    when(CollectionUrlGenerator.getCollectionVersionNumUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try {
      response = contentRepoCollectionDaoImpl.getCollectionUsingVersionNumber(BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER);
      fail(EXCEPTION_EXPECTED);
    } catch (ContentRepoException ex) {
      verifyException(ex, response, ErrorType.ErrorFetchingCollection);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getCollectionUsingTagTest() throws IOException {

    when(CollectionUrlGenerator.getCollectionTagUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, TAG)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getCollectionUsingTag(BUCKET_NAME, COLLECTION_KEY, TAG);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getCollectionUsingTagThrowsExcTest() throws IOException {

    when(CollectionUrlGenerator.getCollectionTagUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, TAG)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try {
      response = contentRepoCollectionDaoImpl.getCollectionUsingTag(BUCKET_NAME, COLLECTION_KEY, TAG);
      fail(EXCEPTION_EXPECTED);
    } catch (ContentRepoException ex) {
      verifyException(ex, response, ErrorType.ErrorFetchingCollection);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }


  @Test
  public void getCollectionsUsingTagTest() throws IOException {
    when(CollectionUrlGenerator.getCollectionsUsingTagUrl(REPO_SERVER, BUCKET_NAME, 0, 10, true, TAG)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getCollectionsUsingTag(BUCKET_NAME, 0, 10, true, TAG);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());
  }

  @Test
  public void getCollectionsUsingTagThrownExcTest() throws IOException {
    when(CollectionUrlGenerator.getCollectionsUsingTagUrl(REPO_SERVER, BUCKET_NAME, 0, 10, true, TAG)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try {
      response = contentRepoCollectionDaoImpl.getCollectionsUsingTag(BUCKET_NAME, 0, 10, true, TAG);
      fail(EXCEPTION_EXPECTED);
    } catch (ContentRepoException ex) {
      verifyException(ex, response, ErrorType.ErrorFetchingCollections);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();
  }


  @Test
  public void getCollectionsTest() throws IOException {

    when(CollectionUrlGenerator.getGetCollectionsUrl(REPO_SERVER, BUCKET_NAME, 0, 10, true)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getCollections(BUCKET_NAME, 0, 10, true);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getCollectionsThrownExcTest() throws IOException {

    when(CollectionUrlGenerator.getGetCollectionsUrl(REPO_SERVER, BUCKET_NAME, 0, 10, true)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try {
      response = contentRepoCollectionDaoImpl.getCollections(BUCKET_NAME, 0, 10, true);
      fail(EXCEPTION_EXPECTED);
    } catch (ContentRepoException ex) {
      verifyException(ex, response, ErrorType.ErrorFetchingCollections);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  private void postCollectionsTest(String create) throws IOException {
    when(CollectionUrlGenerator.getCreateCollUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpPost> httpPostArgument = ArgumentCaptor.forClass(HttpPost.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_CREATED);

    RepoCollectionInput repoCollectionInput = mock(RepoCollectionInput.class);
    mockRepoCollectionCalls(repoCollectionInput);

    HttpResponse response = null;

    if ("NEW".equals(create)) {
      response = contentRepoCollectionDaoImpl.createCollection(BUCKET_NAME, repoCollectionInput);
    } else {
      response = contentRepoCollectionDaoImpl.versionCollection(BUCKET_NAME, repoCollectionInput);
    }

    verify(repoCollectionInput).getKey();
    verify(repoCollectionInput).getObjects();
    verifyCommonCalls(repoAccessConfig, httpPostArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpPost httpPost = httpPostArgument.getValue();
    if ("NEW".equals(create)) {
      verifyHttpPost(httpPost, "NEW");
    } else {
      verifyHttpPost(httpPost, "VERSION");
    }

  }

  public void postCollectionsWithExc(String create, ErrorType errorType) throws IOException {
    when(CollectionUrlGenerator.getCreateCollUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpPost> httpPostArgument = ArgumentCaptor.forClass(HttpPost.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);
    RepoCollectionInput repoCollectionInput = mock(RepoCollectionInput.class);

    HttpResponse response = null;

    try {
      if ("NEW".equals(create)) {
        response = contentRepoCollectionDaoImpl.createCollection(BUCKET_NAME, repoCollectionInput);
      } else {
        response = contentRepoCollectionDaoImpl.versionCollection(BUCKET_NAME, repoCollectionInput);
      }
    } catch (ContentRepoException ex) {
      verifyException(ex, response, errorType);
    }

    verifyCommonCalls(repoAccessConfig, httpPostArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  private void mockRepoCollectionCalls(RepoCollectionInput repoCollectionInput) {
    when(repoCollectionInput.getKey()).thenReturn(COLLECTION_KEY);
    when(repoCollectionInput.getObjects()).thenReturn(ImmutableList.<RepoVersion>of());
    when(repoCollectionInput.getTimestamp()).thenReturn(STRING_TIMESTAMP);
    when(repoCollectionInput.getTag()).thenReturn(TAG);
    when(repoCollectionInput.getCreationDateTime()).thenReturn(STRING_TIMESTAMP);
  }

  private void verifyHttpPost(HttpPost httpPost, String creationMethod) throws IOException {

    assertEquals(SOME_URL, httpPost.getURI().toString());
    String params = EntityUtils.toString(httpPost.getEntity(), CharEncoding.UTF_8);
    Gson gson = new Gson();
    Map<String, Object> postParams = gson.fromJson(params, new TypeToken<Map<String, Object>>() {
    }.getType());
    assertEquals(BUCKET_NAME, postParams.get("bucketName"));
    assertEquals(COLLECTION_KEY, postParams.get("key"));
    assertEquals(creationMethod, postParams.get("create"));
    assertEquals(TAG, postParams.get("tag"));
    assertEquals(STRING_TIMESTAMP, postParams.get("creationDateTime"));
    assertEquals(STRING_TIMESTAMP, postParams.get("timestamp"));
  }


}
