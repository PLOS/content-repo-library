package org.plos.crepo.dao.collections.impl;

import com.google.gson.Gson;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.plos.crepo.dao.ContentRepoBaseDao;
import org.plos.crepo.dao.collections.ContentRepoCollectionsDao;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.CreationMethod;
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoCollectionEntity;
import org.plos.crepo.util.CollectionUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;

@Repository
public class ContentRepoCollectionsDaoImpl extends ContentRepoBaseDao implements ContentRepoCollectionsDao {

  private static final Logger log = LoggerFactory.getLogger(ContentRepoCollectionsDaoImpl.class);

  @Autowired
  private CollectionUrlGenerator collectionUrlGenerator;

  @Override
  public Logger getLog() {
    return log;
  }

  @Override
  public HttpResponse createCollection(String bucketName, RepoCollection repoCollection) {
    HttpPost request = new HttpPost(collectionUrlGenerator.getCreateCollUrl(repoServer));
    request.setEntity(getCollectionEntity(bucketName, repoCollection, CreationMethod.NEW));
    request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    return executeRequest(request, ErrorType.ErrorCreatingCollection);
  }

  @Override
  public HttpResponse versionCollection(String bucketName, RepoCollection repoCollection) {
    HttpPost request = new HttpPost(collectionUrlGenerator.getCreateCollUrl(repoServer));
    request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    request.setEntity(getCollectionEntity(bucketName, repoCollection, CreationMethod.VERSION));
    return executeRequest(request, ErrorType.ErrorVersioningCollection);
  }

  @Override
  public HttpResponse deleteCollectionUsingVersionCks(String bucketName, String key, String versionChecksum) {
    HttpDelete request = new HttpDelete(collectionUrlGenerator.getDeleteCollUsingVersionCksUrl(repoServer, bucketName, key, versionChecksum));
    return executeRequest(request, ErrorType.ErrorDeletingCollection);
  }

  @Override
  public HttpResponse deleteCollectionUsingVersionNumb(String bucketName, String key, int versionNumber) {
    HttpDelete request = new HttpDelete(collectionUrlGenerator.getDeleteCollUsingVersionNumUrl(repoServer, bucketName, key, versionNumber));
    return executeRequest(request, ErrorType.ErrorDeletingCollection);
  }

  @Override
  public HttpResponse getCollectionUsingVersionCks(String bucketName, String key, String versionChecksum) {
    HttpGet request = new HttpGet(collectionUrlGenerator.getGetCollVersionCksUrl(repoServer, bucketName, key, versionChecksum));
    return executeRequest(request, ErrorType.ErrorFetchingCollection);
  }

  @Override
  public HttpResponse getCollectionUsingVersionNumber(String bucketName, String key, int versionNumber) {
    HttpGet request = new HttpGet(collectionUrlGenerator.getGetCollUsingVersionNumUrl(repoServer, bucketName, key, versionNumber));
    return executeRequest(request, ErrorType.ErrorFetchingCollection);
  }

  @Override
  public HttpResponse getCollectionUsingTag(String bucketName, String key, String tag) {
    HttpGet request = new HttpGet(collectionUrlGenerator.getGetCollUsingTagUrl(repoServer, bucketName, key, tag));
    return executeRequest(request, ErrorType.ErrorFetchingCollection);
  }

  @Override
  public HttpResponse getCollectionVersions(String bucketName, String key){
    HttpGet request = new HttpGet(collectionUrlGenerator.getGetCollVersionsUrl(repoServer, bucketName, key));
    return executeRequest(request, ErrorType.ErrorFetchingCollectionVersions);
  }

  @Override
  public HttpResponse getCollectionsUsingTag(String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    HttpGet request = new HttpGet(collectionUrlGenerator.getGetCollectionsUsingTagUrl(repoServer, bucketName, offset, limit, includeDeleted, tag));
    return executeRequest(request, ErrorType.ErrorFetchingCollections);
  }

  @Override
  public HttpResponse getCollections(String bucketName, int offset, int limit, boolean includeDeleted) {
    HttpGet request = new HttpGet(collectionUrlGenerator.getGetCollectionsUrl(repoServer, bucketName, offset, limit, includeDeleted));
    return executeRequest(request, ErrorType.ErrorFetchingCollections);
  }

  private StringEntity getCollectionEntity(String bucketName, RepoCollection repoCollection, CreationMethod creationType) {
    Gson gson= new Gson();
    StringEntity  entityString = null;
    RepoCollectionEntity repoCollectionEntity = new RepoCollectionEntity(repoCollection, bucketName, creationType.toString());
    try {
      entityString = new StringEntity(gson.toJson(repoCollectionEntity));
    } catch (UnsupportedEncodingException e) {
      log.error("Error generating the StringEntity to send in the POST for ---> bucketName: "
          + bucketName + " key " + repoCollection.getKey() + " creationType " + creationType, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorCreatingCollection)
          .key(repoCollection.getKey())
          .baseException(e)
          .build();
    }
    return entityString;
  }

}
