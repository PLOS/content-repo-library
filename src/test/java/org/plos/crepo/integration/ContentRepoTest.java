package org.plos.crepo.integration;

import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.buckets.ContentRepoBucketsDao;
import org.plos.crepo.dao.buckets.impl.ContentRepoBucketDaoImpl;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.identity.RepoVersion;
import org.plos.crepo.model.identity.RepoVersionNumber;
import org.plos.crepo.model.identity.RepoVersionTag;
import org.plos.crepo.model.input.RepoCollection;
import org.plos.crepo.model.input.RepoObject;
import org.plos.crepo.service.ContentRepoServiceImpl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.plos.crepo.service.BaseServiceTest.asRawList;

/**
 * Test the content repo library against a live instance of content-repo.
 * Uncomment @Test and set REPO_SERVER_URL to point the actual content-repo
 */
public class ContentRepoTest {

  private static final String EXCEPTION_EXPECTED = "An exception was expected. ";

  private static final String testData1 = "test data one goes\nhere.";
  private static final String testData2 = "test data two goes\nhere.";

  private static final String repoObjKey1 = "repoObjKey1";
  private static final String repoObjKey2 = "repoObjKey2";
  private static final String repoObjKey3 = "repoObjKey3";
  private static final String repoObjKey4 = "repoObjKey4";
  private static final String repoObjKey5 = "repoObjKey5";
  private static final String repoObjKey6 = "repoObjKey6";
  private static final String repoObjKey7 = "repoObjKey7";
  private static final String repoObjKey8 = "repoObjKey8";
  private static final String repoObjKey9 = "repoObjKey9";
  private static final String repoObjKey10 = "repoObjKey10";

  private static final String collectionKey1 = "collectionKey1";
  private static final String collectionKey2 = "collectionKey2";
  private static final String collectionKey3 = "collectionKey3";

  private static final String TAG = "TEST_TAG";
  private static final String BUCKET_NAME = "TEST_BUCKET";
  private static final String REPO_SERVER_URL = "http://localhost:8080";

  private Timestamp creationDateTime;

  private static ContentRepoServiceImpl contentRepoService;

  private ContentRepoBucketsDao contentRepoDao;

  @BeforeClass
  public static void initialSetUp() {
    ContentRepoAccessConfig config = new ContentRepoAccessConfig(REPO_SERVER_URL, request -> {
      fail("All calls to HttpClientFunction.open in tests should be mocked");
      throw new AssertionError();
    });
    contentRepoService = new ContentRepoServiceImpl(config);

    ContentRepoBucketsDao contentRepoDao = new ContentRepoBucketDaoImpl(config);

    HttpResponse response = null;
    try {
      response = contentRepoDao.getBucket(BUCKET_NAME);
    } catch(ContentRepoException ce){
      // if it does not exist, create it
      contentRepoDao.createBucket(BUCKET_NAME);
    }

  }

  @Before
  public void setUp(){
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis(0);

    cal.set(getRandomNumber(1990, 2013), getRandomNumber(0, 11), getRandomNumber(1, 28), getRandomNumber(0, 59), getRandomNumber(0, 59), getRandomNumber(0, 59));
    creationDateTime = new Timestamp(cal.getTime().getTime());
  }

  private static int getRandomNumber(int Low, int High){
    Random r = new Random();
    return r.nextInt(High-Low) + Low;
  }

  /*@Test*/
  public void objectErrorTest(){

    try{
      contentRepoService.getRepoObjectMetadata(RepoVersion.create(repoObjKey1, "41ff0ad0-3f7e-4fcf-980a-83a336d01624")).getReproxyUrls();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getLatestRepoObject("invalidKey");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObject(RepoVersion.create("invalidKey", "69921e8a-7723-47a0-a25d-18ab0aeef04d"));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObject(new RepoVersionNumber("invalidKey", 0));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getLatestRepoObjectMetadata("invalidKey");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjectMetadata(RepoVersion.create("invalidKey", "e876703d-7c53-4806-96fd-e8d9ebf90d71"));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjectMetadata(new RepoVersionNumber("invalidKey", 0));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjectMetadata(new RepoVersionTag("invalidKey", "tag"));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjectVersions(null);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyKey);
    }

