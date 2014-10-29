package org.plos.crepo.dao.objects;


import org.apache.http.HttpResponse;
import org.plos.crepo.model.RepoObject;

public interface ContentRepoObjectsDao {

  HttpResponse getLatestAsset(String bucketName, String key);

  HttpResponse getAssetUsingVersionCks(String bucketName, String key, String versionChecksum);

  HttpResponse getAssetUsingVersionNum(String bucketName, String key, int versionNumber);

  HttpResponse getAssetMetaLatestVersion(String bucketName, String key);

  HttpResponse getAssetMetaUsingVersionChecksum(String bucketName, String key, String versionChecksum);

  HttpResponse getAssetMetaUsingVersionNumber(String bucketName, String key, int versionNumber);

  HttpResponse getAssetVersionsMeta(String bucketName, String key);

  HttpResponse deleteAssetUsingVersionCks(String bucketName, String key, String versionChecksum);

  HttpResponse deleteAssetUsingVersionNumber(String bucketName, String key, int versionNumber);

  HttpResponse createAsset(String bucketName, RepoObject repoObject, String contentType);

  HttpResponse versionAsset(String bucketName, RepoObject repoObject, String contentType);

  HttpResponse getRedirectURL(String bucketName, String key);

  HttpResponse getRedirectURL(String bucketName, String key, String versionChecksum);

}
