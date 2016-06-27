package org.plos.crepo.dao.collections;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.plos.crepo.model.input.RepoCollectionInput;

public interface ContentRepoCollectionDao {

  CloseableHttpResponse createCollection(String bucketName, RepoCollectionInput repoCollectionInput);

  CloseableHttpResponse versionCollection(String bucketName, RepoCollectionInput repoCollectionInput);

  CloseableHttpResponse deleteCollectionUsingUuid(String bucketName, String key, String uuid);

  CloseableHttpResponse autoCreateCollection(String bucketName, RepoCollectionInput repoCollectionInput);

  CloseableHttpResponse deleteCollectionUsingVersionNumber(String bucketName, String key, int versionNumber);

  CloseableHttpResponse getCollectionUsingUuid(String bucketName, String key, String uuid);

  CloseableHttpResponse getCollectionUsingVersionNumber(String bucketName, String key, int versionNumber);

  CloseableHttpResponse getCollectionUsingTag(String bucketName, String key, String tag);

  CloseableHttpResponse getLatestCollection(String bucketName, String key);

  CloseableHttpResponse getCollectionVersions(String bucketName, String key);

  CloseableHttpResponse getCollections(String bucketName, int offset, int limit, boolean includeDeleted);

  CloseableHttpResponse getCollectionsUsingTag(String bucketName, int offset, int limit, boolean includeDeleted, String tag);


}
