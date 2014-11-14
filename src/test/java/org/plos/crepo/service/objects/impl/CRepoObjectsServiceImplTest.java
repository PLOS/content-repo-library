package org.plos.crepo.service.objects.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.plos.crepo.dao.objects.ContentRepoObjectsDao;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.model.validator.RepoObjectValidator;
import org.plos.crepo.service.BaseServiceTest;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, Gson.class})
public class CRepoObjectsServiceImplTest  extends BaseServiceTest {

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

  @InjectMocks
  private CRepoObjectsServiceImpl cRepoObjectServiceImpl;

  @Mock
  private ContentRepoObjectsDao contentRepoObjectsDao;

  @Mock
  private RepoObjectValidator repoObjectValidator;

  @Before
  public void setUp(){
    cRepoObjectServiceImpl = new CRepoObjectsServiceImpl();
    gson = PowerMockito.mock(Gson.class);
    Whitebox.setInternalState(cRepoObjectServiceImpl, "bucketName", BUCKET_NAME);
    initMocks(this);
  }

  @Test
  public void getRepoObjRedirectURLTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoObjectsDao.getRepoObjMetaLatestVersion(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    when(expectedResponse.get("reproxyURL")).thenReturn(URLS);

    URL[] urls = cRepoObjectServiceImpl.getRepoObjRedirectURL(KEY);

    verify(contentRepoObjectsDao).getRepoObjMetaLatestVersion(BUCKET_NAME, KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(expectedResponse).get("reproxyURL");
    PowerMockito.verifyStatic();

    assertNotNull(urls);
    assertEquals(2, urls.length);
    assertEquals(URL1, urls[0].toString());
    assertEquals(URL2, urls[1].toString());
  }

  @Test
  public void getRepoObjRedirectURLCksTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoObjectsDao.getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);
    when(expectedResponse.get("reproxyURL")).thenReturn(URLS);

    URL[] urls = cRepoObjectServiceImpl.getRepoObjRedirectURL(KEY, VERSION_CHECKSUM);

    verify(contentRepoObjectsDao).getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    verify(expectedResponse).get("reproxyURL");
    PowerMockito.verifyStatic();

    assertNotNull(urls);
    assertEquals(2, urls.length);
    assertEquals(URL1, urls[0].toString());
    assertEquals(URL2, urls[1].toString());
  }

  @Test
  public void getRepoObjMetaLatestVersionTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoObjectsDao.getRepoObjMetaLatestVersion(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.getRepoObjMetaLatestVersion(KEY);

    verify(contentRepoObjectsDao).getRepoObjMetaLatestVersion(BUCKET_NAME, KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getCollectionUsingVersionNumberTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoObjectsDao.getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.getRepoObjMetaUsingVersionChecksum(KEY, VERSION_CHECKSUM);

    verify(contentRepoObjectsDao).getRepoObjMetaUsingVersionChecksum(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();


    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getRepoObjMetaUsingVersionNumTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoObjectsDao.getRepoObjMetaUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.getRepoObjMetaUsingVersionNum(KEY, VERSION_NUMBER);

    verify(contentRepoObjectsDao).getRepoObjMetaUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getRepoObjMetaUsingTagTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoObjectsDao.getRepoObjMetaUsingTag(BUCKET_NAME, KEY, TAG)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.getRepoObjMetaUsingTag(KEY, TAG);

    verify(contentRepoObjectsDao).getRepoObjMetaUsingTag(BUCKET_NAME, KEY, TAG);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getCollectionsUsingTagTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoObjectsDao.getRepoObjVersionsMeta(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    List<Map<String, Object>> objectResponse = cRepoObjectServiceImpl.getRepoObjVersions(KEY);

    verify(contentRepoObjectsDao).getRepoObjVersionsMeta(BUCKET_NAME, KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getObjectsUsingTagTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoObjectsDao.getObjectsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    List<Map<String, Object>> objectResponse = cRepoObjectServiceImpl.getRepoObjects(OFFSET, LIMIT, true, TAG);

    verify(contentRepoObjectsDao).getObjectsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }

  @Test
  public void getObjectsTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoObjectsDao.getObjects(BUCKET_NAME, OFFSET, LIMIT, true)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    List<Map<String, Object>> objectResponse = cRepoObjectServiceImpl.getRepoObjects(OFFSET, LIMIT, true, null);

    verify(contentRepoObjectsDao).getObjects(BUCKET_NAME, OFFSET, LIMIT, true);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);
  }


  @Test
  public void deleteCollectionUsingVersionNumbTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoObjectsDao.deleteRepoObjUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);

    Boolean deleted = cRepoObjectServiceImpl.deleteRepoObjUsingVersionNum(KEY, VERSION_NUMBER);

    verify(contentRepoObjectsDao).deleteRepoObjUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    assertTrue(deleted);
  }

  @Test
  public void deleteRepoObjUsingVersionCksTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoObjectsDao.deleteRepoObjUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);

    Boolean deleted = cRepoObjectServiceImpl.deleteRepoObjUsingVersionCks(KEY, VERSION_CHECKSUM);

    verify(contentRepoObjectsDao).deleteRepoObjUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    assertTrue(deleted);
  }

  @Test
  public void createRepoObjectTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);

    RepoObject repoObject = mock(RepoObject.class);
    doNothing().when(repoObjectValidator).validate(repoObject);
    when(repoObject.getContentType()).thenReturn(CONTENT_TYPE);
    when(contentRepoObjectsDao.createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.createRepoObject(repoObject);

    verify(repoObjectValidator).validate(repoObject);
    verify(repoObject).getContentType();
    verify(contentRepoObjectsDao).createRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);

  }

  @Test
  public void versionRepoObjectTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);

    RepoObject repoObject = mock(RepoObject.class);
    doNothing().when(repoObjectValidator).validate(repoObject);
    when(repoObject.getContentType()).thenReturn(CONTENT_TYPE);
    when(contentRepoObjectsDao.versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(Map.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> objectResponse = cRepoObjectServiceImpl.versionRepoObject(repoObject);

    verify(repoObjectValidator).validate(repoObject);
    verify(repoObject).getContentType();
    verify(contentRepoObjectsDao).versionRepoObj(BUCKET_NAME, repoObject, CONTENT_TYPE);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(objectResponse);
    assertEquals(expectedResponse, objectResponse);

  }


}
