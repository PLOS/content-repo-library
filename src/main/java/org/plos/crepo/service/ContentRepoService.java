package org.plos.crepo.service;

import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoCollectionList;
import org.plos.crepo.model.RepoCollectionMetadata;
import org.plos.crepo.model.RepoObject;
import org.plos.crepo.model.RepoObjectMetadata;
import org.plos.crepo.model.RepoVersion;
import org.plos.crepo.model.RepoVersionNumber;
import org.plos.crepo.model.RepoVersionTag;

import java.io.InputStream;
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
   * Returns the content of the latest version of an object using the most recent creation date time
   *
   * @param key a single string representing the key of the repo object
   * @return an InputStream of the content
   * @deprecated use {@link #getRepoObject(org.plos.crepo.model.RepoVersion)} or {@link
   * #getRepoObject(org.plos.crepo.model.RepoVersionNumber)} instead.
   */
  @Deprecated
  InputStream getLatestRepoObject(String key);

  /**
   * Returns the content of the a repo object version, specified by key and UUID
   *
   * @param version the version of the repo object
   * @return an InputStream representing the repo object content.
   */
  InputStream getRepoObject(RepoVersion version);

  /**
   * Returns the content of the repo object specified by <code>key</code> & the <code>versionNumber</code>
   *
   * @param number the version number of the repo object
   * @return an InputStream object with the content.
   */
  InputStream getRepoObject(RepoVersionNumber number);

  /**
   * Returns the meta data of the latest version of an object using the most recent creation date time.
   *
   * @param key a single string representing the key of the repo object
   * @return a map with the repo object meta data.
   * @deprecated use {@link #getRepoObjectMetadata(org.plos.crepo.model.RepoVersion)} or {@link
   * #getRepoObjectMetadata(org.plos.crepo.model.RepoVersionNumber)} instead.
   */
  @Deprecated
  RepoObjectMetadata getLatestRepoObjectMetadata(String key);

  /**
   * Returns the meta data of the repo object version, specified by key and UUID
   *
   * @param version the version of the repo object
   * @return a map with the repo object meta data.
   */
  RepoObjectMetadata getRepoObjectMetadata(RepoVersion version);

  /**
   * Returns the meta data of the repo object specified by <code>key</code> & <code>versionNumber</code>
   *
   * @param number the version number of the repo object
   * @return a map with the repo object meta data.
   */
  RepoObjectMetadata getRepoObjectMetadata(RepoVersionNumber number);

  /**
   * Returns a repo object using the given key <code>key</code> and tag <code>tag</code>
   *
   * @param tagObj the tag of the repo object.
   * @return a map with the data of the repo object
   */
  RepoObjectMetadata getRepoObjectMetadata(RepoVersionTag tagObj);

  /**
   * Returns the meta data of all the versions for the given repo object, using the repo object key <code>key</code>
   *
   * @param key a single string representing the key of the repo object
   * @return a list of the repo object versions
   */
  List<RepoObjectMetadata> getRepoObjectVersions(String key);

  /**
   * Deletes the latest version of the repo object using object key <code>key</code>
   *
   * @param key a single string representing the key of the repo object
   * @return true if the object was successfully deleted.
   * @deprecated use {@link #deleteRepoObject(org.plos.crepo.model.RepoVersion)} or {@link
   * #deleteRepoObject(org.plos.crepo.model.RepoVersionNumber)} instead.
   */
  @Deprecated
  boolean deleteLatestRepoObject(String key);

  /**
   * Deletes the specific version of a repo object using the key and UUID
   *
   * @param version the version of the repo object
   * @return true if the object was successfully deleted.
   */
  boolean deleteRepoObject(RepoVersion version);

  /**
   * Deletes the specific version of a repo object using the key and UUID
   *
   * @param number the version number of the repo object
   * @return true if the object was successfully deleted.
   */
  boolean deleteRepoObject(RepoVersionNumber number);

  /**
   * Creates a repo object using <code>repoObject</code>
   *
   * @param repoObject a RepoObject containing the meta data & content of the new repo object
   * @return a map with the repo object metadata
   */
  RepoObjectMetadata createRepoObject(RepoObject repoObject);

  /**
   * Versions a repo object using <code>repoObject</code>
   *
   * @param repoObject a RepoObject containing the meta data & content of the new repo object
   * @return a map with the repo object metadata
   */
  RepoObjectMetadata versionRepoObject(RepoObject repoObject);

  /**
   * Create/version a repo object using <code>repoObject</code> depending if the object already exists or not
   *
   * @param repoObject a RepoObject containing the meta data & content of the new repo object
   * @return a map with the repo object metadata
   */
  RepoObjectMetadata autoCreateRepoObject(RepoObject repoObject);

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
  List<RepoObjectMetadata> getRepoObjects(int offset, int limit, boolean includeDeleted, String tag);


  // ------------------------ Collections ------------------------

  /**
   * Creates a new repo collection using the the <code>repoCollection</code> data.
   *
   * @param repoCollection a RepoCollection object containing the data of the new collection
   * @return a map with the data of the collection
   */
  RepoCollectionList createCollection(RepoCollection repoCollection);

  /**
   * Versions an existent repo collection using the <code>repoCollection</code> data.
   *
   * @param repoCollection a RepoCollection object containing the data of the new collection
   * @return a map with the data of the collection
   */
  RepoCollectionList versionCollection(RepoCollection repoCollection);

  /**
   * Creates or versions a repo collection using the the <code>repoCollection</code> data regardless of whether it
   * exists.
   *
   * @param repoCollection a RepoCollection object containing the data of the collection
   * @return a map with the data of the collection
   */
  RepoCollectionList autoCreateCollection(RepoCollection repoCollection);

  /**
   * Deletes a repo collection version, specified by key and UUID
   *
   * @param version the version of the repo collection
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
   * Returns a repo collection version, specified by key and UUID
   *
   * @param version the version of the repo collection
   * @return a map with the data of the collection
   */
  RepoCollectionList getCollection(RepoVersion version);

  /**
   * Returns a repo collection object using the given key <code>key</code> and version number
   * <code>versionNumber</code>
   *
   * @param number the version number of the repo collection
   * @return a map with the data of the collection
   */
  RepoCollectionList getCollection(RepoVersionNumber number);

  /**
   * Returns a repo collection object using the given key <code>key</code> and tag <code>tag</code> If there are more
   * collections with the same key and tag, it returns the latest one.
   *
   * @param tagObj the tag of the repo collection.
   * @return a map with the data of the collection
   */
  RepoCollectionList getCollection(RepoVersionTag tagObj);

  /**
   * Returns all the versions of a repo collection using the given key <code>key</code>
   *
   * @param key a single string representing the key of the repo collection.
   * @return a List with the data of every collection
   */
  List<RepoCollectionList> getCollectionVersions(String key);

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
  List<RepoCollectionMetadata> getCollections(int offset, int limit, boolean includeDeleted, String tag);

}
