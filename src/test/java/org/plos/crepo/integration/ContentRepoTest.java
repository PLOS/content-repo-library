package org.plos.crepo.integration;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.plos.crepo.config.BasicContentRepoAccessConfig;
import org.plos.crepo.dao.buckets.ContentRepoBucketsDao;
import org.plos.crepo.dao.buckets.impl.ContentRepoBucketDaoImpl;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoCollectionObject;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.service.ContentRepoServiceImpl;

import java.io.*;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;

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
    BasicContentRepoAccessConfig.Builder configBuilder = BasicContentRepoAccessConfig.builder();
    configBuilder.setRepoServer(REPO_SERVER_URL);
    configBuilder.setBucketName(BUCKET_NAME);

    BasicContentRepoAccessConfig config = configBuilder.build();
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
      contentRepoService.getRepoObjRedirectURL(repoObjKey1, "gdsfds");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getLatestRepoObjStream("invalidKey");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getLatestRepoObjByteArray("invalidKey");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjStreamUsingVersionCks("invalidKey", "bdjksabdaks");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjByteArrayUsingVersionCks(repoObjKey1, "fdsafds");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjStreamUsingVersionNum("invalidKey", 0);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjByteArrayUsingVersionNum("invalidKey", 0);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjMetaLatestVersion("invalidKey");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjMetaUsingVersionChecksum("invalidKey", "dsaa232");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjMetaUsingVersionNum("invalidKey", 0);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjMetaUsingTag("invalidKey", "tag");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getRepoObjVersions(null);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyObjectKey);
    }

    try{
      contentRepoService.deleteLatestRepoObj("invalidKey");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
    }

    try{
      contentRepoService.deleteRepoObjUsingVersionCks("invalidKey", "dsadas");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorDeletingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.deleteRepoObjUsingVersionNum("invalidKey", 0);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorDeletingObject);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try {
      contentRepoService.createRepoObject(new RepoObject.RepoObjectBuilder("").build());
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyObjectKey);
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
      assertEquals(fe.getErrorType(), ErrorType.EmptyObjectKey);
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
      contentRepoService.createCollection(new RepoCollection("", null));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyCollectionKey);
    }

    try{
      contentRepoService.createCollection(new RepoCollection("dsakjds", null));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorCreatingCollection);
    }

    try{
      contentRepoService.versionCollection(new RepoCollection("", null));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyCollectionKey);
    }

    try{
      contentRepoService.versionCollection(new RepoCollection("dsakjds", null));
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorVersioningCollection);
    }

    try{
      contentRepoService.getCollectionUsingVersionCks("invalidKey", "dsaa232");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getCollectionUsingVersionNumber("invalidKey", 0);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getCollectionUsingTag("invalidKey", "tag");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.getCollectionVersions(null);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.EmptyCollectionKey);
    }

    try{
      contentRepoService.deleteCollectionUsingVersionCks("invalidKey", "dsadas");
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertEquals(fe.getErrorType(), ErrorType.ErrorDeletingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    try{
      contentRepoService.deleteCollectionUsingVersionNumb("invalidKey", 0);
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

    Map<String, Object> repoObj1 = contentRepoService.createRepoObject(repoObject);
    assertNotNull(repoObj1);
    String fileVersionChecksum = (String) repoObj1.get("versionChecksum");
    Double versionNumber = (Double) repoObj1.get("versionNumber");

    // version object 1 ---> object 2
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey1)
        .fileContent(file)
        .downloadName("dowloadNameTest2")
        .build();

    Map<String, Object> repoObj2 = contentRepoService.versionRepoObject(repoObject2);
    assertNotNull(repoObj2);
    String fileVersionChecksum2 = (String) repoObj2.get("versionChecksum");
    Double versionNumber2 = (Double) repoObj2.get("versionNumber");

    //get versions
    List<Map<String, Object>> versions = contentRepoService.getRepoObjVersions(repoObjKey1);
    assertNotNull(versions);
    assertEquals(2, versions.size());
    assertEquals(fileVersionChecksum, versions.get(0).get("versionChecksum"));
    assertEquals(fileVersionChecksum2, versions.get(1).get("versionChecksum"));

    // get object 1 by version checksum
    Map<String, Object> repoObj3 = contentRepoService.getRepoObjMetaUsingVersionChecksum(repoObjKey1, fileVersionChecksum);
    // get object 1 by version number
    Map<String, Object> repoObj4 = contentRepoService.getRepoObjMetaUsingVersionNum(repoObjKey1, versionNumber.intValue());

    assertNotNull(repoObj3);
    assertNotNull(repoObj4);
    assertEquals(repoObj1, repoObj3);
    assertEquals(repoObj3, repoObj4);

    // get latest version with key 'testData1Key' ---> object 2
    Map<String, Object> repoObj5 = contentRepoService.getRepoObjMetaLatestVersion(repoObjKey1);
    assertNotNull(repoObj5);
    assertEquals(repoObj2, repoObj5);

    // delete using version checksum ---> object 1
    contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey1, fileVersionChecksum);

    Map<String, Object> repoObj6 = null;
    try{
      // get object 1 by version checksum ----> must be null
      repoObj6 = contentRepoService.getRepoObjMetaUsingVersionChecksum(repoObjKey1, fileVersionChecksum);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertNull(repoObj6);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    // delete using version number ---> object 2
    contentRepoService.deleteRepoObjUsingVersionNum(repoObjKey1, versionNumber2.intValue());

    try{
      // get object 2 by version checksum ----> must be null
      repoObj6 = contentRepoService.getRepoObjMetaUsingVersionChecksum(repoObjKey1, fileVersionChecksum2);
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

    Map<String, Object> repoObj1 = contentRepoService.autoCreateRepoObject(repoObject);
    assertNotNull(repoObj1);
    String fileVersionChecksum = (String) repoObj1.get("versionChecksum");
    Double versionNumber = (Double) repoObj1.get("versionNumber");

    // auto create object 1, creates new version
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey10)
        .fileContent(file)
        .downloadName("dowloadNameTest2")
        .build();

    Map<String, Object> repoObj2 = contentRepoService.autoCreateRepoObject(repoObject2);
    assertNotNull(repoObj2);
    String fileVersionChecksum2 = (String) repoObj2.get("versionChecksum");
    Double versionNumber2 = (Double) repoObj2.get("versionNumber");

    assertTrue(versionNumber2 > versionNumber);

    //get versions
    List<Map<String, Object>> versions = contentRepoService.getRepoObjVersions(repoObjKey10);
    assertNotNull(versions);
    assertEquals(2, versions.size());
    assertEquals(fileVersionChecksum, versions.get(0).get("versionChecksum"));
    assertEquals(fileVersionChecksum2, versions.get(1).get("versionChecksum"));

    // delete using version checksum ---> object 1
    contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey10, fileVersionChecksum);

    Map<String, Object> repoObj6 = null;
    try{
      // get object 1 by version checksum ----> must be null
      repoObj6 = contentRepoService.getRepoObjMetaUsingVersionChecksum(repoObjKey10, fileVersionChecksum);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertNull(repoObj6);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    // delete using version number ---> object 2
    contentRepoService.deleteRepoObjUsingVersionNum(repoObjKey10, versionNumber2.intValue());

    try{
      // get object 2 by version checksum ----> must be null
      repoObj6 = contentRepoService.getRepoObjMetaUsingVersionChecksum(repoObjKey10, fileVersionChecksum2);
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

    Map<String, Object> repoObj1 = contentRepoService.createRepoObject(repoObject);
    assertNotNull(repoObj1);
    String fileVersionChecksum = (String) repoObj1.get("versionChecksum");

    Boolean deleted = contentRepoService.deleteLatestRepoObj(repoObjKey2);
    assertTrue(deleted);

    Map<String, Object> repoObj2 = null;
    try{
      // get object 1 by version checksum ----> must be null
      repoObj2 = contentRepoService.getRepoObjMetaUsingVersionChecksum(repoObjKey2, fileVersionChecksum);
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

    Map<String, Object> repoObj3 = contentRepoService.createRepoObject(repoObject);
    assertNotNull(repoObj3);
    String fileVersionChecksum3 = (String) repoObj3.get("versionChecksum");

    // version object 1 ---> object 2
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey2)
        .byteContent(content)
        .downloadName("dowloadNameTest4")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj4 = contentRepoService.versionRepoObject(repoObject2);
    assertNotNull(repoObj4);
    String fileVersionChecksum4 = (String) repoObj4.get("versionChecksum");

    URL[] url = contentRepoService.getRepoObjRedirectURL(repoObjKey2);
    assertEquals(0, url.length);

    url = contentRepoService.getRepoObjRedirectURL(repoObjKey2, fileVersionChecksum3);
    assertEquals(0, url.length);

    contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey2, fileVersionChecksum3);
    contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey2, fileVersionChecksum4);

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

    Map<String, Object> repoObj1 = contentRepoService.createRepoObject(repoObject);
    assertNotNull(repoObj1);
    String fileVersionChecksum = (String) repoObj1.get("versionChecksum");
    Double versionNumber = (Double) repoObj1.get("versionNumber");

    InputStream content1 = contentRepoService.getLatestRepoObjStream(repoObjKey3);
    assertNotNull(content1);

    // version object 1 ---> object 2
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey3)
        .fileContent(file)
        .downloadName("dowloadNameTest6")
        .build();

    Map<String, Object> repoObj2 = contentRepoService.versionRepoObject(repoObject2);
    assertNotNull(repoObj2);
    String fileVersionChecksum2 = (String) repoObj2.get("versionChecksum");
    Double versionNumber2 = (Double) repoObj2.get("versionNumber");

    InputStream content2 = contentRepoService.getRepoObjStreamUsingVersionCks(repoObjKey3, fileVersionChecksum);
    assertNotNull(content2);

    InputStream content3 = contentRepoService.getRepoObjStreamUsingVersionNum(repoObjKey3, versionNumber.intValue());
    assertNotNull(content3);

    String fileContent1 = IOUtils.toString(content1, CharEncoding.UTF_8);
    String fileContent2 = IOUtils.toString(content2, CharEncoding.UTF_8);
    String fileContent3 = IOUtils.toString(content3, CharEncoding.UTF_8);

    assertEquals(testData1, fileContent1);
    assertEquals(fileContent1, fileContent2);
    assertEquals(fileContent3, fileContent2);

    byte[] content4 = contentRepoService.getLatestRepoObjByteArray(repoObjKey3);
    byte[] content5 = contentRepoService.getRepoObjByteArrayUsingVersionCks(repoObjKey3, fileVersionChecksum2);
    byte[] content6 = contentRepoService.getRepoObjByteArrayUsingVersionNum(repoObjKey3, versionNumber2.intValue());

    assertNotNull(content4);
    assertNotNull(content5);
    assertNotNull(content6);

    String fileContent4 = new String(content4);
    String fileContent5 = new String(content5);
    String fileContent6 = new String(content6);

    assertEquals(fileContent4,fileContent1);
    assertEquals(fileContent4,fileContent5);
    assertEquals(fileContent4,fileContent6);

    Boolean deleted = contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey3, fileVersionChecksum);
    assertTrue(deleted);
    deleted = contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey3, fileVersionChecksum2);
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

    Map<String, Object> repoObj1 = contentRepoService.createRepoObject(repoObject1);
    assertNotNull(repoObj1);
    String fileVersionChecksum1 = (String) repoObj1.get("versionChecksum");


    byte[] content2 = testData2.getBytes();
    // create object 1
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey5)
        .byteContent(content2)
        .downloadName("dowloadNameTest7")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj2 = contentRepoService.createRepoObject(repoObject2);
    assertNotNull(repoObj2);
    String fileVersionChecksum2 = (String) repoObj2.get("versionChecksum");

    List<RepoCollectionObject> repoObjs = new ArrayList<RepoCollectionObject>();
    RepoCollectionObject rpa1 = new RepoCollectionObject(repoObjKey4, fileVersionChecksum1);
    repoObjs.add(rpa1);
    RepoCollectionObject rpa2 = new RepoCollectionObject(repoObjKey5, fileVersionChecksum2);
    repoObjs.add(rpa2);

    RepoCollection repoCollMeta1 = new RepoCollection(collectionKey1, repoObjs);
    repoCollMeta1.setCreationDateTime(new Timestamp(new Date().getTime()).toString());
    Map<String, Object> collection1 = contentRepoService.createCollection(repoCollMeta1);
    assertNotNull(collection1);
    String collVersionChecksum1 = (String) collection1.get("versionChecksum");
    Double collVersionNumber1 = (Double) collection1.get("versionNumber");

    repoObjs.remove(1);
    Map<String, Object> collection2 = contentRepoService.versionCollection(repoCollMeta1);
    assertNotNull(collection2);
    String collVersionChecksum2 = (String) collection2.get("versionChecksum");
    Double collVersionNumber2 = (Double) collection2.get("versionNumber");

    List<Map<String,Object>> versions = contentRepoService.getCollectionVersions(collectionKey1);
    assertNotNull(versions);
    assertEquals(2, versions.size());
    assertEquals(collVersionChecksum1, versions.get(0).get("versionChecksum"));
    assertEquals(collVersionChecksum2, versions.get(1).get("versionChecksum"));

    Map<String, Object> collection3 = contentRepoService.getCollectionUsingVersionCks(collectionKey1, collVersionChecksum1);
    Map<String, Object> collection4 = contentRepoService.getCollectionUsingVersionNumber(collectionKey1, collVersionNumber1.intValue());

    assertNotNull(collection3);
    assertNotNull(collection4);
    assertEquals(collVersionChecksum1, collection3.get("versionChecksum"));
    assertEquals(collection3, collection4);

    Boolean deleted = contentRepoService.deleteCollectionUsingVersionCks(collectionKey1, collVersionChecksum2);
    assertTrue(deleted);

    Map<String, Object> collection5 = null;
    try{
      // get object 2 by version checksum ----> must be null
      collection5 = contentRepoService.getCollectionUsingVersionNumber(collectionKey1, collVersionNumber2.intValue());
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertNull(collection5);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    deleted = contentRepoService.deleteCollectionUsingVersionNumb(collectionKey1, collVersionNumber1.intValue());
    assertTrue(deleted);

    Map<String, Object> collection6 = null;
    try{
      // get object 2 by version checksum ----> must be null
      collection6 = contentRepoService.getCollectionUsingVersionCks(collectionKey1, collVersionChecksum1);
      fail(EXCEPTION_EXPECTED);
    } catch(ContentRepoException fe){
      assertNull(collection6);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey4, fileVersionChecksum1);
    contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey5, fileVersionChecksum2);

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

    Map<String, Object> repoObj1 = contentRepoService.createRepoObject(repoObject1);
    assertNotNull(repoObj1);
    String fileVersionChecksum1 = (String) repoObj1.get("versionChecksum");


    byte[] content2 = testData2.getBytes();
    // create object 1
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey7)
        .byteContent(content2)
        .downloadName("dowloadNameTest7")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj2 = contentRepoService.createRepoObject(repoObject2);
    assertNotNull(repoObj2);
    String fileVersionChecksum2 = (String) repoObj2.get("versionChecksum");

    List<RepoCollectionObject> repoObjs = new ArrayList<RepoCollectionObject>();
    RepoCollectionObject rpa1 = new RepoCollectionObject(repoObjKey6, fileVersionChecksum1);
    repoObjs.add(rpa1);
    RepoCollectionObject rpa2 = new RepoCollectionObject(repoObjKey7, fileVersionChecksum2);
    repoObjs.add(rpa2);

    RepoCollection repoCollMeta1 = new RepoCollection(collectionKey2, repoObjs);
    repoCollMeta1.setCreationDateTime(new Timestamp(new Date().getTime()).toString());
    repoCollMeta1.setTag(TAG);
    Map<String, Object> collection1 = contentRepoService.createCollection(repoCollMeta1);
    assertNotNull(collection1);
    String collVersionChecksum1 = (String) collection1.get("versionChecksum");

    repoObjs.remove(1);
    repoCollMeta1.setTag("");
    Map<String, Object> collection2 = contentRepoService.versionCollection(repoCollMeta1);
    assertNotNull(collection2);
    String collVersionChecksum2 = (String) collection2.get("versionChecksum");

    Map<String, Object> collection3 = contentRepoService.getCollectionUsingTag(collectionKey2, TAG);
    assertNotNull(collection3);
    assertEquals(collVersionChecksum1, collection3.get("versionChecksum"));

    repoCollMeta1.setTag(TAG);
    Map<String, Object> collection4 = contentRepoService.versionCollection(repoCollMeta1);
    assertNotNull(collection4);
    String collVersionChecksum4 = (String) collection4.get("versionChecksum");

    RepoCollection repoCollMeta2 = new RepoCollection(collectionKey3, repoObjs);
    repoCollMeta2.setCreationDateTime(new Timestamp(new Date().getTime()).toString());
    repoCollMeta2.setTag(TAG);
    Map<String, Object> collection5 = contentRepoService.createCollection(repoCollMeta2);
    assertNotNull(collection5);
    String collVersionChecksum5 = (String) collection5.get("versionChecksum");

    List<Map<String, Object>> collections = contentRepoService.getCollections(0, 10, false, null);
    assertNotNull(collections);
    assertEquals(4, collections.size());

    List<Map<String, Object>> collectionsTag = contentRepoService.getCollections(0, 10, false, TAG);
    assertNotNull(collectionsTag);
    assertEquals(3, collectionsTag.size());

    contentRepoService.deleteCollectionUsingVersionCks(collectionKey2, collVersionChecksum1);
    contentRepoService.deleteCollectionUsingVersionCks(collectionKey2, collVersionChecksum2);
    contentRepoService.deleteCollectionUsingVersionCks(collectionKey2, collVersionChecksum4);
    contentRepoService.deleteCollectionUsingVersionCks(collectionKey3, collVersionChecksum5);

    contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey6, fileVersionChecksum1);
    contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey7, fileVersionChecksum2);

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

    Map<String, Object> repoObj1 = contentRepoService.createRepoObject(repoObject1);
    assertNotNull(repoObj1);
    String fileVersionChecksum1 = (String) repoObj1.get("versionChecksum");

    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(repoObjKey8)
        .byteContent(content1)
        .downloadName("dowloadNameTest1")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj2 = contentRepoService.versionRepoObject(repoObject2);
    assertNotNull(repoObj2);
    String fileVersionChecksum2 = (String) repoObj2.get("versionChecksum");

    byte[] content2 = testData2.getBytes();
    // create object
    RepoObject repoObject3 = new RepoObject.RepoObjectBuilder(repoObjKey9)
        .byteContent(content2)
        .tag(TAG)
        .downloadName("dowloadNameTest3")
        .contentType("test/plain")
        .build();

    Map<String, Object> repoObj3 = contentRepoService.createRepoObject(repoObject3);
    assertNotNull(repoObj3);
    String fileVersionChecksum3 = (String) repoObj3.get("versionChecksum");

    Map<String, Object> repoObj4 = contentRepoService.getRepoObjMetaUsingTag(repoObjKey8, TAG);
    assertNotNull(repoObj4);
    assertEquals(fileVersionChecksum1, repoObj4.get("versionChecksum"));


    List<Map<String, Object>> repoObjectsTag =  contentRepoService.getRepoObjects(0,10,false,TAG);
    assertNotNull(repoObjectsTag);
    assertEquals(2, repoObjectsTag.size());
    for (Map<String, Object> repoObject : repoObjectsTag){
      assertEquals(TAG, repoObject.get("tag"));
    }

    List<Map<String, Object>> repoObjects =  contentRepoService.getRepoObjects(0,10,false,null);
    assertNotNull(repoObjects);
    assertEquals(3, repoObjects.size());

    contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey8, fileVersionChecksum1);
    contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey8, fileVersionChecksum2);
    contentRepoService.deleteRepoObjUsingVersionCks(repoObjKey9, fileVersionChecksum3);

  }

}




