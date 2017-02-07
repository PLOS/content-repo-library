/*
 * Copyright 2017 Public Library of Science
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

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
import org.plos.crepo.model.input.RepoCollectionInput;
import org.plos.crepo.model.input.RepoCollectionEntity;
import org.plos.crepo.util.CollectionUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;

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
  public CloseableHttpResponse createCollection(String bucketName, RepoCollectionInput repoCollectionInput) {
    HttpPost request = new HttpPost(CollectionUrlGenerator.getCreateCollUrl(getRepoServer()));
    request.setEntity(getCollectionEntity(bucketName, repoCollectionInput, CreationMethod.NEW));
    request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    return executeRequest(request, ErrorType.ErrorCreatingCollection);
  }

  @Override
  public CloseableHttpResponse versionCollection(String bucketName, RepoCollectionInput repoCollectionInput) {
    HttpPost request = new HttpPost(CollectionUrlGenerator.getCreateCollUrl(getRepoServer()));
    request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    request.setEntity(getCollectionEntity(bucketName, repoCollectionInput, CreationMethod.VERSION));
    return executeRequest(request, ErrorType.ErrorVersioningCollection);
  }

  @Override
  public CloseableHttpResponse deleteCollectionUsingUuid(String bucketName, String key, String uuid) {
    HttpDelete request = new HttpDelete(CollectionUrlGenerator.getCollectionUuidUrl(getRepoServer(), bucketName, key, uuid));
    return executeRequest(request, ErrorType.ErrorDeletingCollection);
  }

  public CloseableHttpResponse autoCreateCollection(String bucketName, RepoCollectionInput repoCollectionInput) {
    HttpPost request = new HttpPost(CollectionUrlGenerator.getCreateCollUrl(getRepoServer()));
    request.setEntity(getCollectionEntity(bucketName, repoCollectionInput, CreationMethod.AUTO));
    request.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
    return executeRequest(request, ErrorType.ErrorAutoCreatingCollection);
  }

  @Override
  public CloseableHttpResponse deleteCollectionUsingVersionNumber(String bucketName, String key, int versionNumber) {
    HttpDelete request = new HttpDelete(CollectionUrlGenerator.getCollectionVersionNumUrl(getRepoServer(), bucketName, key, versionNumber));
    return executeRequest(request, ErrorType.ErrorDeletingCollection);
  }

  @Override
  public CloseableHttpResponse getCollectionUsingUuid(String bucketName, String key, String uuid) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getCollectionUuidUrl(getRepoServer(), bucketName, key, uuid));
    return executeRequest(request, ErrorType.ErrorFetchingCollection);
  }

  @Override
  public CloseableHttpResponse getCollectionUsingVersionNumber(String bucketName, String key, int versionNumber) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getCollectionVersionNumUrl(getRepoServer(), bucketName, key, versionNumber));
    return executeRequest(request, ErrorType.ErrorFetchingCollection);
  }

  @Override
  public CloseableHttpResponse getCollectionUsingTag(String bucketName, String key, String tag) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getCollectionTagUrl(getRepoServer(), bucketName, key, tag));
    return executeRequest(request, ErrorType.ErrorFetchingCollection);
  }

  @Override
  public CloseableHttpResponse getLatestCollection(String bucketName, String key) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getLatestCollectionUrl(getRepoServer(), bucketName, key));
    return executeRequest(request, ErrorType.ErrorFetchingCollection);
  }

  @Override
  public CloseableHttpResponse getCollectionVersions(String bucketName, String key) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getCollectionVersionsUrl(getRepoServer(), bucketName, key));
    return executeRequest(request, ErrorType.ErrorFetchingCollectionVersions);
  }

  @Override
  public CloseableHttpResponse getCollectionsUsingTag(String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getCollectionsUsingTagUrl(getRepoServer(), bucketName, offset, limit, includeDeleted, tag));
    return executeRequest(request, ErrorType.ErrorFetchingCollections);
  }

  @Override
  public CloseableHttpResponse getCollections(String bucketName, int offset, int limit, boolean includeDeleted) {
    HttpGet request = new HttpGet(CollectionUrlGenerator.getGetCollectionsUrl(getRepoServer(), bucketName, offset, limit, includeDeleted));
    return executeRequest(request, ErrorType.ErrorFetchingCollections);
  }

  private StringEntity getCollectionEntity(String bucketName, RepoCollectionInput repoCollectionInput, CreationMethod creationType) {
    Gson gson = new Gson();
    StringEntity entityString = null;
    RepoCollectionEntity repoCollectionEntity = new RepoCollectionEntity(repoCollectionInput, bucketName, creationType.toString());
    try {
      entityString = new StringEntity(gson.toJson(repoCollectionEntity));
    } catch (UnsupportedEncodingException e) {
      log.error("Error generating the StringEntity to send in the POST for ---> bucketName: "
          + bucketName + " key " + repoCollectionInput.getKey() + " creationType " + creationType, e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorCreatingCollection)
          .key(repoCollectionInput.getKey())
          .baseException(e)
          .build();
    }
    return entityString;
  }

}
