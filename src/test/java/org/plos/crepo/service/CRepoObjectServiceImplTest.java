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
import org.plos.crepo.dao.objects.ContentRepoObjectDao;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.model.RepoObjectVersion;
import org.plos.crepo.model.RepoObjectVersionNumber;
import org.plos.crepo.model.RepoObjectVersionTag;
import org.plos.crepo.model.validator.RepoObjectValidator;
import org.plos.crepo.service.BaseServiceTest;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, Gson.class, RepoObjectValidator.class})
public class CRepoObjectServiceImplTest extends BaseServiceTest {

  private static final String VERSION_CHECKSUM = "EWQW432423FSDF235CFDSW";
  private static final String BUCKET_NAME = "bucketName";
  private static final int VERSION_NUMBER = 0;
  private static final String TAG = "tag";
  private static final int OFFSET = 0;
  private static final int LIMIT = 10;
  private static final String URL1 = "http://url1";
  private static final String URL2 = "http://url1";
  private static final String CONTENT_TYPE = "text/plain";
  private String URLS = URL1 + " " + URL2;

  private ContentRepoService cRepoObjectServiceImpl;

  @Mock
  private ContentRepoObjectDao contentRepoObjectDao;

  @Mock
  private RepoObjectValidator repoObjectValidator;

  @Mock
  private ContentRepoAccessConfig repoAccessConfig;

  @Before
  public void setUp(){
    gson = PowerMockito.mock(Gson.class);
    cRepoObjectServiceImpl = new TestContentRepoServiceBuilder()
        .setAccessConfig(repoAccessConfig)
        .setObjectDao(contentRepoObjectDao)
        .build();
    Whitebox.setInternalState(cRepoObjectServiceImpl, "gson", gson);
    when(repoAccessConfig.getBucketName()).thenReturn(BUCKET_NAME);

  }

