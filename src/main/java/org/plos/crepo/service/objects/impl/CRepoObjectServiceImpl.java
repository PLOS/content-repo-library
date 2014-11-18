package org.plos.crepo.service.objects.impl;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.objects.ContentRepoObjectDao;
import org.plos.crepo.dao.objects.impl.ContentRepoObjectDaoImpl;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.model.validator.RepoObjectValidator;
import org.plos.crepo.service.objects.CRepoObjectService;
import org.plos.crepo.util.HttpResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Service
public class CRepoObjectServiceImpl implements CRepoObjectService {

  private static final Logger log = LoggerFactory.getLogger(CRepoObjectServiceImpl.class);

  private final Gson gson;
  private final ContentRepoAccessConfig accessConfig;
  private final ContentRepoObjectDao contentRepoObjectDao;

  public CRepoObjectServiceImpl(ContentRepoAccessConfig accessConfig, ContentRepoObjectDao contentRepoObjectDao) {
    this.accessConfig = Preconditions.checkNotNull(accessConfig);
    this.contentRepoObjectDao = contentRepoObjectDao;
    gson = new Gson();
  }

  public URL[] getRepoObjRedirectURL(String key) {
    validateObjectKey(key);
    return getUrlsFromMeta(this.getRepoObjMetaLatestVersion(key));
  }

  @Override
  public URL[] getRepoObjRedirectURL(String key, String versionChecksum) {
    validateObjectKey(key);
    validateObjectCks(versionChecksum);
    return getUrlsFromMeta(this.getRepoObjMetaUsingVersionChecksum(key, versionChecksum));
  }

  private URL[] getUrlsFromMeta(Map<String, Object> repoObjValues) {
    String paths = (String) repoObjValues.get("reproxyURL");

    if (StringUtils.isEmpty(paths)) {
      return new URL[]{};
    }

    return getUrls(paths);
  }

