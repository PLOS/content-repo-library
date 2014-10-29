package org.plos.crepo.dao.objects.impl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.plos.crepo.dao.ContentRepoBaseDao;
import org.plos.crepo.dao.objects.ContentRepoObjectsDao;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.CreationMethod;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.util.ObjectUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ContentRepoObjectsDaoImpl extends ContentRepoBaseDao implements ContentRepoObjectsDao{

  private static final Logger log = LoggerFactory.getLogger(ContentRepoObjectsDaoImpl.class);

  @Autowired
  private ObjectUrlGenerator objectUrlGenerator;

  @Override
  public HttpResponse getLatestAsset(String bucketName, String key){

    HttpGet request = new HttpGet(objectUrlGenerator.getLatestObjectUrl(repoServer, bucketName, key));
    return executeRequest(request, ErrorType.ErrorFetchingObject);

  }

  @Override
  public HttpResponse getAssetUsingVersionCks(String bucketName, String key, String versionChecksum) {

    HttpGet request = new HttpGet(objectUrlGenerator.getObjectUsingVersionCksUrl(repoServer, bucketName, key, versionChecksum));
    return executeRequest(request, ErrorType.ErrorFetchingObject);

  }

  @Override
  public HttpResponse getAssetUsingVersionNum(String bucketName, String key, int versionNumber) {

    HttpGet request = new HttpGet(objectUrlGenerator.getObjectUsingVersionNumUrl(repoServer, bucketName, key, versionNumber));
    return executeRequest(request, ErrorType.ErrorFetchingObject);

  }

  @Override
  public HttpResponse getAssetMetaLatestVersion(String bucketName, String key) {
    HttpGet request = new HttpGet(objectUrlGenerator.getLatestObjectMetaUrl(repoServer, bucketName, key));
    return executeRequest(request, ErrorType.ErrorFetchingObjectMeta);
  }

  @Override
  public HttpResponse getAssetMetaUsingVersionChecksum(String bucketName, String key, String versionChecksum) {
    HttpGet request = new HttpGet(objectUrlGenerator.getObjectMetaUsingVersionCksUrl(repoServer, bucketName, key, versionChecksum));
    return executeRequest(request, ErrorType.ErrorFetchingObjectMeta);
  }

  @Override
  public HttpResponse getAssetMetaUsingVersionNumber(String bucketName, String key, int versionNumber) {
    HttpGet request = new HttpGet(objectUrlGenerator.getObjectMetaUsingVersionNumUrl(repoServer, bucketName, key, versionNumber));
    return executeRequest(request, ErrorType.ErrorFetchingObjectVersions);
  }

  @Override
  public HttpResponse getAssetVersionsMeta(String bucketName, String key) {
    HttpGet request = new HttpGet(objectUrlGenerator.getObjectVersionsUrl(repoServer, bucketName, key));
    return executeRequest(request, ErrorType.ErrorFetchingObjectMeta);
  }

  @Override
  public HttpResponse deleteAssetUsingVersionCks(String bucketName, String key, String versionChecksum) {
    HttpDelete request = new HttpDelete(objectUrlGenerator.getDeleteObjectVersionCksUrl(repoServer, bucketName, key, versionChecksum));
    return executeRequest(request, ErrorType.ErrorDeletingObject);
  }

  @Override
  public HttpResponse deleteAssetUsingVersionNumber(String bucketName, String key, int versionNumber) {
    HttpDelete request = new HttpDelete(objectUrlGenerator.getDeleteObjectVersionNumUrl(repoServer, bucketName, key, versionNumber));
    return executeRequest(request, ErrorType.ErrorDeletingObject);
  }

  @Override
  public HttpResponse createAsset(String bucketName, RepoObject repoObject, String contentType) {
    HttpPost request = new HttpPost(objectUrlGenerator.getCreateObjectUrl(repoServer));
    request.setEntity(getObjectEntity(bucketName, repoObject, CreationMethod.NEW, contentType));
    return executeRequest(request, ErrorType.ErrorCreatingObject);
  }

  @Override
  public HttpResponse versionAsset(String bucketName, RepoObject repoObject, String contentType) {
    HttpPost request = new HttpPost(objectUrlGenerator.getCreateObjectUrl(repoServer));
    request.setEntity(getObjectEntity(bucketName, repoObject, CreationMethod.VERSION, contentType));
    return executeRequest(request, ErrorType.ErrorVersioningObject);
  }

  private HttpEntity getObjectEntity(String bucketName, RepoObject repoObject, CreationMethod creationType, String contentType){
    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

    multipartEntityBuilder.addTextBody("key", repoObject.getKey());
    multipartEntityBuilder.addTextBody("bucketName", bucketName);
    multipartEntityBuilder.addTextBody("create", creationType.toString());
    multipartEntityBuilder.addTextBody("contentType", contentType);
    if (repoObject.getByteContent() != null){
      multipartEntityBuilder.addBinaryBody("file", repoObject.getByteContent());
    } else{
      multipartEntityBuilder.addBinaryBody("file", repoObject.getFileContent());
    }

    if (repoObject.getDownloadName() != null){
      multipartEntityBuilder.addTextBody("downloadName", repoObject.getDownloadName());
    }
    if (repoObject.getTimestamp() != null){
      multipartEntityBuilder.addTextBody("timestamp", repoObject.getTimestamp().toString());
    }
    if (repoObject.getCreationDate() != null){
      multipartEntityBuilder.addTextBody("creationDateTime", repoObject.getCreationDate().toString());
    }
    if (repoObject.getTag() != null){
      multipartEntityBuilder.addTextBody("tag", repoObject.getTag());
    }

    return multipartEntityBuilder.build();
  }

  @Override
  public HttpResponse getRedirectURL(String bucketName, String key){
    HttpGet request = new HttpGet(objectUrlGenerator.getLatestObjectUrl(repoServer, bucketName, key));
    request.setHeader("X-Proxy-Capabilities", "reproxy-file");
    return executeRequest(request, ErrorType.ErrorCreatingObject);
  }

  @Override
  public HttpResponse getRedirectURL(String bucketName, String key, String versionChecksum){
    HttpGet request = new HttpGet(objectUrlGenerator.getObjectMetaUsingVersionCksUrl(repoServer, bucketName, key, versionChecksum));
    request.setHeader("X-Proxy-Capabilities", "reproxy-file");
    return executeRequest(request, ErrorType.ErrorCreatingObject);
  }

  @Override
  public Logger getLog() {
    return log;
  }

}
