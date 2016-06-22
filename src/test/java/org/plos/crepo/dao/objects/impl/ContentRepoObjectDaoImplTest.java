package org.plos.crepo.dao.objects.impl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.BaseDaoTest;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.input.RepoObject;
import org.plos.crepo.util.HttpResponseUtil;
import org.plos.crepo.util.ObjectUrlGenerator;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, ObjectUrlGenerator.class})
public class ContentRepoObjectDaoImplTest extends BaseDaoTest {

  private static final String BUCKET_NAME = "bucket1";
  private static final String OBJECT_KEY = "objectKey";
  private static final String STRING_TIMESTAMP = "2014-09-23 11:47:15";
  private static final Timestamp TIMESTAMP = Timestamp.valueOf(STRING_TIMESTAMP);
  private static final String TAG = "test tag";
  private static final String VERSION_UUID = "65dee6e6-2e5c-47bd-adad-85060fa45a1f";
  private static final int VERSION_NUMBER = 0;
  private static final String DOWNLOAD_NAME = "objKeyDownloadName";
  private static final String CONTENT_TYPE = "text/plain";
  private static final RepoObject.ContentAccessor CONTENT = () -> new ByteArrayInputStream(new byte[2]);

  @Mock
  private ContentRepoAccessConfig repoAccessConfig;

  private ContentRepoObjectDaoImpl contentRepoObjectDaoImpl;

  @Before
  public void setUp(){
    contentRepoObjectDaoImpl = new ContentRepoObjectDaoImpl(repoAccessConfig);
    when(repoAccessConfig.getBucketName()).thenReturn(BUCKET_NAME);
    when(repoAccessConfig.getRepoServer()).thenReturn(REPO_SERVER);
    PowerMockito.mockStatic(ObjectUrlGenerator.class);
  }

