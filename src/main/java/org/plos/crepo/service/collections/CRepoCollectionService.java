package org.plos.crepo.service.collections;

import org.plos.crepo.model.RepoCollection;

import java.util.List;
import java.util.Map;

public interface CRepoCollectionService {

  Map<String, Object> createCollection(RepoCollection repoCollection);

  Map<String, Object> versionCollection(RepoCollection repoCollection);

  Boolean deleteCollectionUsingVersionCks(String key, String versionChecksum);

  Boolean deleteCollectionUsingVersionNumb(String key, int versionNumber);

  Map<String, Object> getCollectionUsingVersionCks(String key, String versionChecksum);

  Map<String, Object> getCollectionUsingVersionNumber(String key, int versionNumber);

  Map<String, Object> getCollectionUsingTag(String key, String tag);

  List<Map<String, Object>> getCollectionVersions(String key);

  List<Map<String, Object>> getCollections(int offset, int limit, boolean includeDeleted, String tag);

}
