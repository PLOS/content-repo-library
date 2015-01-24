package org.plos.crepo.service;

import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.model.RepoVersion;
import org.plos.crepo.model.RepoVersionNumber;
import org.plos.crepo.model.RepoVersionTag;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

public interface ContentRepoService {

  // ------------------------ Config ------------------------

  /**
   * Query whether content repo provides HTTP redirects to files.
   *
   * @return - true if the repo can provide redirects for HTTP access to files.
   */
  public boolean hasXReproxy();

  /**
   * Returns the content repo server configuration
   *
   * @return a map with the configuration fields.
   */
  Map<String, Object> getRepoConfig();

  /**
   * Returns the server status.
   *
   * @return a map with the status fields.
   */
  Map<String, Object> getRepoStatus();


  // ------------------------ Buckets ------------------------

  /**
   * Returns all buckets in the content repo.
   *
   * @return a List with the data of every bucket
   */
  List<Map<String, Object>> getBuckets();

  /**
   * Returns the data of the bucket that matches the given <code>key</code>
   *
   * @param key a single string representing the key of the bucket
   * @return a map with the bucket information
   */
  Map<String, Object> getBucket(String key);


  /**
   * Creates a new bucket with the given <code>key</code>
   *
   * @param key
   * @return
   */
  Map<String, Object> createBucket(String key);


  // ------------------------ Objects ------------------------

  /**
   * Get a URL array which can be used as a links to the specified repo object content via XReproxy-url http header
   * redirection.
   *
   * @param key a single string representing the key of the repo object
   * @return - URL that can be used as a redirect for the file
   * @deprecated use {@link #getRepoObjRedirectURL(org.plos.crepo.model.RepoVersion)} instead.
   */
  @Deprecated
  public List<URL> getRepoObjRedirectURL(String key);

  /**
   * Returns a URL array which can be used as a links to the specified repo object content.
   *
   * @param version the version checksum of the repo object
   * @return a URL array
   */
  public List<URL> getRepoObjRedirectURL(RepoVersion version);

  /**
   * Returns the content of the latest version of an object using the most recent creation date time
   *
   * @param key a single string representing the key of the repo object
   * @return an InputStream of the content
   * @deprecated use {@link #getRepoObj(org.plos.crepo.model.RepoVersion)} or {@link #getRepoObj(org.plos.crepo.model.RepoVersionNumber)} instead.
   */
  @Deprecated
  InputStream getLatestRepoObj(String key);

  /**
   * Returns the content of the repo object specified by <code>key</code> & the <code>versionChecksum</code>
   *
   * @param version the version checksum of the repo object
   * @return an InputStream representing the repo object content.
   */
  InputStream getRepoObj(RepoVersion version);

  /**
   * Returns the content of the repo object specified by <code>key</code> & the <code>versionNumber</code>
   *
   * @param number the version number of the repo object
   * @return an InputStream object with the content.
   */
  InputStream getRepoObj(RepoVersionNumber number);

  /**
   * Returns the meta data of the latest version of an object using the most recent creation date time.
   *
   * @param key a single string representing the key of the repo object
   * @return a map with the repo object meta data.
   * @deprecated use {@link #getRepoObjMeta(org.plos.crepo.model.RepoVersion)} or {@link #getRepoObjMeta(org.plos.crepo.model.RepoVersionNumber)}
   * instead.
   */
  @Deprecated
  Map<String, Object> getRepoObjMetaLatestVersion(String key);

  /**
   * Returns the meta data of the repo object specified by <code>key</code> & <code>versionChecksum</code>
   *
   * @param version the version checksum of the repo object
   * @return a map with the repo object meta data.
   */
  Map<String, Object> getRepoObjMeta(RepoVersion version);

  /**
   * Returns the meta data of the repo object specified by <code>key</code> & <code>versionNumber</code>
   *
   * @param number the version number of the repo object
   * @return a map with the repo object meta data.
   */
  Map<String, Object> getRepoObjMeta(RepoVersionNumber number);

  /**
   * Returns a repo object using the given key <code>key</code> and tag <code>tag</code>
   *
   * @param tag the tag of the repo object.
   * @return a map with the data of the repo object
   */
  Map<String, Object> getRepoObjMeta(RepoVersionTag tag);

  /**
   * Returns the meta data of all the versions for the given repo object, using the repo object key <code>key</code>
   *
   * @param key a single string representing the key of the repo object
   * @return a list of the repo object versions
   */
  List<Map<String, Object>> getRepoObjVersions(String key);

  /**
   * Deletes the latest version of the repo object using object key <code>key</code>
   *
   * @param key a single string representing the key of the repo object
   * @return true if the object was successfully deleted.
   * @deprecated use {@link #deleteRepoObj(org.plos.crepo.model.RepoVersion)} or {@link #deleteRepoObj(org.plos.crepo.model.RepoVersionNumber)}
   * instead.
   */
  @Deprecated
  boolean deleteLatestRepoObj(String key);

