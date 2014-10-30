package org.plos.crepo.service.objects;

import org.plos.crepo.model.RepoObject;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Content Repo Object service interface.
 */
public interface CRepoObjectService {

  /**
   * Get a URL array which can be used as a links to the specified repo object content
   * via XReproxy-url http header redirection.
   *
   * @deprecated use {@link #getRepoObjByteArrayUsingVersionCks(String, String)} or
   * {@link #getRepoObjByteArrayUsingVersionNum(String, int)} instead.
   *
   * @param key a single string representing the key of the repo object
   * @return - URL that can be used as a redirect for the file
   */
  @Deprecated
  public URL[] getRepoObjRedirectURL(String key);

  /**
   * Returns a URL array which can be used as a links to the specified repo object content.
   * @param key a single string representing the key of the repo object
   * @param versionChecksum a single string representing the version checksum of the object
   * @return a URL array
   */
  public URL[] getRepoObjRedirectURL(String key, String versionChecksum);


  /**
   * Returns the content of the latest version of an object using the most recent creation date time
   *
   * @deprecated use {@link #getRepoObjStreamUsingVersionCks(String, String)} or
   * {@link #getRepoObjStreamUsingVersionNum(String, int)} instead.
   *
   * @param key a single string representing the key of the repo object
   * @return an InputStream of the content
   */
  @Deprecated
  InputStream getLatestRepoObjStream(String key);

  /**
   * Returns the content of the latest version of an object using the most recent creation date time
   *
   * @deprecated use {@link #getRepoObjByteArrayUsingVersionCks(String, String)} or
   * {@link #getRepoObjByteArrayUsingVersionNum(String, int)} instead.
   *
   * @param key a single string representing the key of the repo object
   * @return a byte array og the content
   */
  @Deprecated
  byte[] getLatestRepoObjByteArray(String key);

  /**
   * Returns the content of the repo object specified by <code>key</code> & the <code>versionChecksum</code>
   * @param key a single string representing the key of the object
   * @param versionChecksum a single string representing the version checksum of the repo object
   * @return an InputStream representing the repo object content.
   */
  InputStream getRepoObjStreamUsingVersionCks(String key, String versionChecksum);

  /**
   * Returns the content of the repo object specified by <code>key</code> & the <code>versionChecksum</code>
   * @param key a single string representing the key of the repo object
   * @param versionChecksum a single string representing the version checksum of the repo object
   * @param versionChecksum
   * @return a byte array representing the repo object content
   */
  byte[] getRepoObjByteArrayUsingVersionCks(String key, String versionChecksum);

  /**
   * Returns the content of the repo object specified by <code>key</code> & the <code>versionNumber</code>
   * @param key a single string representing the key of the repo object
   * @param versionNumber an int value representing the version number of the repo object
   * @return an InputStream object with the content.
   */
  InputStream getRepoObjStreamUsingVersionNum(String key, int versionNumber);

  /**
   * Returns the content of the repo object specified by <code>key</code> & the <code>versionNumber</code>
   * @param key a single string representing the key of the repo object
   * @param versionNumber an int value representing the version number of the repo object
   * @return a byte array representing the repo object content
   */
  byte[] getRepoObjByteArrayUsingVersionNum(String key, int versionNumber);

  /**
   * Returns the meta data of the latest version of an object using the most recent
   * creation date time.
   *
   * @deprecated use {@link #getRepoObjMetaUsingVersionChecksum(String, String)} or
   * {@link #getAssetMetaUsingVersionNumber(String, int)} instead.
   *
   * @param key a single string representing the key of the repo object
   * @return a map with the repo object meta data.
   */
  @Deprecated
  Map<String,Object> getRepoObjMetaLatestVersion(String key);

  /**
   * Returns the meta data of the repo object specified by <code>key</code> & <code>versionChecksum</code>
   * @param key a single string representing the key of the repo object
   * @param versionChecksum a single string representing the version checksum of the repo object
   * @return a map with the repo object meta data.
   */
  Map<String,Object> getRepoObjMetaUsingVersionChecksum(String key, String versionChecksum);

  /**
   * Returns the meta data of the repo object specified by <code>key</code> & <code>versionNumber</code>
   * @param key a single string representing the key of the repo object
   * @param versionNumber an int value representing the version number of the repo object
   * @return a map with the repo object meta data.
   */
  Map<String,Object> getAssetMetaUsingVersionNumber(String key, int versionNumber);

  /**
   * Returns the meta data of all the versions for the given repo object, using the repo object
   * key <code>key</code>
   * @param key a single string representing the key of the repo object
   * @return a list of the repo object versions
   */
  List<Map<String,Object>> getRepoObjVersions(String key);

  /**
   * Deletes the latest version of the repo object using object key <code>key</code>
   *
   * @deprecated use {@link #deleteAssetUsingVersionChecksum(String, String)} or
   * {@link #deleteAssetUsingVersionNumber(String, int)} instead.
   *
   * @param key a single string representing the key of the repo object
   * @return true if the object was successfully deleted.
   */
  @Deprecated
  Boolean deleteLatestRepoObj(String key);

  /**
   * Deletes the specific version of a repo object using the key <code>key</code>
   * & the version checksum <code>versionChecksum</code>
   * @param key a single string representing the key of the repo object
   * @param versionChecksum a single string representing the version checksum of the repo object
   * @return true if the object was successfully deleted.
   */
  Boolean deleteAssetUsingVersionChecksum(String key, String versionChecksum);

  /**
   * Deletes the specific version of a repo object using the key <code>key</code>
   * & the version checksum <code>versionNumber</code>
   * @param key a single string representing the key of the repo object
   * @param versionNumber an int value representing the version number of the repo object
   * @return true if the object was successfully deleted.
   */
  Boolean deleteAssetUsingVersionNumber(String key, int versionNumber);

  /**
   * Creates a repo object using <code>repoObject</code>
   * @param repoObject a RepoObject containing the meta data & content of the new
   * repo object
   * @return a map with the repo object metadata
   */
  Map<String, Object> createRepoObject(RepoObject repoObject);

  /**
   * Versions a repo object using <code>repoObject</code>
   * @param repoObject a RepoObject containing the meta data & content of the new
   * repo object
   * @return a map with the repo object metadata
   */
  Map<String, Object> versionRepoObject(RepoObject repoObject);

}
