package org.plos.crepo.dao.collections;


import org.apache.http.HttpResponse;
import org.plos.crepo.model.RepoCollection;

public interface ContentRepoCollectionDao {

  HttpResponse createCollection(String bucketName, RepoCollection repoCollection);

  HttpResponse versionCollection(String bucketName, RepoCollection repoCollection);

  HttpResponse deleteCollectionUsingVersionCks(String bucketName, String key, String versionChecksum);

  HttpResponse deleteCollectionUsingVersionNumb(String bucketName, String key, int versionNumber);

  HttpResponse getCollectionUsingVersionCks(String bucketName, String key, String versionChecksum);

  HttpResponse getCollectionUsingVersionNumber(String bucketName, String key, int versionNumber);

  HttpResponse getCollectionUsingTag(String bucketName, String key, String tag);

  HttpResponse getCollectionVersions(String bucketName, String key);

  HttpResponse getCollections(String bucketName, int offset, int limit, boolean includeDeleted);

  HttpResponse getCollectionsUsingTag(String bucketName, int offset, int limit, boolean includeDeleted, String tag);


}
