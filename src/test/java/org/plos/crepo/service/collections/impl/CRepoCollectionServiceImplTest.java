package org.plos.crepo.service.collections.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.plos.crepo.dao.collections.ContentRepoCollectionsDao;
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.service.BaseServiceTest;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpResponseUtil.class, Gson.class})
public class CRepoCollectionServiceImplTest extends BaseServiceTest{

  private static final String VERSION_CHECKSUM = "EWQW432423FSDF235CFDSW";
  private static final String BUCKET_NAME = "bucketName";
  private static final int VERSION_NUMBER = 0;
  private static final String TAG = "tag";
  private static final int OFFSET = 0;
  private static final int LIMIT = 10;

  @InjectMocks
  private CRepoCollectionServiceImpl cRepoCollectionServiceImpl;

  @Mock
  private ContentRepoCollectionsDao contentRepoCollectionsDao;

  @Before
  public void setUp(){
    cRepoCollectionServiceImpl = new CRepoCollectionServiceImpl();
    gson = PowerMockito.mock(Gson.class);
    Whitebox.setInternalState(cRepoCollectionServiceImpl, "bucketName", BUCKET_NAME);
    initMocks(this);
  }

  @Test
  public void createCollectionTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    RepoCollection repoCollection = mock(RepoCollection.class);
    when(repoCollection.getKey()).thenReturn("key");
    when(contentRepoCollectionsDao.createCollection(BUCKET_NAME, repoCollection)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.createCollection(repoCollection);

    verify(repoCollection).getKey();
    verify(contentRepoCollectionsDao).createCollection(BUCKET_NAME, repoCollection);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void versionCollectionTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    RepoCollection repoCollection = mock(RepoCollection.class);
    when(repoCollection.getKey()).thenReturn("key");
    when(contentRepoCollectionsDao.versionCollection(BUCKET_NAME, repoCollection)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.versionCollection(repoCollection);

    verify(repoCollection).getKey();
    verify(contentRepoCollectionsDao).versionCollection(BUCKET_NAME, repoCollection);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void deleteCollectionUsingVersionNumbTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoCollectionsDao.deleteCollectionUsingVersionNumb(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);

    Boolean deleted = cRepoCollectionServiceImpl.deleteCollectionUsingVersionNumb(KEY, VERSION_NUMBER);

    verify(contentRepoCollectionsDao).deleteCollectionUsingVersionNumb(BUCKET_NAME, KEY, VERSION_NUMBER);
    assertTrue(deleted);
  }

  @Test
  public void deleteCollectionUsingVersionCksTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoCollectionsDao.deleteCollectionUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);

    Boolean deleted = cRepoCollectionServiceImpl.deleteCollectionUsingVersionCks(KEY, VERSION_CHECKSUM);

    verify(contentRepoCollectionsDao).deleteCollectionUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    assertTrue(deleted);
  }

  @Test
  public void getCollectionUsingVersionCksTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoCollectionsDao.getCollectionUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.getCollectionUsingVersionCks(KEY, VERSION_CHECKSUM);

    verify(contentRepoCollectionsDao).getCollectionUsingVersionCks(BUCKET_NAME, KEY, VERSION_CHECKSUM);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionUsingVersionNumberTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoCollectionsDao.getCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.getCollectionUsingVersionNumber(KEY, VERSION_NUMBER);

    verify(contentRepoCollectionsDao).getCollectionUsingVersionNumber(BUCKET_NAME, KEY, VERSION_NUMBER);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();


    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionUsingTagTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoCollectionsDao.getCollectionUsingTag(BUCKET_NAME,KEY, TAG)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    Map<String,Object> expectedResponse = mock(HashMap.class);
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    Map<String, Object> collectionResponse = cRepoCollectionServiceImpl.getCollectionUsingTag(KEY, TAG);

    verify(contentRepoCollectionsDao).getCollectionUsingTag(BUCKET_NAME,KEY, TAG);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionVersionsTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoCollectionsDao.getCollectionVersions(BUCKET_NAME, KEY)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    List<Map<String, Object>> collectionResponse = cRepoCollectionServiceImpl.getCollectionVersions(KEY);

    verify(contentRepoCollectionsDao).getCollectionVersions(BUCKET_NAME, KEY);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionsUsingTagTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoCollectionsDao.getCollectionsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    List<Map<String, Object>> collectionResponse = cRepoCollectionServiceImpl.getCollections(OFFSET, LIMIT, true, TAG);

    verify(contentRepoCollectionsDao).getCollectionsUsingTag(BUCKET_NAME, OFFSET, LIMIT, true, TAG);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

  @Test
  public void getCollectionsTest(){
    HttpResponse httpResponse = mock(HttpResponse.class);
    when(contentRepoCollectionsDao.getCollections(BUCKET_NAME, OFFSET, LIMIT, true)).thenReturn(httpResponse);
    mockStatics(httpResponse);

    List<Map<String,Object>> expectedResponse = mock(List.class);
    Type type = new TypeToken<List<Map<String, Object>>>() {
    }.getType();
    when(gson.fromJson(eq(JSON_MSG), eq(type))).thenReturn(expectedResponse);

    List<Map<String, Object>> collectionResponse = cRepoCollectionServiceImpl.getCollections(OFFSET, LIMIT, true, null);

    verify(contentRepoCollectionsDao).getCollections(BUCKET_NAME, OFFSET, LIMIT, true);
    verify(gson).fromJson(eq(JSON_MSG), eq(type));
    PowerMockito.verifyStatic();

    assertNotNull(collectionResponse);
    assertEquals(expectedResponse, collectionResponse);
  }

}
