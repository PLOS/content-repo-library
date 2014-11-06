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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.plos.crepo.dao.BaseDaoTest;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.util.HttpResponseUtil;
import org.plos.crepo.util.ObjectUrlGenerator;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.sql.Timestamp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpResponseUtil.class)
public class ContentRepoObjectsDaoImplTest extends BaseDaoTest {

  private static final String BUCKET_NAME = "bucket1";
  private static final String OBJECT_KEY = "objectKey";
  private static final String STRING_TIMESTAMP = "2014-09-23 11:47:15";
  private static final Timestamp TIMESTAMP = Timestamp.valueOf(STRING_TIMESTAMP);
  private static final String TAG = "test tag";
  private static final String VERSION_CHECKSUM = "2312kbijdasnjdq733";
  private static final int VERSION_NUMBER = 0;
  private static final byte[] CONTENT = new byte[2];
  private static final String DOWNLOAD_NAME = "objKeyDownloadName";
  private static final String CONTENT_TYPE = "text/plain";

  @Mock
  private ObjectUrlGenerator objectUrlGenerator;

  @InjectMocks
  private ContentRepoObjectsDaoImpl contentRepoObjectsDaoImpl;

  @Before
  public void setUp(){
    contentRepoObjectsDaoImpl = new ContentRepoObjectsDaoImpl();
    initMocks(this);
    Whitebox.setInternalState(contentRepoObjectsDaoImpl, "repoServer", REPO_SERVER);
  }

