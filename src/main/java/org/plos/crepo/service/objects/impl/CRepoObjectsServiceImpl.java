package org.plos.crepo.service.objects.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.plos.crepo.dao.objects.ContentRepoObjectsDao;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.model.validator.RepoObjectValidator;
import org.plos.crepo.service.objects.CRepoObjectService;
import org.plos.crepo.util.HttpResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Service
public class CRepoObjectsServiceImpl implements CRepoObjectService {

  private static final Logger log = LoggerFactory.getLogger(CRepoObjectsServiceImpl.class);

  private static Gson gson = new Gson();

  @Value("${crepo.bucketName}")
  private String bucketName;

  @Autowired
  private ContentRepoObjectsDao contentRepoObjectsDao;

  @Autowired
  private RepoObjectValidator repoObjectValidator;

  public URL[] getRedirectURL(String key){

    HttpResponse response = contentRepoObjectsDao.getRedirectURL(bucketName, key);
    Header header = response.getFirstHeader("X-Reproxy-URL");

    if (header == null) {
      return new URL[]{};
    }

    return getUrls(header.getValue());

  }

  @Override
  public URL[] getRedirectURL(String key, String versionChecksum){

    Map<String, Object> assetValues = this.getAssetMetaUsingVersionChecksum(key, versionChecksum);
    String paths = (String) assetValues.get("reproxyURL");

    if (StringUtils.isEmpty(paths)){
      return new URL[]{};
    }

    return getUrls(paths);

  }

  private URL[] getUrls(String paths){
    String[] pathArray = paths.split("\\s");

    int pathCount = pathArray.length;
    URL[] urls = new URL[pathCount];

    for(int i = 0; i < pathCount; i++) {
      try {
        urls[i] = new URL(pathArray[i]);
      } catch (MalformedURLException e) {
        log.error("Error trying to get the urls. paths: " + paths + " + repoMessage:  ", e);
        throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingReProxyUrl)
            .baseException(e)
            .build();
      }
    }
    return urls;
  }

  @Override
  public InputStream getLatestAssetInStream(String key){

    HttpResponse response = contentRepoObjectsDao.getLatestAsset(bucketName, key);

    try {
      return response.getEntity().getContent();
    } catch (IOException e) {
      log.error("Error getting the latest asset content from the response. key:  " +  key, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }

  }

  @Override
  public byte[] getLatestAssetByteArray(String key){
    InputStream file = this.getLatestAssetInStream(key);
    try {
      byte[] bytes = IOUtils.toByteArray(file);
      file.close();
      return bytes;
    } catch (IOException e) {
      log.error("Error converting the InputStream in a byte[] getting the latest asset. key " + key, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }

  @Override
  public InputStream getAssetInStreamUsingVersionCks(String key, String versionChecksum) {

    HttpResponse response = contentRepoObjectsDao.getAssetUsingVersionCks(bucketName, key, versionChecksum);
    try {
      return response.getEntity().getContent();
    } catch (IOException e) {
      log.error("Error getting the asset content from the response, when using the version checksum." +
          "  key " + key + " versionNumber: " + versionChecksum, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }

  @Override
  public byte[] getAssetByteArrayUsingVersionCks(String key, String versionChecksum) {
    InputStream file = this.getAssetInStreamUsingVersionCks(key, versionChecksum);
    try {
      byte[] bytes = IOUtils.toByteArray(file);
      file.close();
      return bytes;
    } catch (IOException e) {
      log.error("Error converting the InputStream in a byte[]. key " + key + " versionChecksum: " + versionChecksum, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }


  @Override
  public InputStream getAssetInStreamUsingVersionNum(String key, int versionNumber) {

    HttpResponse response = contentRepoObjectsDao.getAssetUsingVersionNum(bucketName, key, versionNumber);

    try {
      return response.getEntity().getContent();
    } catch (IOException e) {
      log.error(" Error trying to get the content from the response, using version number." +
          " bucketName " + bucketName + " Key: " + key + " versionNumber: " + versionNumber, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }

  @Override
  public byte[] getAssetByteArrayUsingVersionNum(String key, int versionNumber) {
    InputStream file = this.getAssetInStreamUsingVersionNum(key, versionNumber);
    try {
      byte[] bytes = IOUtils.toByteArray(file);
      file.close();
      return bytes;
    } catch (IOException e) {
      log.error("Error getting the content type from file. Key: " + key + " versionNumber " + versionNumber, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }

  @Override
  public Map<String,Object> getAssetMetaLatestVersion(String key) {
    HttpResponse response = contentRepoObjectsDao.getAssetMetaLatestVersion(bucketName, key);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public Map<String,Object> getAssetMetaUsingVersionChecksum(String key, String versionChecksum) {
    HttpResponse response = contentRepoObjectsDao.getAssetMetaUsingVersionChecksum(bucketName, key, versionChecksum);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public Map<String,Object> getAssetMetaUsingVersionNumber(String key, int versionNumber) {
    HttpResponse response = contentRepoObjectsDao.getAssetMetaUsingVersionNumber(bucketName, key, versionNumber);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public List<Map<String, Object>> getAssetVersionsMeta(String key) {
    HttpResponse response = contentRepoObjectsDao.getAssetVersionsMeta(bucketName, key);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {}.getType());
  }


  @Override
  public Boolean deleteLatestAsset(String key) {
    Map<String, Object> asset = this.getAssetMetaLatestVersion(key);
    String versionChecksum = (String) asset.get("versionChecksum");
    contentRepoObjectsDao.deleteAssetUsingVersionCks(bucketName, key, versionChecksum);
    return true;
  }

  @Override
  public Boolean deleteAssetUsingVersionChecksum(String key, String versionChecksum) {
    contentRepoObjectsDao.deleteAssetUsingVersionCks(bucketName, key, versionChecksum);
    return true;
  }

  @Override
  public Boolean deleteAssetUsingVersionNumber(String key, int versionNumber) {
    contentRepoObjectsDao.deleteAssetUsingVersionNumber(bucketName, key, versionNumber);
    return true;
  }

  @Override
  public Map<String, Object> createAsset(RepoObject repoObject) {
    repoObjectValidator.validate(repoObject);
    HttpResponse response = contentRepoObjectsDao.createAsset(bucketName, repoObject, getFileContentType(repoObject, repoObject.getFileContent()));
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public Map<String, Object> versionAsset(RepoObject repoObject) {
    repoObjectValidator.validate(repoObject);
    HttpResponse response = contentRepoObjectsDao.versionAsset(bucketName, repoObject, getFileContentType(repoObject, repoObject.getFileContent()));
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
    }.getType());
  }

  private String getFileContentType(RepoObject repoObject, File file){
    String contentType = repoObject.getContentType();
    if (StringUtils.isEmpty(contentType)){
      try {
        contentType = Files.probeContentType(file.toPath());
      } catch (IOException e) {
        e.printStackTrace();
        log.error("Error getting the content type from file. Key: " + repoObject.getKey(), e);
        throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorAccessingFile)
            .baseException(e)
            .key(repoObject.getKey())
            .build();
      }
    }
    return contentType;
  }

}
