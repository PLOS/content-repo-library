package org.plos.crepo.model.metadata;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents output to the client, describing a collection.
 */
public class RepoCollectionList extends RepoCollectionMetadata {

  private final ImmutableList<RepoObjectMetadata> objects;

  public RepoCollectionList(String bucketName, Map<String, Object> raw) {
    super(bucketName, raw);
    objects = parseObjects(bucketName, raw);
  }

  private static ImmutableList<RepoObjectMetadata> parseObjects(String bucketName, Map<String, Object> raw) {
    List<Map<String, Object>> rawObjects = (List<Map<String, Object>>) raw.get("objects");
    List<RepoObjectMetadata> builtObjects = new ArrayList<>(rawObjects.size());
    for (Map<String, Object> rawObject : rawObjects) {
      builtObjects.add(new RepoObjectMetadata(bucketName, rawObject));
    }
    return ImmutableList.copyOf(builtObjects);
  }

  public ImmutableList<RepoObjectMetadata> getObjects() {
    return objects;
  }

}