  @Test
  public void getLatestObjectTest() throws IOException {

    when(objectUrlGenerator.getLatestObjectUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectsDaoImpl.getLatestRepoObj(BUCKET_NAME, OBJECT_KEY);

    verify(objectUrlGenerator).getLatestObjectUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getLatestObjectThrowsExcTest() throws IOException {

    when(objectUrlGenerator.getLatestObjectUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectsDaoImpl.getLatestRepoObj(BUCKET_NAME, OBJECT_KEY);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObject);
    }

    verify(objectUrlGenerator).getLatestObjectUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void getObjectUsingVersionCksTest() throws IOException {

    when(objectUrlGenerator.getObjectUsingVersionCksUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectsDaoImpl.getRepoObjUsingVersionCks(BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM);

    verify(objectUrlGenerator).getObjectUsingVersionCksUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getObjectUsingVersionCksThrowsExcTest() throws IOException {

    when(objectUrlGenerator.getObjectUsingVersionCksUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectsDaoImpl.getRepoObjUsingVersionCks(BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObject);

      assertNull(response);
      assertTrue(ex.getMessage().contains(""));

    }

    verify(objectUrlGenerator).getObjectUsingVersionCksUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void getObjectUsingVersionNumTest() throws IOException {

    when(objectUrlGenerator.getObjectUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectsDaoImpl.getRepoObjUsingVersionNum(BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);

    verify(objectUrlGenerator).getObjectUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getObjectUsingVersionNumThrowsExcTest() throws IOException {

    when(objectUrlGenerator.getObjectUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectsDaoImpl.getRepoObjUsingVersionNum(BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObject);
    }

    verify(objectUrlGenerator).getObjectUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void getLatestObjectMetaTest() throws IOException {

    when(objectUrlGenerator.getLatestObjectMetaUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectsDaoImpl.getRepoObjMetaLatestVersion(BUCKET_NAME, OBJECT_KEY);

    verify(objectUrlGenerator).getLatestObjectMetaUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getLatestObjectMetaThrowsExcTest() throws IOException {

    when(objectUrlGenerator.getLatestObjectMetaUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectsDaoImpl.getRepoObjMetaLatestVersion(BUCKET_NAME, OBJECT_KEY);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObjectMeta);
    }

    verify(objectUrlGenerator).getLatestObjectMetaUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void getObjectMetaUsingVersionCksTest() throws IOException {

    when(objectUrlGenerator.getObjectMetaUsingVersionCksUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectsDaoImpl.getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM);

    verify(objectUrlGenerator).getObjectMetaUsingVersionCksUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getObjectMetaUsingVersionCksThrowsExcTest() throws IOException {

    when(objectUrlGenerator.getObjectMetaUsingVersionCksUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectsDaoImpl.getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObjectMeta);
    }

    verify(objectUrlGenerator).getObjectMetaUsingVersionCksUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void getObjectMetaUsingVersionNumTest() throws IOException {

    when(objectUrlGenerator.getObjectMetaUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectsDaoImpl.getRepoObjMetaUsingVersionNumber(BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);

    verify(objectUrlGenerator).getObjectMetaUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getObjectMetaUsingVersionNumThrowsExcTest() throws IOException {

    when(objectUrlGenerator.getObjectMetaUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectsDaoImpl.getRepoObjMetaUsingVersionNumber(BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObjectMeta);
    }

    verify(objectUrlGenerator).getObjectMetaUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void getObjectMetaUsingTagTest() throws IOException {

    when(objectUrlGenerator.getGetObjMetaUsingTagUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, TAG)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectsDaoImpl.getRepoObjMetaUsingTag(BUCKET_NAME, OBJECT_KEY, TAG);

    verify(objectUrlGenerator).getGetObjMetaUsingTagUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, TAG);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getObjectMetaUsingTagThrowsExcTest() throws IOException {

    when(objectUrlGenerator.getGetObjMetaUsingTagUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, TAG)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectsDaoImpl.getRepoObjMetaUsingTag(BUCKET_NAME, OBJECT_KEY, TAG);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObjectMeta);
    }

    verify(objectUrlGenerator).getGetObjMetaUsingTagUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, TAG);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void getObjectVersionsMetaTest() throws IOException {

    when(objectUrlGenerator.getObjectVersionsUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectsDaoImpl.getRepoObjVersionsMeta(BUCKET_NAME, OBJECT_KEY);

    verify(objectUrlGenerator).getObjectVersionsUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getObjectVersionsMetaThrowsExcTest() throws IOException {

    when(objectUrlGenerator.getObjectVersionsUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectsDaoImpl.getRepoObjVersionsMeta(BUCKET_NAME, OBJECT_KEY);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingObjectVersions);
    }

    verify(objectUrlGenerator).getObjectVersionsUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void deleteObjectUsingVersionCksTest() throws IOException {

    when(objectUrlGenerator.getDeleteObjectVersionCksUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectsDaoImpl.deleteRepoObjUsingVersionCks(BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM);

    verify(objectUrlGenerator).getDeleteObjectVersionCksUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);

  }

  @Test
  public void deleteObjectUsingVersionCksThrowsExcTest() throws IOException {

    when(objectUrlGenerator.getDeleteObjectVersionCksUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectsDaoImpl.deleteRepoObjUsingVersionCks(BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorDeletingObject);
    }

    verify(objectUrlGenerator).getDeleteObjectVersionCksUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_CHECKSUM);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void deleteObjectUsingVersionNumTest() throws IOException {

    when(objectUrlGenerator.getDeleteObjectVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoObjectsDaoImpl.deleteRepoObjUsingVersionNumber(BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);

    verify(objectUrlGenerator).getDeleteObjectVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);

  }

  @Test
  public void deleteObjectUsingVersionNumThrowsExcTest() throws IOException {

    when(objectUrlGenerator.getDeleteObjectVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoObjectsDaoImpl.deleteRepoObjUsingVersionNumber(BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorDeletingObject);
    }

    verify(objectUrlGenerator).getDeleteObjectVersionNumUrl(REPO_SERVER, BUCKET_NAME, OBJECT_KEY, VERSION_NUMBER);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

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
  public void createObjectThrownExcTest() throws IOException {
    postObjectsWithExc("NEW", ErrorType.ErrorCreatingObject);
  }

  @Test
  public void versionObjectThrownExcTest() throws IOException {
    postObjectsWithExc("VERSION", ErrorType.ErrorVersioningObject);
  }

  private void postObjectsTest(String create) throws IOException {
    when(objectUrlGenerator.getCreateObjectUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpPost> httpPostArgument = ArgumentCaptor.forClass(HttpPost.class);
    mockCommonCalls(HttpStatus.SC_CREATED);

    RepoObject repoObject = mock(RepoObject.class);
    mockRepoObjectCalls(repoObject);

    HttpResponse response = null;

    if ("NEW".equals(create)){
      response = contentRepoObjectsDaoImpl.createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    } else{
      response = contentRepoObjectsDaoImpl.versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    }

    verify(objectUrlGenerator).getCreateObjectUrl(REPO_SERVER);
    verifyRepoObjectCalls(repoObject);
    verifyCommonCalls(httpPostArgument, statusLine, 2, 2);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpPost httpPost = httpPostArgument.getValue();
    verifyHttpPost(httpPost);

  }

  public void postObjectsWithExc(String create, ErrorType errorType) throws IOException {
    when(objectUrlGenerator.getCreateObjectUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpPost> httpPostArgument = ArgumentCaptor.forClass(HttpPost.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);
    RepoObject repoObject = mock(RepoObject.class);
    mockRepoObjectCalls(repoObject);

    HttpResponse response = null;

    try{
      if ("NEW".equals(create)){
        response = contentRepoObjectsDaoImpl.createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
      } else{
        response = contentRepoObjectsDaoImpl.versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
      }
    } catch(ContentRepoException ex){
      verifyException(ex, response, errorType);
    }

    verifyRepoObjectCalls(repoObject);
    verify(objectUrlGenerator).getCreateObjectUrl(REPO_SERVER);
    verifyCommonCalls(httpPostArgument, statusLine, 2, 2);

  }

  private void mockRepoObjectCalls(RepoObject repoObject) {

    when(repoObject.getKey()).thenReturn(OBJECT_KEY);
    when(repoObject.getByteContent()).thenReturn(CONTENT);
    when(repoObject.getTimestamp()).thenReturn(TIMESTAMP);
    when(repoObject.getDownloadName()).thenReturn(DOWNLOAD_NAME);
    when(repoObject.getCreationDate()).thenReturn(TIMESTAMP);
    when(repoObject.getTag()).thenReturn(TAG);

  }

  private void verifyRepoObjectCalls(RepoObject repoObject) {

    verify(repoObject).getKey();
    verify(repoObject, times(2)).getByteContent();
    verify(repoObject, times(2)).getTimestamp();
    verify(repoObject, times(2)).getDownloadName();
    verify(repoObject, times(2)).getCreationDate();
    verify(repoObject, times(2)).getTag();

  }

  private void verifyHttpPost(HttpPost httpPost) throws IOException {

    assertEquals(SOME_URL, httpPost.getURI().toString());
    HttpEntity entity = httpPost.getEntity();

    java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream((int)entity.getContentLength());
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