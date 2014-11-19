package org.plos.crepo.dao.objects;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.plos.crepo.model.RepoObject;

public interface ContentRepoObjectDao {

  CloseableHttpResponse getLatestRepoObj(String bucketName, String key);

  CloseableHttpResponse getRepoObjUsingVersionCks(String bucketName, String key, String versionChecksum);

  CloseableHttpResponse getRepoObjUsingVersionNum(String bucketName, String key, int versionNumber);

  CloseableHttpResponse getRepoObjMetaLatestVersion(String bucketName, String key);

  CloseableHttpResponse getRepoObjMetaUsingVersionChecksum(String bucketName, String key, String versionChecksum);

  CloseableHttpResponse getRepoObjMetaUsingVersionNumber(String bucketName, String key, int versionNumber);

  CloseableHttpResponse getRepoObjVersionsMeta(String bucketName, String key);

  CloseableHttpResponse getRepoObjMetaUsingTag(String bucketName, String key, String tag);

  CloseableHttpResponse deleteRepoObjUsingVersionCks(String bucketName, String key, String versionChecksum);

  CloseableHttpResponse deleteRepoObjUsingVersionNumber(String bucketName, String key, int versionNumber);

  CloseableHttpResponse createRepoObj(String bucketName, RepoObject repoObject, String contentType);

  CloseableHttpResponse versionRepoObj(String bucketName, RepoObject repoObject, String contentType);

  CloseableHttpResponse getRedirectURL(String bucketName, String key);

  CloseableHttpResponse getObjects(String bucketName, int offset, int limit, boolean includeDeleted);

  CloseableHttpResponse getObjectsUsingTag(String bucketName, int offset, int limit, boolean includeDeleted, String tag);

}
