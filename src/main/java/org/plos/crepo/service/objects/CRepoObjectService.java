package org.plos.crepo.service.objects;

import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoObject;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by lmasola on 10/28/14.
 */
public interface CRepoObjectService {

  /**
   * Get a URL array which can be used as a links to the specified file
   * via XReproxy-url http header redirection.
   *
   * @deprecated use {@link #getAssetByteArrayUsingVersionCks(String, String)} or
   * {@link #getAssetByteArrayUsingVersionNum(String, int)} instead.
   *
   * @param fsid - the file system's id for the file requested
   * @return - URL that can be used as a redirect for the file
   */
  @Deprecated
  public URL[] getRedirectURL(String fsid);

  public URL[] getRedirectURL(String key, String versionChecksum);


  /**
   * Get the content of the latest version of an object using the most recent creation date time
   *
   * @deprecated use {@link #getAssetInStreamUsingVersionCks(String, String)} or
   * {@link #getAssetInStreamUsingVersionNum(String, int)} instead.
   *
   * @param key a single String representing the object key
   * @return an InputStream of the content
   */
  @Deprecated
  InputStream getLatestAssetInStream(String key);

  /**
   * Get the content of the  latest version of an object using the most recent creation date time
   *
   * @deprecated use {@link #getAssetByteArrayUsingVersionCks(String, String)} or
   * {@link #getAssetByteArrayUsingVersionNum(String, int)} instead.
   *
   * @param key a single String representing the object key
   * @return a byte array og the content
   */
  @Deprecated
  byte[] getLatestAssetByteArray(String key);

  InputStream getAssetInStreamUsingVersionCks(String key, String versionChecksum);

  byte[] getAssetByteArrayUsingVersionCks(String key, String versionChecksum);

  InputStream getAssetInStreamUsingVersionNum(String key, int versionNumber);

  byte[] getAssetByteArrayUsingVersionNum(String key, int versionNumber);

  /**
   * Get the meta data of the latest version of an object using the most recent
   * creation date time.
   *
   * @deprecated use {@link #getAssetMetaUsingVersionChecksum(String, String)} or
   * {@link #getAssetMetaUsingVersionNumber(String, int)} instead.
   *
   * @param key a single String representing the object key
   * @return
   */
  @Deprecated
  Map<String,Object> getAssetMetaLatestVersion(String key);

  Map<String,Object> getAssetMetaUsingVersionChecksum(String key, String versionChecksum);

  Map<String,Object> getAssetMetaUsingVersionNumber(String key, int versionNumber);

  List<Map<String,Object>> getAssetVersionsMeta(String key);

  /**
   * Get the meta data of the latest version of an object using the most recent
   * creation date time.
   *
   * @deprecated use {@link #deleteAssetUsingVersionChecksum(String, String)} or
   * {@link #deleteAssetUsingVersionNumber(String, int)} instead.
   *
   * @param key a single String representing the object key
   * @return
   */
  @Deprecated
  Boolean deleteLatestAsset(String key);

  Boolean deleteAssetUsingVersionChecksum(String key, String versionChecksum);

  Boolean deleteAssetUsingVersionNumber(String key, int versionNumber);

  Map<String, Object> createAsset(RepoObject repoObject);

  Map<String, Object> versionAsset(RepoObject repoObject);

}
