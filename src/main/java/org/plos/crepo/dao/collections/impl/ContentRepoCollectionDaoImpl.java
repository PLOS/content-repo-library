package org.plos.crepo.dao.collections.impl;

import com.google.gson.Gson;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.ContentRepoBaseDao;
import org.plos.crepo.dao.collections.ContentRepoCollectionDao;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.CreationMethod;
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoCollectionEntity;
import org.plos.crepo.util.CollectionUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;

@Repository
public class ContentRepoCollectionDaoImpl extends ContentRepoBaseDao implements ContentRepoCollectionDao {

  private static final Logger log = LoggerFactory.getLogger(ContentRepoCollectionDaoImpl.class);

  public ContentRepoCollectionDaoImpl(ContentRepoAccessConfig accessConfig) {
    super(accessConfig);
  }

  @Override
  public Logger getLog() {
    return log;
  }

  @Override
  public CloseableHttpResponse createCollection(String bucketName, RepoCollection repoCollection) {
    HttpPost request = new HttpPost(CollectionUrlGenerator.getCreateCollUrl(getRepoServer()));
    request.setEntity(getCollectionEntity(bucketName, repoCollection, CreationMethod.NEW));
    request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    return executeRequest(request, ErrorType.ErrorCreatingCollection);
  }

  @Override
  public CloseableHttpResponse versionCollection(String bucketName, RepoCollection repoCollection) {
    HttpPost request = new HttpPost(CollectionUrlGenerator.getCreateCollUrl(getRepoServer()));
    request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    request.setEntity(getCollectionEntity(bucketName, repoCollection, CreationMethod.VERSION));
    return executeRequest(request, ErrorType.ErrorVersioningCollection);
  }

  @Override
  public CloseableHttpResponse deleteCollectionUsingVersionCks(String bucketName, String key, String versionChecksum) {
    HttpDelete request = new HttpDelete(CollectionUrlGenerator.getDeleteCollUsingVersionCksUrl(getRepoServer(), bucketName, key, versionChecksum));
    return executeRequest(request, ErrorType.ErrorDeletingCollection);
  }

  @Override
  public CloseableHttpResponse deleteCollectionUsingVersionNumb(String bucketName, String key, int versionNumber) {
    HttpDelete request = new HttpDelete(CollectionUrlGenerator.getDeleteCollUsingVersionNumUrl(getRepoServer(), bucketName, key, versionNumber));
    return executeRequest(request, ErrorType.ErrorDeletingCollection);
  }

  @Override
  public CloseableHttpResponse getCollectionUsingVersionCks(String bucketName, String key, String versionChecksum) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getGetCollVersionCksUrl(getRepoServer(), bucketName, key, versionChecksum));
    return executeRequest(request, ErrorType.ErrorFetchingCollection);
  }

  @Override
  public CloseableHttpResponse getCollectionUsingVersionNumber(String bucketName, String key, int versionNumber) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getGetCollUsingVersionNumUrl(getRepoServer(), bucketName, key, versionNumber));
    return executeRequest(request, ErrorType.ErrorFetchingCollection);
  }

  @Override
  public CloseableHttpResponse getCollectionUsingTag(String bucketName, String key, String tag) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getGetCollUsingTagUrl(getRepoServer(), bucketName, key, tag));
    return executeRequest(request, ErrorType.ErrorFetchingCollection);
  }

  @Override
  public CloseableHttpResponse getCollectionVersions(String bucketName, String key) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getGetCollVersionsUrl(getRepoServer(), bucketName, key));
    return executeRequest(request, ErrorType.ErrorFetchingCollectionVersions);
  }

  @Override
  public CloseableHttpResponse getCollectionsUsingTag(String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getGetCollectionsUsingTagUrl(getRepoServer(), bucketName, offset, limit, includeDeleted, tag));
    return executeRequest(request, ErrorType.ErrorFetchingCollections);
  }

  @Override
  public CloseableHttpResponse getCollections(String bucketName, int offset, int limit, boolean includeDeleted) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getGetCollectionsUrl(getRepoServer(), bucketName, offset, limit, includeDeleted));
    return executeRequest(request, ErrorType.ErrorFetchingCollections);
  }

  private StringEntity getCollectionEntity(String bucketName, RepoCollection repoCollection, CreationMethod creationType) {
    Gson gson = new Gson();
    StringEntity entityString = null;
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
