package org.plos.crepo.service.collections;

import org.plos.crepo.model.RepoCollection;

import java.util.List;
import java.util.Map;

/**
 * Content Repo Collection service interface.
 */
public interface CRepoCollectionService {

  /**
   * Creates a new repo collection using the the <code>repoCollection</code> data.
   * @param repoCollection a RepoCollection object containing the data of the new collection
   * @return a map with the data of the collection
   */
  Map<String, Object> createCollection(RepoCollection repoCollection);

  /**
   * Versions an existent repo collection using the <code>repoCollection</code> data.
   * @param repoCollection a RepoCollection object containing the data of the new collection
   * @return a map with the data of the collection
   */
  Map<String, Object> versionCollection(RepoCollection repoCollection);

  /**
   * Deletes a repo collection using the key <code>key</code> and the version checksum
   * <code>versionChecksum</code>
   * @param key a single string representing the key of the repo collection.
   * @param versionChecksum a single string representing the version checksum of the repo collection
   * @return a map with the data of the collection
   */
  Boolean deleteCollectionUsingVersionCks(String key, String versionChecksum);

  /**
   * Deletes a repo collection using the key <code>key</code> and the version number
   * <code>versionNumber</code>
   * @param key a single string representing the key of the repo collection.
   * @param versionNumber an int value representing the version number of the repo collection
   * @return true if the collection was successfully deleted
   */
  Boolean deleteCollectionUsingVersionNumb(String key, int versionNumber);

  /**
   * Returns a repo collection object using the given key <code>key</code> and version checksum
   * <code>versionChecksum</code>
   * @param key a single string representing the key of the repo collection.
   * @param versionChecksum a single string representing the version checksum of the repo collection
   * @return a map with the data of the collection
   */
  Map<String, Object> getCollectionUsingVersionCks(String key, String versionChecksum);

  /**
   * Returns a repo collection object using the given key <code>key</code> and version number
   * <code>versionNumber</code>
   * @param key a single string representing the key of the repo collection.
   * @param versionNumber an int value representing the version number of the repo collection
   * @return a map with the data of the collection
   */
  Map<String, Object> getCollectionUsingVersionNumber(String key, int versionNumber);

  /**
   * Returns a repo collection object using the given key <code>key</code> and tag <code>tag</code>
   * If there are more collections with the same key and tag, it returns the latest one.
   * @param key a single string representing the key of the repo collection.
   * @param tag a single string representing the tag of the repo collection.
   * @return a map with the data of the collection
   */
  Map<String, Object> getCollectionUsingTag(String key, String tag);

  /**
   * Returns all the versions of a repo collection using the given key <code>key</code>
   * @param key a single string representing the key of the repo collection.
   * @return a List with the data of every collection
   */
  List<Map<String, Object>> getCollectionVersions(String key);

  /**
   * Returns all the collections in the configured bucket. It uses the offset and limit to
   * paginate the response.
   * @param offset an int value to indicate the page number of the pagination
   * @param limit an int value representing the number of collections for each page
   * @param includeDeleted if true the response include deleted collections
   * @param tag a single string representing the collection tag to filter the response. If it is null,
   *            it will be ignore.
   *
   * @return a map List with the data of every collection
   */
  List<Map<String, Object>> getCollections(int offset, int limit, boolean includeDeleted, String tag);

}
