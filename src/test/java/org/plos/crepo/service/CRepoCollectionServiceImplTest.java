package org.plos.crepo.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.service.BaseServiceTest;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, Gson.class})
public class CRepoCollectionServiceImplTest extends BaseServiceTest{

  private static final String VERSION_CHECKSUM = "EWQW432423FSDF235CFDSW";
  private static final String BUCKET_NAME = "bucketName";
  private static final int VERSION_NUMBER = 0;
  private static final String TAG = "tag";
  private static final int OFFSET = 0;
  private static final int LIMIT = 10;

  private ContentRepoService cRepoCollectionServiceImpl;

  @Mock
  private ContentRepoCollectionDao contentRepoCollectionDao;

  @Mock
  private ContentRepoAccessConfig repoAccessConfig;

  @Mock
  private CloseableHttpResponse httpResponse;

  @Before
  public void setUp(){
    gson = PowerMockito.mock(Gson.class);
    cRepoCollectionServiceImpl = new TestContentRepoServiceBuilder()
        .setAccessConfig(repoAccessConfig)
        .setCollectionDao(contentRepoCollectionDao)
        .build();
    Whitebox.setInternalState(cRepoCollectionServiceImpl, "gson", gson);
    when(repoAccessConfig.getBucketName()).thenReturn(BUCKET_NAME);
  }

