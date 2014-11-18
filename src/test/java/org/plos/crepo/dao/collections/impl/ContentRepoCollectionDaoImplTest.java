package org.plos.crepo.dao.collections.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.util.EntityUtils;
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
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoCollectionObject;
import org.plos.crepo.util.CollectionUrlGenerator;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpResponseUtil.class)
public class ContentRepoCollectionDaoImplTest extends BaseDaoTest {

  private static final String BUCKET_NAME = "bucket1";
  private static final String COLLECTION_KEY = "collectionKey";
  private static final String STRING_TIMESTAMP = "2014-09-23 11:47:15";
  private static final String TAG = "test tag";
  private static final String VERSION_CHECKSUM = "2312kbijdasnjdq733";
  private static final int VERSION_NUMBER = 0;

  @Mock
  private CollectionUrlGenerator collectionUrlGenerator;

  @InjectMocks
  private ContentRepoCollectionDaoImpl contentRepoCollectionDaoImpl;

  @Before
  public void setUp(){
    contentRepoCollectionDaoImpl = new ContentRepoCollectionDaoImpl();
    initMocks(this);
    Whitebox.setInternalState(contentRepoCollectionDaoImpl, "repoServer", REPO_SERVER);
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
  public void versionCollectionThrownExcTest() throws IOException { postCollectionsWithExc("VERSION", ErrorType.ErrorVersioningCollection); }

  @Test
  public void deleteCollectionUsingVersionCksTest() throws IOException {

    when(collectionUrlGenerator.getDeleteCollUsingVersionCksUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_CHECKSUM)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.deleteCollectionUsingVersionCks(BUCKET_NAME, COLLECTION_KEY, VERSION_CHECKSUM);

    verify(collectionUrlGenerator).getDeleteCollUsingVersionCksUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_CHECKSUM);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);

  }

  @Test
  public void deleteCollectionUsingVersionCksThrowsExcTest() throws IOException {

    when(collectionUrlGenerator.getDeleteCollUsingVersionCksUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_CHECKSUM)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoCollectionDaoImpl.deleteCollectionUsingVersionCks(BUCKET_NAME, COLLECTION_KEY, VERSION_CHECKSUM);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorDeletingCollection);
    }

    verify(collectionUrlGenerator).getDeleteCollUsingVersionCksUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_CHECKSUM);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void deleteCollectionUsingVersionNumTest() throws IOException {

    when(collectionUrlGenerator.getDeleteCollUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.deleteCollectionUsingVersionNumb(BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER);

    verify(collectionUrlGenerator).getDeleteCollUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);

  }

  @Test
  public void deleteCollectionUsingVersionNumThrowsExcTest() throws IOException {

    when(collectionUrlGenerator.getDeleteCollUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpDelete> httpGettArgument = ArgumentCaptor.forClass(HttpDelete.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoCollectionDaoImpl.deleteCollectionUsingVersionNumb(BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorDeletingCollection);
    }

    verify(collectionUrlGenerator).getDeleteCollUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void getCollectionUsingVersionCksTest() throws IOException {

    when(collectionUrlGenerator.getGetCollVersionCksUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_CHECKSUM)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getCollectionUsingVersionCks(BUCKET_NAME, COLLECTION_KEY, VERSION_CHECKSUM);

    verify(collectionUrlGenerator).getGetCollVersionCksUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_CHECKSUM);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getCollectionUsingVersionNumThrowsExcTest() throws IOException {

    when(collectionUrlGenerator.getGetCollVersionCksUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_CHECKSUM)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoCollectionDaoImpl.getCollectionUsingVersionCks(BUCKET_NAME, COLLECTION_KEY, VERSION_CHECKSUM);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingCollection);
    }

    verify(collectionUrlGenerator).getGetCollVersionCksUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_CHECKSUM);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void getCollectionVersionsTest() throws IOException {

    when(collectionUrlGenerator.getGetCollVersionsUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getCollectionVersions(BUCKET_NAME, COLLECTION_KEY);

    verify(collectionUrlGenerator).getGetCollVersionsUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getCollectionVersionsThrowsExcTest() throws IOException {

    when(collectionUrlGenerator.getGetCollVersionsUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoCollectionDaoImpl.getCollectionVersions(BUCKET_NAME, COLLECTION_KEY);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingCollectionVersions);
    }

    verify(collectionUrlGenerator).getGetCollVersionsUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void getCollectionUsingVersionNumTest() throws IOException {

    when(collectionUrlGenerator.getGetCollUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getCollectionUsingVersionNumber(BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER);

    verify(collectionUrlGenerator).getGetCollUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getCollectionUsingVersionCksThrowsExcTest() throws IOException {

    when(collectionUrlGenerator.getGetCollUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoCollectionDaoImpl.getCollectionUsingVersionNumber(BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingCollection);
    }

    verify(collectionUrlGenerator).getGetCollUsingVersionNumUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, VERSION_NUMBER);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  @Test
  public void getCollectionUsingTagTest() throws IOException {

    when(collectionUrlGenerator.getGetCollUsingTagUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, TAG)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getCollectionUsingTag(BUCKET_NAME, COLLECTION_KEY, TAG);

    verify(collectionUrlGenerator).getGetCollUsingTagUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, TAG);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getCollectionUsingTagThrowsExcTest() throws IOException {

    when(collectionUrlGenerator.getGetCollUsingTagUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, TAG)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoCollectionDaoImpl.getCollectionUsingTag(BUCKET_NAME, COLLECTION_KEY, TAG);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingCollection);
    }

    verify(collectionUrlGenerator).getGetCollUsingTagUrl(REPO_SERVER, BUCKET_NAME, COLLECTION_KEY, TAG);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }


  @Test
  public void getCollectionsUsingTagTest() throws IOException {
    when(collectionUrlGenerator.getGetCollectionsUsingTagUrl(REPO_SERVER, BUCKET_NAME, 0, 10, true, TAG)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getCollectionsUsingTag(BUCKET_NAME, 0, 10, true, TAG);

    verify(collectionUrlGenerator).getGetCollectionsUsingTagUrl(REPO_SERVER, BUCKET_NAME, 0, 10, true, TAG);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());
  }

  @Test
  public void getCollectionsUsingTagThrownExcTest() throws IOException {
    when(collectionUrlGenerator.getGetCollectionsUsingTagUrl(REPO_SERVER, BUCKET_NAME, 0, 10, true, TAG)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoCollectionDaoImpl.getCollectionsUsingTag(BUCKET_NAME, 0, 10, true, TAG);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingCollections);
    }

    verify(collectionUrlGenerator).getGetCollectionsUsingTagUrl(REPO_SERVER, BUCKET_NAME, 0, 10, true, TAG);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);
  }


  @Test
  public void getCollectionsTest() throws IOException {

    when(collectionUrlGenerator.getGetCollectionsUrl(REPO_SERVER, BUCKET_NAME, 0, 10, true)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getCollections(BUCKET_NAME, 0, 10, true);

    verify(collectionUrlGenerator).getGetCollectionsUrl(REPO_SERVER, BUCKET_NAME, 0, 10, true);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }

  @Test
  public void getCollectionsThrownExcTest() throws IOException {

    when(collectionUrlGenerator.getGetCollectionsUrl(REPO_SERVER, BUCKET_NAME, 0, 10, true)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);

    HttpResponse response = null;
    try{
      response = contentRepoCollectionDaoImpl.getCollections(BUCKET_NAME, 0, 10, true);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException ex){
      verifyException(ex, response, ErrorType.ErrorFetchingCollections);
    }

    verify(collectionUrlGenerator).getGetCollectionsUrl(REPO_SERVER, BUCKET_NAME, 0, 10, true);
    verifyCommonCalls(httpGettArgument, statusLine, 2, 2);

  }

  private void postCollectionsTest(String create) throws IOException {
    when(collectionUrlGenerator.getCreateCollUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpPost> httpPostArgument = ArgumentCaptor.forClass(HttpPost.class);
    mockCommonCalls(HttpStatus.SC_CREATED);

    RepoCollection repoCollection = mock(RepoCollection.class);
    mockRepoCollectionCalls(repoCollection);

    HttpResponse response = null;

    if ("NEW".equals(create)){
      response = contentRepoCollectionDaoImpl.createCollection(BUCKET_NAME, repoCollection);
    } else{
      response = contentRepoCollectionDaoImpl.versionCollection(BUCKET_NAME, repoCollection);
    }

    verify(collectionUrlGenerator).getCreateCollUrl(REPO_SERVER);
    verify(repoCollection).getKey();
    verify(repoCollection).getObjects();
    verifyCommonCalls(httpPostArgument, statusLine, 2, 2);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpPost httpPost = httpPostArgument.getValue();
    if ("NEW".equals(create)){
      verifyHttpPost(httpPost, "NEW");
    } else{
      verifyHttpPost(httpPost, "VERSION");
    }

  }

  public void postCollectionsWithExc(String create, ErrorType errorType) throws IOException {
    when(collectionUrlGenerator.getCreateCollUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpPost> httpPostArgument = ArgumentCaptor.forClass(HttpPost.class);
    mockCommonCalls(HttpStatus.SC_BAD_REQUEST);
    mockHttpResponseUtilCalls(mockResponse);
    RepoCollection repoCollection = mock(RepoCollection.class);

    HttpResponse response = null;

    try{
      if ("NEW".equals(create)){
        response = contentRepoCollectionDaoImpl.createCollection(BUCKET_NAME, repoCollection);
      } else{
        response = contentRepoCollectionDaoImpl.versionCollection(BUCKET_NAME, repoCollection);
      }
    } catch(ContentRepoException ex){
      verifyException(ex, response, errorType);
    }

    verify(collectionUrlGenerator).getCreateCollUrl(REPO_SERVER);
    verifyCommonCalls(httpPostArgument, statusLine, 2, 2);

  }

  private void mockRepoCollectionCalls(RepoCollection repoCollection) {
    when(repoCollection.getKey()).thenReturn(COLLECTION_KEY);
    when(repoCollection.getObjects()).thenReturn(new ArrayList<RepoCollectionObject>());
    when(repoCollection.getTimestamp()).thenReturn(STRING_TIMESTAMP);
    when(repoCollection.getTag()).thenReturn(TAG);
    when(repoCollection.getCreationDateTime()).thenReturn(STRING_TIMESTAMP);
  }

  private void verifyHttpPost(HttpPost httpPost, String creationMethod) throws IOException {

    assertEquals(SOME_URL, httpPost.getURI().toString());
    String params = EntityUtils.toString(httpPost.getEntity(), CharEncoding.UTF_8);
    Gson gson = new Gson();
    Map<String, Object> postParams = gson.fromJson(params, new TypeToken<Map<String, Object>>() {}.getType());
    assertEquals(BUCKET_NAME, postParams.get("bucketName"));
    assertEquals(COLLECTION_KEY, postParams.get("key"));
    assertEquals(creationMethod, postParams.get("create"));
    assertEquals(TAG, postParams.get("tag"));
    assertEquals(STRING_TIMESTAMP, postParams.get("creationDateTime"));
    assertEquals(STRING_TIMESTAMP, postParams.get("timestamp"));
  }

 /* @Test
  public void getBucketsTest() throws IOException {

    when(collectionUrlGenerator.getBucketsUrl(REPO_SERVER)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getBuckets();

    verify(collectionUrlGenerator).getBucketsUrl(REPO_SERVER);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }


  @Test
  public void getBucketTest() throws IOException {

    when(collectionUrlGenerator.getBucketUrl(REPO_SERVER, BUCKET_NAME)).thenReturn(SOME_URL);
    ArgumentCaptor<HttpGet> httpGettArgument = ArgumentCaptor.forClass(HttpGet.class);
    mockCommonCalls(HttpStatus.SC_OK);

    HttpResponse response = contentRepoCollectionDaoImpl.getBucket(BUCKET_NAME);

    verify(collectionUrlGenerator).getBucketUrl(REPO_SERVER, BUCKET_NAME);
    verifyCommonCalls(httpGettArgument, statusLine, 1, 1);

    assertNotNull(response);
    assertEquals(mockResponse, response);
    HttpGet httpGet = httpGettArgument.getValue();
    assertEquals(SOME_URL, httpGet.getURI().toString());

  }*/

}