  /**
   * Deletes the specific version of a repo object using the key <code>key</code> & the version checksum
   * <code>versionChecksum</code>
   *
   * @param version the version checksum of the repo object
   * @return true if the object was successfully deleted.
   */
  boolean deleteRepoObj(RepoVersion version);

  /**
   * Deletes the specific version of a repo object using the key <code>key</code> & the version checksum
   * <code>versionNumber</code>
   *
   * @param number the version number of the repo object
   * @return true if the object was successfully deleted.
   */
  boolean deleteRepoObj(RepoVersionNumber number);

  /**
   * Creates a repo object using <code>repoObject</code>
   *
   * @param repoObject a RepoObject containing the meta data & content of the new repo object
   * @return a map with the repo object metadata
   */
  Map<String, Object> createRepoObject(RepoObject repoObject);

  /**
   * Versions a repo object using <code>repoObject</code>
   *
   * @param repoObject a RepoObject containing the meta data & content of the new repo object
   * @return a map with the repo object metadata
   */
  Map<String, Object> versionRepoObject(RepoObject repoObject);

  /**
   * Create/version a repo object using <code>repoObject</code> depending if the object already exists or not
   *
   * @param repoObject a RepoObject containing the meta data & content of the new repo object
   * @return a map with the repo object metadata
   */
  Map<String, Object> autoCreateRepoObject(RepoObject repoObject);

  /**
   * Returns all the object in the configured bucket. It uses the offset and limit to paginate the response.
   *
   * @param offset         an int value to indicate the page number of the pagination
   * @param limit          an int value representing the number of objects for each page
   * @param includeDeleted if true the response include deleted objects
   * @param tag            a single string representing the collection tag to filter the response. If it is null, it
   *                       will be ignore.
   * @return a map List with the data of every collection
   */
  List<Map<String, Object>> getRepoObjects(int offset, int limit, boolean includeDeleted, String tag);


  // ------------------------ Collections ------------------------

  /**
   * Creates a new repo collection using the the <code>repoCollection</code> data.
   *
   * @param repoCollection a RepoCollection object containing the data of the new collection
   * @return a map with the data of the collection
   */
  Map<String, Object> createCollection(RepoCollection repoCollection);

  /**
   * Versions an existent repo collection using the <code>repoCollection</code> data.
   *
   * @param repoCollection a RepoCollection object containing the data of the new collection
   * @return a map with the data of the collection
   */
  Map<String, Object> versionCollection(RepoCollection repoCollection);

  /**
   * Deletes a repo collection using the key <code>key</code> and the version checksum <code>versionChecksum</code>
   *
   * @param version the version checksum of the repo collection
   * @return a map with the data of the collection
   */
  boolean deleteCollection(RepoVersion version);

  /**
   * Deletes a repo collection using the key <code>key</code> and the version number <code>versionNumber</code>
   *
   * @param number the version number of the repo collection
   * @return true if the collection was successfully deleted
   */
  boolean deleteCollection(RepoVersionNumber number);

  /**
   * Returns a repo collection object using the given key <code>key</code> and version checksum
   * <code>versionChecksum</code>
   *
   * @param version the version checksum of the repo collection
   * @return a map with the data of the collection
   */
  Map<String, Object> getCollection(RepoVersion version);

  /**
   * Returns a repo collection object using the given key <code>key</code> and version number
   * <code>versionNumber</code>
   *
   * @param number the version number of the repo collection
   * @return a map with the data of the collection
   */
  Map<String, Object> getCollection(RepoVersionNumber number);

  /**
   * Returns a repo collection object using the given key <code>key</code> and tag <code>tag</code> If there are more
   * collections with the same key and tag, it returns the latest one.
   *
   * @param tag the tag of the repo collection.
   * @return a map with the data of the collection
   */
  Map<String, Object> getCollection(RepoVersionTag tag);

  /**
   * Returns all the versions of a repo collection using the given key <code>key</code>
   *
   * @param key a single string representing the key of the repo collection.
   * @return a List with the data of every collection
   */
  List<Map<String, Object>> getCollectionVersions(String key);

  /**
   * Returns all the collections in the configured bucket. It uses the offset and limit to paginate the response.
   *
   * @param offset         an int value to indicate the page number of the pagination
   * @param limit          an int value representing the number of collections for each page
   * @param includeDeleted if true the response include deleted collections
   * @param tag            a single string representing the collection tag to filter the response. If it is null, it
   *                       will be ignore.
   * @return a map List with the data of every collection
   */
  List<Map<String, Object>> getCollections(int offset, int limit, boolean includeDeleted, String tag);

}