    try{
      contentRepoService.deleteLatestRepoObject("invalidKey");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
    }

    try{
      contentRepoService.deleteRepoObject(RepoVersion.create("invalidKey", "7289a3b1-a96c-4de2-b4b8-034f16988976"));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorDeletingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.deleteRepoObject(new RepoVersionNumber("invalidKey", 0));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorDeletingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try {
      contentRepoService.createRepoObject(new RepoObject.RepoObjectBuilder("").build());
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyKey);
    }

    try {
      contentRepoService.createRepoObject(new RepoObject.RepoObjectBuilder("dsad").build());
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyContent);
    }

    try {
      contentRepoService.createRepoObject(new RepoObject.RepoObjectBuilder("dsad").byteContent(new byte[]{}).build());
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyContentType);
    }

    try {
      contentRepoService.versionRepoObject(new RepoObject.RepoObjectBuilder("").build());
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyKey);
    }

    try {
      contentRepoService.versionRepoObject(new RepoObject.RepoObjectBuilder("dsad").build());
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyContent);
    }

    try {
      contentRepoService.versionRepoObject(new RepoObject.RepoObjectBuilder("dsad").byteContent(new byte[]{}).build());
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyContentType);
    }

    try {
      contentRepoService.versionRepoObject(new RepoObject.RepoObjectBuilder(repoObjKey1).byteContent(new byte[]{}).contentType("text/plain").build());
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorVersioningObject);
      assertTrue(fe.getMessage().contains("does not exist"));
    }

  }

  /*@Test*/
  public void collectionErrorTest(){

    try{
      contentRepoService.createCollection(RepoCollection.create("", null));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyKey);
    }

    try{
      contentRepoService.createCollection(RepoCollection.create("dsakjds", null));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorCreatingCollection);
    }

    try{
      contentRepoService.versionCollection(RepoCollection.create("", null));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyKey);
    }

    try{
      contentRepoService.versionCollection(RepoCollection.create("dsakjds", null));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorVersioningCollection);
    }

    try{
      contentRepoService.getCollection(RepoVersion.create("invalidKey", "365ef3d9-5017-41f8-8ab6-9d2179b40e6d"));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getCollection(new RepoVersionNumber("invalidKey", 0));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getCollection(new RepoVersionTag("invalidKey", "tag"));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getCollectionVersions(null);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyKey);
    }

    try{
      contentRepoService.deleteCollection(RepoVersion.create("invalidKey", "1ae72ceb-fce8-4711-8ed3-f2927848076d"));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorDeletingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.deleteCollection(new RepoVersionNumber("invalidKey", 0));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorDeletingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

  }

  /*@Test*/
  public void bucketErrorTest(){

    try{
      contentRepoService.createBucket("");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorCreatingBucket);
    }

    try{
      contentRepoService.getBucket("dsadas");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingBucketMeta);
    }

    try{
      contentRepoService.getBucket("");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyBucketKey);
    }

    try{
      contentRepoService.getBucket("invalidBucket");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingBucketMeta);
    }

  }

  /*@Test*/
  public void creationAndMetadataTest() {

    File file = null;
    try {
      file = new File("testFile.txt");
      BufferedWriter output = new BufferedWriter(new FileWriter(file));
      output.write(testData1);
      output.close();
    } catch ( IOException e ) {
      e.printStackTrace();
    }

    // create object 1
    RepoObject repoObject = new RepoObject.RepoObjectBuilder(repoObjKey1)
        .creationDate(creationDateTime)
        .fileContent(file)
        .downloadName("dowloadNameTest1")
        .build();

    Map<String, Object> repoObj1 = contentRepoService.createRepoObject(repoObject).getMapView();
    assertNotNull(repoObj1);
    String fileUuid = (String) repoObj1.get("uuid");
    Double versionNumber = (Double) repoObj1.get("versionNumber");

    // version object 1 ---> object 2
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey1)
        .fileContent(file)
        .downloadName("dowloadNameTest2")
        .build();

    Map<String, Object> repoObj2 = contentRepoService.versionRepoObject(repoObject2).getMapView();
    assertNotNull(repoObj2);
    String fileUuid2 = (String) repoObj2.get("uuid");
    Double versionNumber2 = (Double) repoObj2.get("versionNumber");

    //get versions
    List<Map<String, Object>> versions = asRawList(contentRepoService.getRepoObjectVersions(repoObjKey1));
    assertNotNull(versions);
    assertEquals(2, versions.size());
    assertEquals(fileUuid, versions.get(0).get("uuid"));
    assertEquals(fileUuid2, versions.get(1).get("uuid"));

    // get object 1 by version UUID
    Map<String, Object> repoObj3 = contentRepoService.getRepoObjectMetadata(RepoVersion.create(repoObjKey1, fileUuid)).getMapView();
    // get object 1 by version number
    Map<String, Object> repoObj4 = contentRepoService.getRepoObjectMetadata(new RepoVersionNumber(repoObjKey1, versionNumber.intValue())).getMapView();

    assertNotNull(repoObj3);
    assertNotNull(repoObj4);
    assertEquals(repoObj1, repoObj3);
    assertEquals(repoObj3, repoObj4);

    // get latest version with key 'testData1Key' ---> object 2
    Map<String, Object> repoObj5 = contentRepoService.getLatestRepoObjectMetadata(repoObjKey1).getMapView();
    assertNotNull(repoObj5);
    assertEquals(repoObj2, repoObj5);

    // delete using version UUID ---> object 1
    contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey1, fileUuid));

    Map<String, Object> repoObj6 = null;
    try{
      // get object 1 by version UUID ----> must be null
      repoObj6 = contentRepoService.getRepoObjectMetadata(RepoVersion.create(repoObjKey1, fileUuid)).getMapView();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertNull(repoObj6);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    // delete using version number ---> object 2
    contentRepoService.deleteRepoObject(new RepoVersionNumber(repoObjKey1, versionNumber2.intValue()));

    try{
      // get object 2 by version UUID ----> must be null
      repoObj6 = contentRepoService.getRepoObjectMetadata(RepoVersion.create(repoObjKey1, fileUuid2)).getMapView();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertNull(repoObj6);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

  }

  /*@Test*/
  public void autoCreationObjectTest() {

    File file = null;
    try {
      file = new File("testFile.txt");
      BufferedWriter output = new BufferedWriter(new FileWriter(file));
      output.write(testData1);
      output.close();
    } catch ( IOException e ) {
      e.printStackTrace();
    }

    // auto - create object 1
    RepoObject repoObject = new RepoObject.RepoObjectBuilder(repoObjKey10)
        .creationDate(creationDateTime)
        .fileContent(file)
        .downloadName("dowloadNameTest1")
        .build();

    Map<String, Object> repoObj1 = contentRepoService.autoCreateRepoObject(repoObject).getMapView();
    assertNotNull(repoObj1);
    String fileUuid = (String) repoObj1.get("uuid");
    Double versionNumber = (Double) repoObj1.get("versionNumber");

    // auto create object 1, creates new version
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey10)
        .fileContent(file)
        .downloadName("dowloadNameTest2")
        .build();

    Map<String, Object> repoObj2 = contentRepoService.autoCreateRepoObject(repoObject2).getMapView();
    assertNotNull(repoObj2);
    String fileUuid2 = (String) repoObj2.get("uuid");
    Double versionNumber2 = (Double) repoObj2.get("versionNumber");

    assertTrue(versionNumber2 > versionNumber);

    //get versions
    List<Map<String, Object>> versions = asRawList(contentRepoService.getRepoObjectVersions(repoObjKey10));
    assertNotNull(versions);
    assertEquals(2, versions.size());
    assertEquals(fileUuid, versions.get(0).get("uuid"));
    assertEquals(fileUuid2, versions.get(1).get("uuid"));

    // delete using version UUID ---> object 1
    contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey10, fileUuid));

    Map<String, Object> repoObj6 = null;
    try{
      // get object 1 by version UUID ----> must be null
      repoObj6 = contentRepoService.getRepoObjectMetadata(RepoVersion.create(repoObjKey10, fileUuid)).getMapView();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertNull(repoObj6);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    // delete using version number ---> object 2
    contentRepoService.deleteRepoObject(new RepoVersionNumber(repoObjKey10, versionNumber2.intValue()));

    try{
      // get object 2 by version UUID ----> must be null
      repoObj6 = contentRepoService.getRepoObjectMetadata(RepoVersion.create(repoObjKey10, fileUuid2)).getMapView();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertNull(repoObj6);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

  }

  /*@Test*/
  public void hasXProxyAndRedirectUrlTest(){

    assertFalse(contentRepoService.hasXReproxy());

    byte[] content = testData1.getBytes();
    // create object 1
    RepoObject repoObject = new RepoObject.RepoObjectBuilder(repoObjKey2)
        .byteContent(content)
        .creationDate(creationDateTime)
        .downloadName("dowloadNameTest2")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj1 = contentRepoService.createRepoObject(repoObject).getMapView();
    assertNotNull(repoObj1);
    String fileUuid = (String) repoObj1.get("uuid");

    boolean deleted = contentRepoService.deleteLatestRepoObject(repoObjKey2);
    assertTrue(deleted);

    Map<String, Object> repoObj2 = null;
    try{
      // get object 1 by version UUID ----> must be null
      repoObj2 = contentRepoService.getRepoObjectMetadata(RepoVersion.create(repoObjKey2, fileUuid)).getMapView();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertNull(repoObj2);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    repoObject = new RepoObject.RepoObjectBuilder(repoObjKey2)
        .byteContent(content)
        .creationDate(new Timestamp(new Date().getTime()))
        .downloadName("dowloadNameTest3")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj3 = contentRepoService.createRepoObject(repoObject).getMapView();
    assertNotNull(repoObj3);
    String fileUuid3 = (String) repoObj3.get("uuid");

    // version object 1 ---> object 2
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey2)
        .byteContent(content)
        .downloadName("dowloadNameTest4")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj4 = contentRepoService.versionRepoObject(repoObject2).getMapView();
    assertNotNull(repoObj4);
    String fileUuid4 = (String) repoObj4.get("uuid");

    List<URL> url = contentRepoService.getLatestRepoObjectMetadata(repoObjKey2).getReproxyUrls();
    assertEquals(0, url.size());

    url = contentRepoService.getRepoObjectMetadata(RepoVersion.create(repoObjKey2, fileUuid3)).getReproxyUrls();
    assertEquals(0, url.size());

    contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey2, fileUuid3));
    contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey2, fileUuid4));

  }

  /*@Test*/
  public void creationAndContentTest() throws IOException {
    File file = null;
    try {
      file = new File("testFile.txt");
      BufferedWriter output = new BufferedWriter(new FileWriter(file));
      output.write(testData1);
      output.close();
    } catch ( IOException e ) {
      e.printStackTrace();
    }

    // create object 1
    RepoObject repoObject = new RepoObject.RepoObjectBuilder(repoObjKey3)
        .creationDate(creationDateTime)
        .fileContent(file)
        .downloadName("dowloadNameTest5")
        .build();

    Map<String, Object> repoObj1 = contentRepoService.createRepoObject(repoObject).getMapView();
    assertNotNull(repoObj1);
    String fileUuid = (String) repoObj1.get("uuid");
    Double versionNumber = (Double) repoObj1.get("versionNumber");

    InputStream content1 = contentRepoService.getLatestRepoObject(repoObjKey3);
    assertNotNull(content1);

    // version object 1 ---> object 2
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey3)
        .fileContent(file)
        .downloadName("dowloadNameTest6")
        .build();

    Map<String, Object> repoObj2 = contentRepoService.versionRepoObject(repoObject2).getMapView();
    assertNotNull(repoObj2);
    String fileUuid2 = (String) repoObj2.get("uuid");
    Double versionNumber2 = (Double) repoObj2.get("versionNumber");

    InputStream content2 = contentRepoService.getRepoObject(RepoVersion.create(repoObjKey3, fileUuid));
    assertNotNull(content2);

    InputStream content3 = contentRepoService.getRepoObject(new RepoVersionNumber(repoObjKey3, versionNumber.intValue()));
    assertNotNull(content3);

    String fileContent1 = IOUtils.toString(content1, CharEncoding.UTF_8);
    String fileContent2 = IOUtils.toString(content2, CharEncoding.UTF_8);
    String fileContent3 = IOUtils.toString(content3, CharEncoding.UTF_8);

    assertEquals(testData1, fileContent1);
    assertEquals(fileContent1, fileContent2);
    assertEquals(fileContent3, fileContent2);

    byte[] content4 = ByteStreams.toByteArray(contentRepoService.getLatestRepoObject(repoObjKey3));
    byte[] content5 = ByteStreams.toByteArray(contentRepoService.getRepoObject(RepoVersion.create(repoObjKey3, fileUuid2)));
    byte[] content6 = ByteStreams.toByteArray(contentRepoService.getRepoObject(new RepoVersionNumber(repoObjKey3, versionNumber2.intValue())));

    assertNotNull(content4);
    assertNotNull(content5);
    assertNotNull(content6);

    String fileContent4 = new String(content4);
    String fileContent5 = new String(content5);
    String fileContent6 = new String(content6);

    assertEquals(fileContent4,fileContent1);
    assertEquals(fileContent4,fileContent5);
    assertEquals(fileContent4,fileContent6);

    boolean deleted = contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey3, fileUuid));
    assertTrue(deleted);
    deleted = contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey3, fileUuid2));
    assertTrue(deleted);

  }

  /*@Test*/
  public void collectionsTest(){

    byte[] content1 = testData1.getBytes();
    // create object 1
    RepoObject repoObject1 = new RepoObject.RepoObjectBuilder(repoObjKey4)
        .byteContent(content1)
        .downloadName("dowloadNameTest")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj1 = contentRepoService.createRepoObject(repoObject1).getMapView();
    assertNotNull(repoObj1);
    String fileUuid1 = (String) repoObj1.get("uuid");


    byte[] content2 = testData2.getBytes();
    // create object 1
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey5)
        .byteContent(content2)
        .downloadName("dowloadNameTest7")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj2 = contentRepoService.createRepoObject(repoObject2).getMapView();
    assertNotNull(repoObj2);
    String fileUuid2 = (String) repoObj2.get("uuid");

    List<RepoVersion> repoObjs = new ArrayList<RepoVersion>();
    RepoVersion rpa1 = RepoVersion.create(repoObjKey4, fileUuid1);
    repoObjs.add(rpa1);
    RepoVersion rpa2 = RepoVersion.create(repoObjKey5, fileUuid2);
    repoObjs.add(rpa2);

    RepoCollection repoCollMeta1 = RepoCollection.builder()
        .setKey(collectionKey1)
        .setObjects(repoObjs)
        .setCreationDateTime(new Timestamp(new Date().getTime()).toString())
        .build();
    Map<String, Object> collection1 = contentRepoService.createCollection(repoCollMeta1).getMapView();
    assertNotNull(collection1);
    String collUuid1 = (String) collection1.get("uuid");
    Double collVersionNumber1 = (Double) collection1.get("versionNumber");

    repoObjs.remove(1);
    Map<String, Object> collection2 = contentRepoService.versionCollection(repoCollMeta1).getMapView();
    assertNotNull(collection2);
    String collUuid2 = (String) collection2.get("uuid");
    Double collVersionNumber2 = (Double) collection2.get("versionNumber");

    List<Map<String,Object>> versions = asRawList(contentRepoService.getCollectionVersions(collectionKey1));
    assertNotNull(versions);
    assertEquals(2, versions.size());
    assertEquals(collUuid1, versions.get(0).get("uuid"));
    assertEquals(collUuid2, versions.get(1).get("uuid"));

    Map<String, Object> collection3 = contentRepoService.getCollection(RepoVersion.create(collectionKey1, collUuid1)).getMapView();
    Map<String, Object> collection4 = contentRepoService.getCollection(new RepoVersionNumber(collectionKey1, collVersionNumber1.intValue())).getMapView();

    assertNotNull(collection3);
    assertNotNull(collection4);
    assertEquals(collUuid1, collection3.get("uuid"));
    assertEquals(collection3, collection4);

    boolean deleted = contentRepoService.deleteCollection(RepoVersion.create(collectionKey1, collUuid2));
    assertTrue(deleted);

    Map<String, Object> collection5 = null;
    try{
      // get object 2 by version UUID ----> must be null
      collection5 = contentRepoService.getCollection(new RepoVersionNumber(collectionKey1, collVersionNumber2.intValue())).getMapView();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertNull(collection5);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    deleted = contentRepoService.deleteCollection(new RepoVersionNumber(collectionKey1, collVersionNumber1.intValue()));
    assertTrue(deleted);

    Map<String, Object> collection6 = null;
    try{
      // get object 2 by version UUID ----> must be null
      collection6 = contentRepoService.getCollection(RepoVersion.create(collectionKey1, collUuid1)).getMapView();
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertNull(collection6);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey4, fileUuid1));
    contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey5, fileUuid2));

  }

  /*@Test*/
  public void repoConfigTest(){

    Map<String, Object> repoConfig = contentRepoService.getRepoConfig();
    assertNotNull(repoConfig.get("version"));
    assertNotNull(repoConfig.get("objectStoreBackend"));
    assertNotNull(repoConfig.get("sqlServiceBackend"));
    assertNotNull(repoConfig.get("hasXReproxy"));

    Map<String, Object> repoStatus = contentRepoService.getRepoStatus();
    assertNotNull(repoStatus.get("bucketCount"));
    assertNotNull(repoStatus.get("serviceStarted"));
    assertNotNull(repoStatus.get("readsSinceStart"));
    assertNotNull(repoStatus.get("writesSinceStart"));

  }

  /*@Test*/
  public void collectionsTagTest(){

    byte[] content1 = testData1.getBytes();
    // create object 1
    RepoObject repoObject1 = new RepoObject.RepoObjectBuilder(repoObjKey6)
        .byteContent(content1)
        .downloadName("dowloadNameTest")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj1 = contentRepoService.createRepoObject(repoObject1).getMapView();
    assertNotNull(repoObj1);
    String fileUuid1 = (String) repoObj1.get("uuid");


    byte[] content2 = testData2.getBytes();
    // create object 1
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey7)
        .byteContent(content2)
        .downloadName("dowloadNameTest7")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj2 = contentRepoService.createRepoObject(repoObject2).getMapView();
    assertNotNull(repoObj2);
    String fileUuid2 = (String) repoObj2.get("uuid");

    List<RepoVersion> repoObjs = new ArrayList<RepoVersion>();
    RepoVersion rpa1 = RepoVersion.create(repoObjKey6, fileUuid1);
    repoObjs.add(rpa1);
    RepoVersion rpa2 = RepoVersion.create(repoObjKey7, fileUuid2);
    repoObjs.add(rpa2);

    RepoCollection repoCollMeta1 = RepoCollection.builder()
        .setKey(collectionKey2)
        .setObjects(repoObjs)
        .setCreationDateTime(new Timestamp(new Date().getTime()).toString())
        .setTag(TAG)
        .build();
    Map<String, Object> collection1 = contentRepoService.createCollection(repoCollMeta1).getMapView();
    assertNotNull(collection1);
    String collUuid1 = (String) collection1.get("uuid");

    repoObjs.remove(1);
    repoCollMeta1 = RepoCollection.builder()
        .setKey(collectionKey2)
        .setObjects(repoObjs)
        .setCreationDateTime(new Timestamp(new Date().getTime()).toString())
        .setTag("")
        .build();
    Map<String, Object> collection2 = contentRepoService.versionCollection(repoCollMeta1).getMapView();
    assertNotNull(collection2);
    String collUuid2 = (String) collection2.get("uuid");

    Map<String, Object> collection3 = contentRepoService.getCollection(new RepoVersionTag(collectionKey2, TAG)).getMapView();
    assertNotNull(collection3);
    assertEquals(collUuid1, collection3.get("uuid"));

    repoCollMeta1 = RepoCollection.builder()
        .setKey(collectionKey2)
        .setObjects(repoObjs)
        .setCreationDateTime(new Timestamp(new Date().getTime()).toString())
        .setTag(TAG)
        .build();
    Map<String, Object> collection4 = contentRepoService.versionCollection(repoCollMeta1).getMapView();
    assertNotNull(collection4);
    String collUuid4 = (String) collection4.get("uuid");

    RepoCollection repoCollMeta2 = RepoCollection.builder()
        .setKey(collectionKey3)
        .setObjects(repoObjs)
        .setCreationDateTime(new Timestamp(new Date().getTime()).toString())
        .setTag(TAG)
        .build();
    Map<String, Object> collection5 = contentRepoService.createCollection(repoCollMeta2).getMapView();
    assertNotNull(collection5);
    String collUuid5 = (String) collection5.get("uuid");

    List<Map<String, Object>> collections = asRawList(contentRepoService.getCollections(0, 10, false, null));
    assertNotNull(collections);
    assertEquals(4, collections.size());

    List<Map<String, Object>> collectionsTag = asRawList(contentRepoService.getCollections(0, 10, false, TAG));
    assertNotNull(collectionsTag);
    assertEquals(3, collectionsTag.size());

    contentRepoService.deleteCollection(RepoVersion.create(collectionKey2, collUuid1));
    contentRepoService.deleteCollection(RepoVersion.create(collectionKey2, collUuid2));
    contentRepoService.deleteCollection(RepoVersion.create(collectionKey2, collUuid4));
    contentRepoService.deleteCollection(RepoVersion.create(collectionKey3, collUuid5));

    contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey6, fileUuid1));
    contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey7, fileUuid2));

  }

  /*@Test*/
  public void repoObjectsTagTest(){

    byte[] content1 = testData1.getBytes();
    RepoObject repoObject1 = new RepoObject.RepoObjectBuilder(repoObjKey8)
        .byteContent(content1)
        .tag(TAG)
        .downloadName("dowloadNameTest")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj1 = contentRepoService.createRepoObject(repoObject1).getMapView();
    assertNotNull(repoObj1);
    String fileUuid1 = (String) repoObj1.get("uuid");

    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey8)
        .byteContent(content1)
        .downloadName("dowloadNameTest1")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj2 = contentRepoService.versionRepoObject(repoObject2).getMapView();
    assertNotNull(repoObj2);
    String fileUuid2 = (String) repoObj2.get("uuid");

    byte[] content2 = testData2.getBytes();
    // create object
    RepoObject repoObject3 = new RepoObject.RepoObjectBuilder(repoObjKey9)
        .byteContent(content2)
        .tag(TAG)
        .downloadName("dowloadNameTest3")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj3 = contentRepoService.createRepoObject(repoObject3).getMapView();
    assertNotNull(repoObj3);
    String fileUuid3 = (String) repoObj3.get("uuid");

    Map<String, Object> repoObj4 = contentRepoService.getRepoObjectMetadata(new RepoVersionTag(repoObjKey8, TAG)).getMapView();
    assertNotNull(repoObj4);
    assertEquals(fileUuid1, repoObj4.get("uuid"));


    List<Map<String, Object>> repoObjectsTag =  asRawList(contentRepoService.getRepoObjects(0, 10, false, TAG));
    assertNotNull(repoObjectsTag);
    assertEquals(2, repoObjectsTag.size());
    for (Map<String, Object> repoObject : repoObjectsTag){
      assertEquals(TAG, repoObject.get("tag"));
    }

    List<Map<String, Object>> repoObjects =  asRawList(contentRepoService.getRepoObjects(0, 10, false, null));
    assertNotNull(repoObjects);
    assertEquals(3, repoObjects.size());

    contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey8, fileUuid1));
    contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey8, fileUuid2));
    contentRepoService.deleteRepoObject(RepoVersion.create(repoObjKey9, fileUuid3));

  }

}




