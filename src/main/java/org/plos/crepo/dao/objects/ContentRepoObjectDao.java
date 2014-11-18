package org.plos.crepo.dao.objects;


import org.apache.http.HttpResponse;
import org.plos.crepo.model.RepoObject;

public interface ContentRepoObjectDao {

  HttpResponse getLatestRepoObj(String bucketName, String key);

  HttpResponse getRepoObjUsingVersionCks(String bucketName, String key, String versionChecksum);

  HttpResponse getRepoObjUsingVersionNum(String bucketName, String key, int versionNumber);

  HttpResponse getRepoObjMetaLatestVersion(String bucketName, String key);

  HttpResponse getRepoObjMetaUsingVersionChecksum(String bucketName, String key, String versionChecksum);

  HttpResponse getRepoObjMetaUsingVersionNumber(String bucketName, String key, int versionNumber);

  HttpResponse getRepoObjVersionsMeta(String bucketName, String key);

  HttpResponse getRepoObjMetaUsingTag(String bucketName, String key, String tag);

  HttpResponse deleteRepoObjUsingVersionCks(String bucketName, String key, String versionChecksum);

  HttpResponse deleteRepoObjUsingVersionNumber(String bucketName, String key, int versionNumber);

  HttpResponse createRepoObj(String bucketName, RepoObject repoObject, String contentType);

  HttpResponse versionRepoObj(String bucketName, RepoObject repoObject, String contentType);

  HttpResponse getRedirectURL(String bucketName, String key);

  HttpResponse getObjects(String bucketName, int offset, int limit, boolean includeDeleted);

  HttpResponse getObjectsUsingTag(String bucketName, int offset, int limit, boolean includeDeleted, String tag);

}