  private URL[] getUrls(String paths) {
    String[] pathArray = paths.split("\\s");

    int pathCount = pathArray.length;
    URL[] urls = new URL[pathCount];

    for (int i = 0; i < pathCount; i++) {
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
  public InputStream getLatestRepoObjStream(String key) {
    validateObjectKey(key);
    HttpResponse response = contentRepoObjectDao.getLatestRepoObj(accessConfig.getBucketName(), key);

    try {
      return response.getEntity().getContent();
    } catch (IOException e) {
      log.error("Error getting the latest repoObj content from the response. key:  " + key, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }

  }

  @Override
  public byte[] getLatestRepoObjByteArray(String key) {
    InputStream file = this.getLatestRepoObjStream(key);
    try {
      byte[] bytes = IOUtils.toByteArray(file);
      file.close();
      return bytes;
    } catch (IOException e) {
      log.error("Error converting the InputStream in a byte[] getting the latest repoObj. key " + key, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }

  @Override
  public InputStream getRepoObjStreamUsingVersionCks(String key, String versionChecksum) {
    validateObjectKey(key);
    validateObjectCks(versionChecksum);
    HttpResponse response = contentRepoObjectDao.getRepoObjUsingVersionCks(accessConfig.getBucketName(), key, versionChecksum);
    try {
      return response.getEntity().getContent();
    } catch (IOException e) {
      log.error("Error getting the repoObj content from the response, when using the version checksum." +
          "  key " + key + " versionNumber: " + versionChecksum, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }

  @Override
  public byte[] getRepoObjByteArrayUsingVersionCks(String key, String versionChecksum) {
    InputStream file = this.getRepoObjStreamUsingVersionCks(key, versionChecksum);
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
  public InputStream getRepoObjStreamUsingVersionNum(String key, int versionNumber) {
    validateObjectKey(key);
    HttpResponse response = contentRepoObjectDao.getRepoObjUsingVersionNum(accessConfig.getBucketName(), key, versionNumber);

    try {
      return response.getEntity().getContent();
    } catch (IOException e) {
      log.error(" Error trying to get the content from the response, using version number." +
          " accessConfig.getBucketName() " + accessConfig.getBucketName() + " Key: " + key + " versionNumber: " + versionNumber, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingObject)
          .baseException(e)
          .key(key)
          .build();
    }
  }

  @Override
  public byte[] getRepoObjByteArrayUsingVersionNum(String key, int versionNumber) {
    InputStream file = this.getRepoObjStreamUsingVersionNum(key, versionNumber);
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
  public Map<String, Object> getRepoObjMetaLatestVersion(String key) {
    validateObjectKey(key);
    HttpResponse response = contentRepoObjectDao.getRepoObjMetaLatestVersion(accessConfig.getBucketName(), key);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public Map<String, Object> getRepoObjMetaUsingVersionChecksum(String key, String versionChecksum) {
    validateObjectKey(key);
    validateObjectCks(versionChecksum);
    HttpResponse response = contentRepoObjectDao.getRepoObjMetaUsingVersionChecksum(accessConfig.getBucketName(), key, versionChecksum);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public Map<String, Object> getRepoObjMetaUsingVersionNum(String key, int versionNumber) {
    validateObjectKey(key);
    HttpResponse response = contentRepoObjectDao.getRepoObjMetaUsingVersionNumber(accessConfig.getBucketName(), key, versionNumber);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public Map<String, Object> getRepoObjMetaUsingTag(String key, String tag) {
    validateObjectKey(key);
    validateObjectTag(tag);
    HttpResponse response = contentRepoObjectDao.getRepoObjMetaUsingTag(accessConfig.getBucketName(), key, tag);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public List<Map<String, Object>> getRepoObjVersions(String key) {
    validateObjectKey(key);
    HttpResponse response = contentRepoObjectDao.getRepoObjVersionsMeta(accessConfig.getBucketName(), key);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {}.getType());
  }


  @Override
  public Boolean deleteLatestRepoObj(String key) {
    validateObjectKey(key);
    Map<String, Object> repoObj = this.getRepoObjMetaLatestVersion(key);
    String versionChecksum = (String) repoObj.get("versionChecksum");
    contentRepoObjectDao.deleteRepoObjUsingVersionCks(accessConfig.getBucketName(), key, versionChecksum);
    return true;
  }

  @Override
  public Boolean deleteRepoObjUsingVersionCks(String key, String versionChecksum) {
    validateObjectKey(key);
    validateObjectCks(versionChecksum);
    contentRepoObjectDao.deleteRepoObjUsingVersionCks(accessConfig.getBucketName(), key, versionChecksum);
    return true;
  }

  @Override
  public Boolean deleteRepoObjUsingVersionNum(String key, int versionNumber) {
    validateObjectKey(key);
    contentRepoObjectDao.deleteRepoObjUsingVersionNumber(accessConfig.getBucketName(), key, versionNumber);
    return true;
  }

  @Override
  public Map<String, Object> createRepoObject(RepoObject repoObject) {
    RepoObjectValidator.validate(repoObject);
    HttpResponse response = contentRepoObjectDao.createRepoObj(accessConfig.getBucketName(), repoObject, getFileContentType(repoObject, repoObject.getFileContent()));
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public Map<String, Object> versionRepoObject(RepoObject repoObject) {
    RepoObjectValidator.validate(repoObject);
    HttpResponse response = contentRepoObjectDao.versionRepoObj(accessConfig.getBucketName(), repoObject, getFileContentType(repoObject, repoObject.getFileContent()));
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public List<Map<String, Object>> getRepoObjects(int offset, int limit, boolean includeDeleted, String tag) {
    HttpResponse response = null;
    if (StringUtils.isEmpty(tag)) {
      response = contentRepoObjectDao.getObjects(accessConfig.getBucketName(), offset, limit, includeDeleted);
    } else {
      response = contentRepoObjectDao.getObjectsUsingTag(accessConfig.getBucketName(), offset, limit, includeDeleted, tag);
    }
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {}.getType());
  }

  private String getFileContentType(RepoObject repoObject, File file) {
    String contentType = repoObject.getContentType();
    if (StringUtils.isEmpty(contentType)) {
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

  private void validateObjectKey(String key) {
    if (StringUtils.isEmpty(key)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyObjectKey)
          .build();
    }
  }

  private void validateObjectCks(String versionChecksum) {
    if (StringUtils.isEmpty(versionChecksum)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyObjectCks)
          .build();
    }
  }

  private void validateObjectTag(String tag) {
    if (StringUtils.isEmpty(tag)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyObjectTag)
          .build();
    }
  }

}