  @Test
  public void getLatestObjectTest() throws IOException {

    when(ObjectUrlGenerator.getLatestObjectUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectDaoImpl.getLatestRepoObj(BUCKET_NAME, OBJECT_KEY);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getLatestObjectThrowsExcTest() throws IOException {

    when(ObjectUrlGenerator.getLatestObjectUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectDaoImpl.getLatestRepoObj(BUCKET_NAME, OBJECT_KEY);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObject);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getObjectUsingUuidTest() throws IOException {

    when(ObjectUrlGenerator.getObjectUsingUuidUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_UUID)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectDaoImpl.getRepoObjUsingUuid(BUCKET_NAME, OBJECT_KEY, VERSION_UUID);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getObjectUsingUuidThrowsExcTest() throws IOException {

    when(ObjectUrlGenerator.getObjectUsingUuidUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_UUID)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectDaoImpl.getRepoObjUsingUuid(BUCKET_NAME, OBJECT_KEY, VERSION_UUID);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObject);

      assertNull(response);
      assertTrue(ex.getMessage().contains(""));

    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getObjectUsingVersionNumTest() throws IOException {

    when(ObjectUrlGenerator.getObjectUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectDaoImpl.getRepoObjUsingVersionNum(BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getObjectUsingVersionNumThrowsExcTest() throws IOException {

    when(ObjectUrlGenerator.getObjectUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectDaoImpl.getRepoObjUsingVersionNum(BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObject);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getLatestObjectMetaTest() throws IOException {

    when(ObjectUrlGenerator.getLatestObjectMetaUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectDaoImpl.getRepoObjMetaLatestVersion(BUCKET_NAME, OBJECT_KEY);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getLatestObjectMetaThrowsExcTest() throws IOException {

    when(ObjectUrlGenerator.getLatestObjectMetaUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectDaoImpl.getRepoObjMetaLatestVersion(BUCKET_NAME, OBJECT_KEY);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObjectMeta);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getObjectMetaUsingUuidTest() throws IOException {

    when(ObjectUrlGenerator.getObjectMetaUsingUuidUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_UUID)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectDaoImpl.getRepoObjMetaUsingUuid(BUCKET_NAME, OBJECT_KEY, VERSION_UUID);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getObjectMetaUsingUuidThrowsExcTest() throws IOException {

    when(ObjectUrlGenerator.getObjectMetaUsingUuidUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_UUID)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectDaoImpl.getRepoObjMetaUsingUuid(BUCKET_NAME, OBJECT_KEY, VERSION_UUID);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObjectMeta);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getObjectMetaUsingVersionNumTest() throws IOException {

    when(ObjectUrlGenerator.getObjectMetaUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectDaoImpl.getRepoObjMetaUsingVersionNumber(BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getObjectMetaUsingVersionNumThrowsExcTest() throws IOException {

    when(ObjectUrlGenerator.getObjectMetaUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectDaoImpl.getRepoObjMetaUsingVersionNumber(BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObjectMeta);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getObjectMetaUsingTagTest() throws IOException {

    when(ObjectUrlGenerator.getGetObjMetaUsingTagUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, TAG)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectDaoImpl.getRepoObjMetaUsingTag(BUCKET_NAME, OBJECT_KEY, TAG);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getObjectMetaUsingTagThrowsExcTest() throws IOException {

    when(ObjectUrlGenerator.getGetObjMetaUsingTagUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, TAG)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectDaoImpl.getRepoObjMetaUsingTag(BUCKET_NAME, OBJECT_KEY, TAG);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObjectMeta);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void getObjectVersionsMetaTest() throws IOException {

    when(ObjectUrlGenerator.getObjectVersionsUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectDaoImpl.getRepoObjVersionsMeta(BUCKET_NAME, OBJECT_KEY);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getObjectVersionsMetaThrowsExcTest() throws IOException {

    when(ObjectUrlGenerator.getObjectVersionsUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectDaoImpl.getRepoObjVersionsMeta(BUCKET_NAME, OBJECT_KEY);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObjectVersions);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void deleteObjectUsingUuidTest() throws IOException {

    when(ObjectUrlGenerator.getObjectUsingUuidUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_UUID)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectDaoImpl.deleteRepoObjUsingUuid(BUCKET_NAME, OBJECT_KEY, VERSION_UUID);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);

  }

  @Test
  public void deleteObjectUsingUuidThrowsExcTest() throws IOException {

    when(ObjectUrlGenerator.getObjectUsingUuidUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_UUID)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectDaoImpl.deleteRepoObjUsingUuid(BUCKET_NAME, OBJECT_KEY, VERSION_UUID);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorDeletingObject);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void deleteObjectUsingVersionNumTest() throws IOException {

    when(ObjectUrlGenerator.getObjectUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectDaoImpl.deleteRepoObjUsingVersionNumber(BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

    assertNotNull(response);
    assertEquals(mockResponse, response);

  }

  @Test
  public void deleteObjectUsingVersionNumThrowsExcTest() throws IOException {

    when(ObjectUrlGenerator.getObjectUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectDaoImpl.deleteRepoObjUsingVersionNumber(BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorDeletingObject);
    }

    verifyCommonCalls(repoAccessConfig, httpGettArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  @Test
  public void versionObjectTest() throws IOException {
    postObjectsTest("VERSION");
  }

  @Test
  public void createObjectTest() throws IOException {
    postObjectsTest("NEW");
  }

  @Test
  public void autoCreateObjectTest() throws IOException {
    postObjectsTest("AUTO");
  }

  @Test
  public void createObjectThrownExcTest() throws IOException {
    postObjectsWithExc("NEW", ErrorType.ErrorCreatingObject);
  }

  @Test
  public void versionObjectThrownExcTest() throws IOException {
    postObjectsWithExc("VERSION", ErrorType.ErrorVersioningObject);
  }

  @Test
  public void autoCreateObjectThrownExcTest() throws IOException {
    postObjectsWithExc("AUTO", ErrorType.ErrorAutoCreatingObject);
  }

  private void postObjectsTest(String create) throws IOException {
    when(ObjectUrlGenerator.getCreateObjectUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpPost> httpPostArgument = ArgumentCaptor.forClass(HttpPost.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_CREATED);

    RepoObject repoObject = mock(RepoObject.class);
    mockRepoObjectCalls(repoObject);

    HttpResponse response = null;

    if ("NEW".equals(create)){
      response = contentRepoObjectDaoImpl.createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    } else if ("VERSION".equals(create)) {
      response = contentRepoObjectDaoImpl.versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    } else{
      response = contentRepoObjectDaoImpl.autoCreateRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    }

    verifyRepoObjectCalls(repoObject);
    verifyCommonCalls(repoAccessConfig, httpPostArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpPost httpPost = httpPostArgument.getValue();
    verifyHttpPost(httpPost);
    PowerMockito.verifyStatic();

  }

  public void postObjectsWithExc(String create, ErrorType errorType) throws IOException {
    when(ObjectUrlGenerator.getCreateObjectUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpPost> httpPostArgument = ArgumentCaptor.forClass(HttpPost.class);
    mockCommonCalls(repoAccessConfig, HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);
    RepoObject repoObject = mock(RepoObject.class);
    mockRepoObjectCalls(repoObject);

    HttpResponse response = null;

    try{
      if ("NEW".equals(create)){
        response = contentRepoObjectDaoImpl.createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
      } else if ("VERSION".equals(create)){
        response = contentRepoObjectDaoImpl.versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
      } else {
        response = contentRepoObjectDaoImpl.autoCreateRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
      }
    } catch(ContentRepoException ex){
      verifyException(ex, response, errorType);
    }

    verifyRepoObjectCalls(repoObject);
    verifyCommonCalls(repoAccessConfig, httpPostArgument, statusLine, 1, 1);
    PowerMockito.verifyStatic();

  }

  private void mockRepoObjectCalls(RepoObject repoObject) {

    when(repoObject.getKey()).thenReturn(OBJECT_KEY);
    when(repoObject.getContentAccessor()).thenReturn(CONTENT);
    when(repoObject.getTimestamp()).thenReturn(TIMESTAMP);
    when(repoObject.getDownloadName()).thenReturn(DOWNLOAD_NAME);
    when(repoObject.getCreationDate()).thenReturn(TIMESTAMP);
    when(repoObject.getTag()).thenReturn(TAG);

  }

  private void verifyRepoObjectCalls(RepoObject repoObject) {

    verify(repoObject).getKey();
    verify(repoObject, atLeastOnce()).getContentAccessor();
    verify(repoObject, times(2)).getTimestamp();
    verify(repoObject, times(2)).getDownloadName();
    verify(repoObject, times(2)).getCreationDate();
    verify(repoObject, times(2)).getTag();

  }

  private void verifyHttpPost(HttpPost httpPost) throws IOException {

    assertEquals(SOME_URL, httpPost.getURI().toString());
    HttpEntity entity = httpPost.getEntity();

    java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
    entity.writeTo(out);
    String entityContentAsString = new String(out.toByteArray());
    assertTrue(entityContentAsString.contains("bucketName"));
    assertTrue(entityContentAsString.contains("key"));
    assertTrue(entityContentAsString.contains("create"));
    assertTrue(entityContentAsString.contains("tag"));
    assertTrue(entityContentAsString.contains("creationDateTime"));
    assertTrue(entityContentAsString.contains("bucketName"));
    assertTrue(entityContentAsString.contains("timestamp"));

  }

}