  @Test
  public void createCollectionTest() throws IOException {
    RepoCollection repoCollection = mock(RepoCollection.class);
    when(repoCollection.getKey()).thenReturn("key");
    when(contentRepoCollectionDao.createCollection(BUCKET_NAME, repoCollection)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.createCollection(repoCollection);

    verify(repoCollection).getKey();
    verify(contentRepoCollectionDao).createCollection(BUCKET_NAME, repoCollection);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void createCollectionThrowsExpTest() throws IOException {
    RepoCollection repoCollection = mock(RepoCollection.class);
    when(repoCollection.getKey()).thenReturn("key");
    when(contentRepoCollectionDao.createCollection(BUCKET_NAME, repoCollection)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> collectionResponse = null;
    try{
      collectionResponse = cRepoCollectionServiceImpl.createCollection(repoCollection);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(repoCollection).getKey();
    verify(contentRepoCollectionDao).createCollection(BUCKET_NAME, repoCollection);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);

  }

  @Test
  public void versionCollectionTest() throws IOException {
    RepoCollection repoCollection = mock(RepoCollection.class);
    when(repoCollection.getKey()).thenReturn("key");
    when(contentRepoCollectionDao.versionCollection(BUCKET_NAME, repoCollection)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.versionCollection(repoCollection);

    verify(repoCollection).getKey();
    verify(contentRepoCollectionDao).versionCollection(BUCKET_NAME, repoCollection);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void versionCollectionThrowsExpTest() throws IOException {
    RepoCollection repoCollection = mock(RepoCollection.class);
    when(repoCollection.getKey()).thenReturn("key");
    when(contentRepoCollectionDao.versionCollection(BUCKET_NAME, repoCollection)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> collectionResponse = null;
    try{
      collectionResponse = cRepoCollectionServiceImpl.versionCollection(repoCollection);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(repoCollection).getKey();
    verify(contentRepoCollectionDao).versionCollection(BUCKET_NAME, repoCollection);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);

  }

  @Test
  public void deleteCollectionUsingVersionNumbTest() throws IOException {
    when(contentRepoCollectionDao.deleteCollectionUsingVersionNumb(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    boolean deleted = cRepoCollectionServiceImpl.deleteCollectionUsingVersionNumb(KEY, VERSION_NUMBER);

    verify(contentRepoCollectionDao).deleteCollectionUsingVersionNumb(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(httpResponse).close();
    assertTrue(deleted);
  }

  @Test
  public void deleteCollectionUsingVersionNumbThrowsExpTest() throws IOException {
    when(contentRepoCollectionDao.deleteCollectionUsingVersionNumb(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    boolean deleted = false;
    try{
      deleted = cRepoCollectionServiceImpl.deleteCollectionUsingVersionNumb(KEY, VERSION_NUMBER);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoCollectionDao).deleteCollectionUsingVersionNumb(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(httpResponse).close();
    assertFalse(deleted);
  }

  @Test
  public void deleteCollectionUsingVersionCksTest() throws IOException {
    when(contentRepoCollectionDao.deleteCollectionUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    boolean deleted = cRepoCollectionServiceImpl.deleteCollectionUsingVersionCks(KEY, VERSION_CHECKSUM);

    verify(contentRepoCollectionDao).deleteCollectionUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(httpResponse).close();
    assertTrue(deleted);
  }

  @Test
  public void deleteCollectionUsingVersionCksThrowsExpTest() throws IOException {
    when(contentRepoCollectionDao.deleteCollectionUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    boolean deleted = false;
    try{
      deleted = cRepoCollectionServiceImpl.deleteCollectionUsingVersionCks(KEY, VERSION_CHECKSUM);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoCollectionDao).deleteCollectionUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(httpResponse).close();
    assertFalse(deleted);
  }

  @Test
  public void getCollectionUsingVersionCksTest() throws IOException {
    when(contentRepoCollectionDao.getCollectionUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.getCollectionUsingVersionCks(KEY, VERSION_CHECKSUM);

    verify(contentRepoCollectionDao).getCollectionUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionUsingVersionCksThrowsExpTest() throws IOException {
    when(contentRepoCollectionDao.getCollectionUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> collectionResponse = null;

    try{
      collectionResponse = cRepoCollectionServiceImpl.getCollectionUsingVersionCks(KEY, VERSION_CHECKSUM);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoCollectionDao).getCollectionUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);
  }

  @Test
  public void getCollectionUsingVersionNumberTest() throws IOException {
    when(contentRepoCollectionDao.getCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.getCollectionUsingVersionNumber(KEY, VERSION_NUMBER);

    verify(contentRepoCollectionDao).getCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionUsingVersionNumberThrowsExpTest() throws IOException {
    when(contentRepoCollectionDao.getCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> collectionResponse = null;
    try{
      collectionResponse = cRepoCollectionServiceImpl.getCollectionUsingVersionNumber(KEY, VERSION_NUMBER);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }


    verify(contentRepoCollectionDao).getCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);
  }

  @Test
  public void getCollectionUsingTagTest() throws IOException {
    when(contentRepoCollectionDao.getCollectionUsingTag(BUCKET_NAME,KEY, TAG)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.getCollectionUsingTag(KEY, TAG);

    verify(contentRepoCollectionDao).getCollectionUsingTag(BUCKET_NAME,KEY, TAG);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionUsingTagThrowsExpTest() throws IOException {
    when(contentRepoCollectionDao.getCollectionUsingTag(BUCKET_NAME,KEY, TAG)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> collectionResponse = null;
    try{
      collectionResponse = cRepoCollectionServiceImpl.getCollectionUsingTag(KEY, TAG);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoCollectionDao).getCollectionUsingTag(BUCKET_NAME,KEY, TAG);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);
  }

  @Test
  public void getCollectionVersionsTest() throws IOException {
    when(contentRepoCollectionDao.getCollectionVersions(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> collectionResponse = cRepoCollectionServiceImpl.getCollectionVersions(KEY);

    verify(contentRepoCollectionDao).getCollectionVersions(BUCKET_NAME, KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }


  @Test
  public void getCollectionVersionsThrowsExpTest() throws IOException {
    when(contentRepoCollectionDao.getCollectionVersions(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    List<Map<String, Object>> collectionResponse = null;
    try{
      collectionResponse = cRepoCollectionServiceImpl.getCollectionVersions(KEY);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoCollectionDao).getCollectionVersions(BUCKET_NAME, KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);
  }

  @Test
  public void getCollectionsUsingTagTest() throws IOException {
    when(contentRepoCollectionDao.getCollectionsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> collectionResponse = cRepoCollectionServiceImpl.getCollections(OFFSET, LIMIT, true, TAG);

    verify(contentRepoCollectionDao).getCollectionsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionsUsingTagThrowsExcTest() throws IOException {
    when(contentRepoCollectionDao.getCollectionsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    List<Map<String, Object>> collectionResponse = null;
    try{
      collectionResponse = cRepoCollectionServiceImpl.getCollections(OFFSET, LIMIT, true, TAG);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoCollectionDao).getCollectionsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);
  }


  @Test
  public void getCollectionsTest() throws IOException {
    when(contentRepoCollectionDao.getCollections(BUCKET_NAME, OFFSET, LIMIT, true)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> collectionResponse  = cRepoCollectionServiceImpl.getCollections(OFFSET, LIMIT, true, null);

    verify(contentRepoCollectionDao).getCollections(BUCKET_NAME, OFFSET, LIMIT, true);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);

  }

  @Test
  public void getCollectionsThrowsExcTest() throws IOException {
    when(contentRepoCollectionDao.getCollections(BUCKET_NAME, OFFSET, LIMIT, true)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    List<Map<String, Object>> collectionResponse = null;
    try{
      collectionResponse = cRepoCollectionServiceImpl.getCollections(OFFSET, LIMIT, true, null);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoCollectionDao).getCollections(BUCKET_NAME, OFFSET, LIMIT, true);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(collectionResponse);

  }


}