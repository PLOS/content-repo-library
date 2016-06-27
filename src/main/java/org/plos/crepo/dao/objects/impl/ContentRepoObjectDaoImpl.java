package org.plos.crepo.dao.objects.impl;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.ContentRepoBaseDao;
import org.plos.crepo.dao.objects.ContentRepoObjectDao;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.CreationMethod;
import org.plos.crepo.model.input.RepoObjectInput;
import org.plos.crepo.util.ObjectUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class ContentRepoObjectDaoImpl extends ContentRepoBaseDao implements ContentRepoObjectDao {

  private static final Logger log = LoggerFactory.getLogger(ContentRepoObjectDaoImpl.class);

  public ContentRepoObjectDaoImpl(ContentRepoAccessConfig accessConfig) {
    super(accessConfig);
  }

  @Override
  public CloseableHttpResponse getLatestRepoObj(String bucketName, String key) {

    HttpGet request = new HttpGet(ObjectUrlGenerator.getLatestObjectUrl(getRepoServer(), bucketName, key));
    return executeRequest(request, ErrorType.ErrorFetchingObject);

  }

  @Override
  public CloseableHttpResponse getRepoObjUsingUuid(String bucketName, String key, String uuid) {

    HttpGet request = new HttpGet(ObjectUrlGenerator.getObjectUsingUuidUrl(getRepoServer(), bucketName, key, uuid));
    return executeRequest(request, ErrorType.ErrorFetchingObject);

  }

  @Override
  public CloseableHttpResponse getRepoObjUsingVersionNum(String bucketName, String key, int versionNumber) {

    HttpGet request = new HttpGet(ObjectUrlGenerator.getObjectUsingVersionNumUrl(getRepoServer(), bucketName, key, versionNumber));
    return executeRequest(request, ErrorType.ErrorFetchingObject);

  }

  @Override
  public CloseableHttpResponse getRepoObjMetaLatestVersion(String bucketName, String key) {
    HttpGet request = new HttpGet(ObjectUrlGenerator.getLatestObjectMetaUrl(getRepoServer(), bucketName, key));
    return executeRequest(request, ErrorType.ErrorFetchingObjectMeta);
  }

  @Override
  public CloseableHttpResponse getRepoObjMetaUsingUuid(String bucketName, String key, String uuid) {
    HttpGet request = new HttpGet(ObjectUrlGenerator.getObjectMetaUsingUuidUrl(getRepoServer(), bucketName, key, uuid));
    return executeRequest(request, ErrorType.ErrorFetchingObjectMeta);
  }

  @Override
  public CloseableHttpResponse getRepoObjMetaUsingVersionNumber(String bucketName, String key, int versionNumber) {
    HttpGet request = new HttpGet(ObjectUrlGenerator.getObjectMetaUsingVersionNumUrl(getRepoServer(), bucketName, key, versionNumber));
    return executeRequest(request, ErrorType.ErrorFetchingObjectMeta);
  }

  @Override
  public CloseableHttpResponse getRepoObjVersionsMeta(String bucketName, String key) {
    HttpGet request = new HttpGet(ObjectUrlGenerator.getObjectVersionsUrl(getRepoServer(), bucketName, key));
    return executeRequest(request, ErrorType.ErrorFetchingObjectVersions);
  }

  @Override
  public CloseableHttpResponse getRepoObjMetaUsingTag(String bucketName, String key, String tag) {
    HttpGet request = new HttpGet(ObjectUrlGenerator.getGetObjMetaUsingTagUrl(getRepoServer(), bucketName, key, tag));
    return executeRequest(request, ErrorType.ErrorFetchingObjectMeta);
  }

  @Override
  public CloseableHttpResponse deleteRepoObjUsingUuid(String bucketName, String key, String uuid) {
    HttpDelete request = new HttpDelete(ObjectUrlGenerator.getObjectUsingUuidUrl(getRepoServer(), bucketName, key, uuid));
    return executeRequest(request, ErrorType.ErrorDeletingObject);
  }

  @Override
  public CloseableHttpResponse deleteRepoObjUsingVersionNumber(String bucketName, String key, int versionNumber) {
    HttpDelete request = new HttpDelete(ObjectUrlGenerator.getObjectUsingVersionNumUrl(getRepoServer(), bucketName, key, versionNumber));
    return executeRequest(request, ErrorType.ErrorDeletingObject);
  }

  @Override
  public CloseableHttpResponse createRepoObj(String bucketName, RepoObjectInput repoObjectInput, String contentType) {
    return executePost(bucketName, repoObjectInput, contentType, CreationMethod.NEW, ErrorType.ErrorCreatingObject);
  }

  @Override
  public CloseableHttpResponse versionRepoObj(String bucketName, RepoObjectInput repoObjectInput, String contentType) {
    return executePost(bucketName, repoObjectInput, contentType, CreationMethod.VERSION, ErrorType.ErrorVersioningObject);
  }

  @Override
  public CloseableHttpResponse autoCreateRepoObj(String bucketName, RepoObjectInput repoObjectInput, String contentType) {
    return executePost(bucketName, repoObjectInput, contentType, CreationMethod.AUTO, ErrorType.ErrorAutoCreatingObject);
  }

  private CloseableHttpResponse executePost(String bucketName, RepoObjectInput repoObjectInput, String contentType,
                                            CreationMethod creationMethod, ErrorType errorType) {
    HttpPost request = new HttpPost(ObjectUrlGenerator.getCreateObjectUrl(getRepoServer()));
    try (InputStream stream = repoObjectInput.getContentAccessor().open()) {
      request.setEntity(getObjectEntity(bucketName, repoObjectInput, stream, creationMethod, contentType));
      return executeRequest(request, errorType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private HttpEntity getObjectEntity(String bucketName, RepoObjectInput repoObjectInput, InputStream stream, CreationMethod creationType, String contentType) {
    MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
    multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

    multipartEntityBuilder.addTextBody("key", repoObjectInput.getKey());
    multipartEntityBuilder.addTextBody("bucketName", bucketName);
    multipartEntityBuilder.addTextBody("create", creationType.toString());
    multipartEntityBuilder.addTextBody("contentType", contentType);
    multipartEntityBuilder.addBinaryBody("file", stream);

    if (repoObjectInput.getDownloadName() != null) {
      multipartEntityBuilder.addTextBody("downloadName", repoObjectInput.getDownloadName());
    }
    if (repoObjectInput.getTimestamp() != null) {
      multipartEntityBuilder.addTextBody("timestamp", repoObjectInput.getTimestamp().toString());
    }
    if (repoObjectInput.getCreationDate() != null) {
      multipartEntityBuilder.addTextBody("creationDateTime", repoObjectInput.getCreationDate().toString());
    }
    if (repoObjectInput.getTag() != null) {
      multipartEntityBuilder.addTextBody("tag", repoObjectInput.getTag());
    }
    if (repoObjectInput.getUserMetadata() != null) {
      multipartEntityBuilder.addTextBody("userMetadata", repoObjectInput.getUserMetadata());
    }

    return multipartEntityBuilder.build();

  }

  @Override
  public CloseableHttpResponse getRedirectURL(String bucketName, String key) {
    HttpGet request = new HttpGet(ObjectUrlGenerator.getLatestObjectUrl(getRepoServer(), bucketName, key));
    request.setHeader("X-Proxy-Capabilities", "reproxy-file");
    return executeRequest(request, ErrorType.ErrorCreatingObject);
  }

  @Override
  public CloseableHttpResponse getObjects(String bucketName, int offset, int limit, boolean includeDeleted) {
    HttpGet request = new HttpGet(ObjectUrlGenerator.getGetObjectsUrl(getRepoServer(), bucketName, offset, limit, includeDeleted));
    return executeRequest(request, ErrorType.ErrorFetchingCollection);
  }

  @Override
  public CloseableHttpResponse getObjectsUsingTag(String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    HttpGet request = new HttpGet(ObjectUrlGenerator.getGetObjectsUrl(getRepoServer(), bucketName, offset, limit, includeDeleted, tag));
    return executeRequest(request, ErrorType.ErrorFetchingCollection);
  }

  @Override
  public Logger getLog() {
    return log;
  }

}