  @Test
  public void getRepoObjRedirectURLTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaLatestVersion(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    when(expectedResponse.get("reproxyURL")).thenReturn(URLS);
    Mockito.doNothing().when(httpResponse).close();

    List<URL> urls = cRepoObjectServiceImpl.getRepoObjRedirectURL(KEY);

    verify(contentRepoObjectDao).getRepoObjMetaLatestVersion(BUCKET_NAME, KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(expectedResponse).get("reproxyURL");
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(urls);
    assertEquals(2, urls.size());
    assertEquals(URL1, urls.get(0).toString());
    assertEquals(URL2, urls.get(1).toString());
  }

  @Test
  public void getRepoObjRedirectURLThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaLatestVersion(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    List<URL> urls = null;
    try{
      urls = cRepoObjectServiceImpl.getRepoObjRedirectURL(KEY);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).getRepoObjMetaLatestVersion(BUCKET_NAME, KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(urls);

  }

  @Test
  public void getRepoObjRedirectURLCksTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    when(expectedResponse.get("reproxyURL")).thenReturn(URLS);
    Mockito.doNothing().when(httpResponse).close();

    List<URL> urls = cRepoObjectServiceImpl.getRepoObjRedirectURL(RepoObjectVersion.createFromHex(KEY, VERSION_CHECKSUM));

    verify(contentRepoObjectDao).getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(expectedResponse).get("reproxyURL");
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(urls);
    assertEquals(2, urls.size());
    assertEquals(URL1, urls.get(0).toString());
    assertEquals(URL2, urls.get(1).toString());
  }

  @Test
  public void getRepoObjRedirectURLCksThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    List<URL> urls = null;
    try{
      urls = cRepoObjectServiceImpl.getRepoObjRedirectURL(RepoObjectVersion.createFromHex(KEY, VERSION_CHECKSUM));
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(urls);

  }

  @Test
  public void getRepoObjMetaLatestVersionTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaLatestVersion(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.getRepoObjMetaLatestVersion(KEY);

    verify(contentRepoObjectDao).getRepoObjMetaLatestVersion(BUCKET_NAME, KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getRepoObjMetaLatestVersionThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaLatestVersion(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> objectResponse = null;
    try{
      objectResponse = cRepoObjectServiceImpl.getRepoObjMetaLatestVersion(KEY);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).getRepoObjMetaLatestVersion(BUCKET_NAME, KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void getObjectMetaUsingVersionCksTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.getRepoObjMeta(RepoObjectVersion.createFromHex(KEY, VERSION_CHECKSUM));

    verify(contentRepoObjectDao).getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getObjectMetaUsingVersionCksThrowsTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> objectResponse = null;
    try{
      objectResponse = cRepoObjectServiceImpl.getRepoObjMeta(RepoObjectVersion.createFromHex(KEY, VERSION_CHECKSUM));
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void getRepoObjMetaUsingVersionNumTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.getRepoObjMeta(new RepoObjectVersionNumber(KEY, VERSION_NUMBER));

    verify(contentRepoObjectDao).getRepoObjMetaUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getRepoObjMetaUsingVersionNumThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> objectResponse = null;
    try{
      objectResponse = cRepoObjectServiceImpl.getRepoObjMeta(new RepoObjectVersionNumber(KEY, VERSION_NUMBER));
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).getRepoObjMetaUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void getRepoObjMetaUsingTagTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaUsingTag(BUCKET_NAME, KEY, TAG)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.getRepoObjMeta(new RepoObjectVersionTag(KEY, TAG));

    verify(contentRepoObjectDao).getRepoObjMetaUsingTag(BUCKET_NAME, KEY, TAG);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getRepoObjMetaUsingTagThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjMetaUsingTag(BUCKET_NAME, KEY, TAG)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> objectResponse = null;
    try{
      objectResponse = cRepoObjectServiceImpl.getRepoObjMeta(new RepoObjectVersionTag(KEY, TAG));
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).getRepoObjMetaUsingTag(BUCKET_NAME, KEY, TAG);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void getObjVersionsTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjVersionsMeta(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> objectResponse = cRepoObjectServiceImpl.getRepoObjVersions(KEY);

    verify(contentRepoObjectDao).getRepoObjVersionsMeta(BUCKET_NAME, KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getObjVersionsThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getRepoObjVersionsMeta(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    List<Map<String, Object>> objectResponse = null;
    try{
      objectResponse = cRepoObjectServiceImpl.getRepoObjVersions(KEY);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).getRepoObjVersionsMeta(BUCKET_NAME, KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void getObjectsUsingTagTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getObjectsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> objectResponse = cRepoObjectServiceImpl.getRepoObjects(OFFSET, LIMIT, true, TAG);

    verify(contentRepoObjectDao).getObjectsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getObjectsUsingTagThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getObjectsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    List<Map<String, Object>> objectResponse = null;
    try{
      objectResponse = cRepoObjectServiceImpl.getRepoObjects(OFFSET, LIMIT, true, TAG);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).getObjectsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }

  @Test
  public void getObjectsTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getObjects(BUCKET_NAME, OFFSET, LIMIT, true)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    List<Map<String, Object>> objectResponse = cRepoObjectServiceImpl.getRepoObjects(OFFSET, LIMIT, true, null);

    verify(contentRepoObjectDao).getObjects(BUCKET_NAME, OFFSET, LIMIT, true);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getObjectsThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.getObjects(BUCKET_NAME, OFFSET, LIMIT, true)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    List<Map<String, Object>> objectResponse = null;
    try{
      objectResponse = cRepoObjectServiceImpl.getRepoObjects(OFFSET, LIMIT, true, null);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).getObjects(BUCKET_NAME, OFFSET, LIMIT, true);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);
  }


  @Test
  public void deleteCollectionUsingVersionNumbTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.deleteRepoObjUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    boolean deleted = cRepoObjectServiceImpl.deleteRepoObj(new RepoObjectVersionNumber(KEY, VERSION_NUMBER));

    verify(contentRepoObjectDao).deleteRepoObjUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    assertTrue(deleted);
  }

  @Test
  public void deleteCollectionUsingVersionNumbThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.deleteRepoObjUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    boolean deleted = false;
    try{
      deleted = cRepoObjectServiceImpl.deleteRepoObj(new RepoObjectVersionNumber(KEY, VERSION_NUMBER));
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).deleteRepoObjUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    assertFalse(deleted);
  }

  @Test
  public void deleteRepoObjUsingVersionCksTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.deleteRepoObjUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);
    Mockito.doNothing().when(httpResponse).close();

    boolean deleted = cRepoObjectServiceImpl.deleteRepoObj(RepoObjectVersion.createFromHex(KEY, VERSION_CHECKSUM));

    verify(contentRepoObjectDao).deleteRepoObjUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(httpResponse).close();
    assertTrue(deleted);
  }

  @Test
  public void deleteRepoObjUsingVersionCksThrowsExcTest() throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    when(contentRepoObjectDao.deleteRepoObjUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    boolean deleted = false;
    try{
      deleted = cRepoObjectServiceImpl.deleteRepoObj(RepoObjectVersion.createFromHex(KEY, VERSION_CHECKSUM));
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(contentRepoObjectDao).deleteRepoObjUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(httpResponse).close();
    assertFalse(deleted);
  }

  @Test
  public void createRepoObjectTest() throws Exception {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);

    RepoObject repoObject = mock(RepoObject.class);
    PowerMockito.mockStatic(RepoObjectValidator.class);
    PowerMockito.doNothing().when(RepoObjectValidator.class, "validate", repoObject);
    when(repoObject.getContentType()).thenReturn(CONTENT_TYPE);
    when(contentRepoObjectDao.createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.createRepoObject(repoObject);

    verify(repoObject).getContentType();
    verify(contentRepoObjectDao).createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
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
    when(repoObject.getContentType()).thenReturn(CONTENT_TYPE);
    when(contentRepoObjectDao.createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> objectResponse = null;
    try{
      objectResponse = cRepoObjectServiceImpl.createRepoObject(repoObject);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(repoObject).getContentType();
    verify(contentRepoObjectDao).createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);

  }

  @Test
  public void versionRepoObjectTest() throws Exception {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);

    RepoObject repoObject = mock(RepoObject.class);
    PowerMockito.mockStatic(RepoObjectValidator.class);
    PowerMockito.doNothing().when(RepoObjectValidator.class, "validate", repoObject);
    when(repoObject.getContentType()).thenReturn(CONTENT_TYPE);
    when(contentRepoObjectDao.versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    Mockito.doNothing().when(httpResponse).close();

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.versionRepoObject(repoObject);

    verify(repoObject).getContentType();
    verify(contentRepoObjectDao).versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
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
    when(repoObject.getContentType()).thenReturn(CONTENT_TYPE);
    when(contentRepoObjectDao.versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    IOException expectedException = mock(IOException.class);
    Mockito.doThrow(expectedException).when(httpResponse).close();

    Map<String, Object> objectResponse = null;
    try{
      objectResponse = cRepoObjectServiceImpl.versionRepoObject(repoObject);
    } catch(ContentRepoException exception){
      assertEquals(ErrorType.ServerError, exception.getErrorType());
      assertEquals(expectedException, exception.getCause());
    }

    verify(repoObject).getContentType();
    verify(contentRepoObjectDao).versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(httpResponse).close();
    PowerMockito.verifyStatic();

    assertNull(objectResponse);

  }


}
