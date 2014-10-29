package org.plos.crepo.integration;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.plos.crepo.Application;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoCollectionObject;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.service.contentRepo.impl.ContentRepoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ContentRepoTest {

  private static final String testData1 = "test data one goes\nhere.";
  private static final String testData2 = "test data two goes\nhere.";

  private static final String assetKey1 = "assetKey1";
  private static final String assetKey2 = "assetKey2";
  private static final String assetKey3 = "assetKey3";
  private static final String assetKey4 = "assetKey4";
  private static final String assetKey5 = "assetKey5";
  private static final String assetKey6 = "assetKey6";
  private static final String assetKey7 = "assetKey7";

  private static final String collectionKey1 = "collectionKey1";
  private static final String collectionKey2 = "collectionKey2";
  private static final String collectionKey3 = "collectionKey3";

  private static final String TAG = "TEST_TAG";

  private Timestamp creationDateTime;

  @Autowired
  private ContentRepoServiceImpl contentRepoService;

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

  @Test
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
    RepoObject repoObject = new RepoObject.RepoObjectBuilder(assetKey1)
        .creationDate(creationDateTime)
        .fileContent(file)
        .downloadName("dowloadNameTest1")
        .build();

    Map<String, Object> asset1 = contentRepoService.createAsset(repoObject);
    assertNotNull(asset1);
    String fileVersionChecksum = (String) asset1.get("versionChecksum");
    Double versionNumber = (Double) asset1.get("versionNumber");

    // version object 1 ---> object 2
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(assetKey1)
        .fileContent(file)
        .downloadName("dowloadNameTest2")
        .build();

    Map<String, Object> asset2 = contentRepoService.versionAsset(repoObject2);
    assertNotNull(asset2);
    String fileVersionChecksum2 = (String) asset2.get("versionChecksum");
    Double versionNumber2 = (Double) asset2.get("versionNumber");

    //get versions
    List<Map<String, Object>> versions = contentRepoService.getAssetVersionsMeta(assetKey1);
    assertNotNull(versions);
    assertEquals(2, versions.size());
    assertEquals(fileVersionChecksum, versions.get(0).get("versionChecksum"));
    assertEquals(fileVersionChecksum2, versions.get(1).get("versionChecksum"));

    // get object 1 by version checksum
    Map<String, Object> asset3 = contentRepoService.getAssetMetaUsingVersionChecksum(assetKey1, fileVersionChecksum);
    // get object 1 by version number
    Map<String, Object> asset4 = contentRepoService.getAssetMetaUsingVersionNumber(assetKey1, versionNumber.intValue());

    assertNotNull(asset3);
    assertNotNull(asset4);
    assertEquals(asset1, asset3);
    assertEquals(asset3, asset4);

    // get latest version with key 'testData1Key' ---> object 2
    Map<String, Object> asset5 = contentRepoService.getAssetMetaLatestVersion(assetKey1);
    assertNotNull(asset5);
    assertEquals(asset2, asset5);

    // delete using version checksum ---> object 1
    contentRepoService.deleteAssetUsingVersionChecksum(assetKey1, fileVersionChecksum);

    Map<String, Object> asset6 = null;
    try{
      // get object 1 by version checksum ----> must be null
      asset6 = contentRepoService.getAssetMetaUsingVersionChecksum(assetKey1, fileVersionChecksum);
      fail("An exception was expected. ");
    } catch(ContentRepoException fe){
      assertNull(asset6);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    // delete using version number ---> object 2
    contentRepoService.deleteAssetUsingVersionNumber(assetKey1, versionNumber2.intValue());

    try{
      // get object 2 by version checksum ----> must be null
      asset6 = contentRepoService.getAssetMetaUsingVersionChecksum(assetKey1, fileVersionChecksum2);
      fail("An exception was expected. ");
    } catch(ContentRepoException fe){
      assertNull(asset6);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

  }

  @Test
  public void hasXProxyAndRedirectUrlTest(){

    assertFalse(contentRepoService.hasXReproxy());

    byte[] content = testData1.getBytes();
    // create object 1
    RepoObject repoObject = new RepoObject.RepoObjectBuilder(assetKey2)
        .byteContent(content)
        .creationDate(creationDateTime)
        .downloadName("dowloadNameTest2")
        .contentType("test/plain")
        .build();

    Map<String, Object> asset1 = contentRepoService.createAsset(repoObject);
    assertNotNull(asset1);
    String fileVersionChecksum = (String) asset1.get("versionChecksum");

    Boolean deleted = contentRepoService.deleteLatestAsset(assetKey2);
    assertTrue(deleted);

    Map<String, Object> asset2 = null;
    try{
      // get object 1 by version checksum ----> must be null
      asset2 = contentRepoService.getAssetMetaUsingVersionChecksum(assetKey2, fileVersionChecksum);
      fail("An exception was expected. ");
    } catch(ContentRepoException fe){
      assertNull(asset2);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingObjectMeta);
      assertTrue(fe.getMessage().contains("not found"));
    }

    repoObject = new RepoObject.RepoObjectBuilder(assetKey2)
        .byteContent(content)
        .creationDate(new Timestamp(new Date().getTime()))
        .downloadName("dowloadNameTest3")
        .contentType("test/plain")
        .build();

    Map<String, Object> asset3 = contentRepoService.createAsset(repoObject);
    assertNotNull(asset3);
    String fileVersionChecksum3 = (String) asset3.get("versionChecksum");

    // version object 1 ---> object 2
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(assetKey2)
        .byteContent(content)
        .downloadName("dowloadNameTest4")
        .contentType("test/plain")
        .build();

    Map<String, Object> asset4 = contentRepoService.versionAsset(repoObject2);
    assertNotNull(asset4);
    String fileVersionChecksum4 = (String) asset4.get("versionChecksum");

    URL[] url = contentRepoService.getRedirectURL(assetKey2);
    assertEquals(0, url.length);

    url = contentRepoService.getRedirectURL(assetKey2, fileVersionChecksum3);
    assertEquals(0, url.length);

    contentRepoService.deleteAssetUsingVersionChecksum(assetKey2, fileVersionChecksum3);
    contentRepoService.deleteAssetUsingVersionChecksum(assetKey2, fileVersionChecksum4);

  }

  @Test
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
    RepoObject repoObject = new RepoObject.RepoObjectBuilder(assetKey3)
        .creationDate(creationDateTime)
        .fileContent(file)
        .downloadName("dowloadNameTest5")
        .build();

    Map<String, Object> asset1 = contentRepoService.createAsset(repoObject);
    assertNotNull(asset1);
    String fileVersionChecksum = (String) asset1.get("versionChecksum");
    Double versionNumber = (Double) asset1.get("versionNumber");

    InputStream content1 = contentRepoService.getLatestAssetInStream(assetKey3);
    assertNotNull(content1);

    // version object 1 ---> object 2
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(assetKey3)
        .fileContent(file)
        .downloadName("dowloadNameTest6")
        .build();

    Map<String, Object> asset2 = contentRepoService.versionAsset(repoObject2);
    assertNotNull(asset2);
    String fileVersionChecksum2 = (String) asset2.get("versionChecksum");
    Double versionNumber2 = (Double) asset2.get("versionNumber");

    InputStream content2 = contentRepoService.getAssetInStreamUsingVersionCks(assetKey3, fileVersionChecksum);
    assertNotNull(content2);

    InputStream content3 = contentRepoService.getAssetInStreamUsingVersionNum(assetKey3, versionNumber.intValue());
    assertNotNull(content3);

    String fileContent1 = IOUtils.toString(content1, CharEncoding.UTF_8);
    String fileContent2 = IOUtils.toString(content2, CharEncoding.UTF_8);
    String fileContent3 = IOUtils.toString(content3, CharEncoding.UTF_8);

    assertEquals(testData1, fileContent1);
    assertEquals(fileContent1, fileContent2);
    assertEquals(fileContent3, fileContent2);

    byte[] content4 = contentRepoService.getLatestAssetByteArray(assetKey3);
    byte[] content5 = contentRepoService.getAssetByteArrayUsingVersionCks(assetKey3, fileVersionChecksum2);
    byte[] content6 = contentRepoService.getAssetByteArrayUsingVersionNum(assetKey3, versionNumber2.intValue());

    assertNotNull(content4);
    assertNotNull(content5);
    assertNotNull(content6);

    String fileContent4 = new String(content4);
    String fileContent5 = new String(content5);
    String fileContent6 = new String(content6);

    assertEquals(fileContent4,fileContent1);
    assertEquals(fileContent4,fileContent5);
    assertEquals(fileContent4,fileContent6);

    Boolean deleted = contentRepoService.deleteAssetUsingVersionChecksum(assetKey3, fileVersionChecksum);
    assertTrue(deleted);
    deleted = contentRepoService.deleteAssetUsingVersionChecksum(assetKey3, fileVersionChecksum2);
    assertTrue(deleted);

  }

  @Test
  public void collectionsTest(){

    byte[] content1 = testData1.getBytes();
    // create object 1
    RepoObject repoObject1 = new RepoObject.RepoObjectBuilder(assetKey4)
        .byteContent(content1)
        .downloadName("dowloadNameTest")
        .contentType("test/plain")
        .build();

    Map<String, Object> asset1 = contentRepoService.createAsset(repoObject1);
    assertNotNull(asset1);
    String fileVersionChecksum1 = (String) asset1.get("versionChecksum");


    byte[] content2 = testData2.getBytes();
    // create object 1
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(assetKey5)
        .byteContent(content2)
        .downloadName("dowloadNameTest7")
        .contentType("test/plain")
        .build();

    Map<String, Object> asset2 = contentRepoService.createAsset(repoObject2);
    assertNotNull(asset1);
    String fileVersionChecksum2 = (String) asset2.get("versionChecksum");

    List<RepoCollectionObject> assets = new ArrayList<RepoCollectionObject>();
    RepoCollectionObject rpa1 = new RepoCollectionObject(assetKey4, fileVersionChecksum1);
    assets.add(rpa1);
    RepoCollectionObject rpa2 = new RepoCollectionObject(assetKey5, fileVersionChecksum2);
    assets.add(rpa2);

    RepoCollection repoCollMeta1 = new RepoCollection(collectionKey1, assets);
    repoCollMeta1.setCreationDateTime(new Timestamp(new Date().getTime()).toString());
    Map<String, Object> collection1 = contentRepoService.createCollection(repoCollMeta1);
    assertNotNull(collection1);
    String collVersionChecksum1 = (String) collection1.get("versionChecksum");
    Double collVersionNumber1 = (Double) collection1.get("versionNumber");

    assets.remove(1);
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
      fail("An exception was expected. ");
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
      fail("An exception was expected. ");
    } catch(ContentRepoException fe){
      assertNull(collection6);
      assertEquals(fe.getErrorType(), ErrorType.ErrorFetchingCollection);
      assertTrue(fe.getMessage().contains("not found"));
    }

    contentRepoService.deleteAssetUsingVersionChecksum(assetKey4, fileVersionChecksum1);
    contentRepoService.deleteAssetUsingVersionChecksum(assetKey5, fileVersionChecksum2);

  }

  @Test
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

  @Test
  public void collectionsTagTest(){

    byte[] content1 = testData1.getBytes();
    // create object 1
    RepoObject repoObject1 = new RepoObject.RepoObjectBuilder(assetKey6)
        .byteContent(content1)
        .downloadName("dowloadNameTest")
        .contentType("test/plain")
        .build();

    Map<String, Object> asset1 = contentRepoService.createAsset(repoObject1);
    assertNotNull(asset1);
    String fileVersionChecksum1 = (String) asset1.get("versionChecksum");


    byte[] content2 = testData2.getBytes();
    // create object 1
    RepoObject repoObject2 = new RepoObject.RepoObjectBuilder(assetKey7)
        .byteContent(content2)
        .downloadName("dowloadNameTest7")
        .contentType("test/plain")
        .build();

    Map<String, Object> asset2 = contentRepoService.createAsset(repoObject2);
    assertNotNull(asset1);
    String fileVersionChecksum2 = (String) asset2.get("versionChecksum");

    List<RepoCollectionObject> assets = new ArrayList<RepoCollectionObject>();
    RepoCollectionObject rpa1 = new RepoCollectionObject(assetKey6, fileVersionChecksum1);
    assets.add(rpa1);
    RepoCollectionObject rpa2 = new RepoCollectionObject(assetKey7, fileVersionChecksum2);
    assets.add(rpa2);

    RepoCollection repoCollMeta1 = new RepoCollection(collectionKey2, assets);
    repoCollMeta1.setCreationDateTime(new Timestamp(new Date().getTime()).toString());
    repoCollMeta1.setTag(TAG);
    Map<String, Object> collection1 = contentRepoService.createCollection(repoCollMeta1);
    assertNotNull(collection1);
    String collVersionChecksum1 = (String) collection1.get("versionChecksum");

    assets.remove(1);
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

    RepoCollection repoCollMeta2 = new RepoCollection(collectionKey3, assets);
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

    contentRepoService.deleteAssetUsingVersionChecksum(assetKey6, fileVersionChecksum1);
    contentRepoService.deleteAssetUsingVersionChecksum(assetKey7, fileVersionChecksum2);

  }

}




