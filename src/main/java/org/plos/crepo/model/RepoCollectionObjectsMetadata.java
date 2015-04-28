package org.plos.crepo.model;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents output to the client, describing a collection.
 */
public class RepoCollectionObjectsMetadata extends RepoMetadata {

  private final ImmutableList<RepoObjectMetadata> objects;

  public RepoCollectionObjectsMetadata(Map<String, Object> raw) {
    super(raw);
    objects = parseObjects(raw);
  }

  private static ImmutableList<RepoObjectMetadata> parseObjects(Map<String, Object> raw) {
    if (!raw.containsKey("objects")) {
      return null;
    }
    List<Map<String, Object>> rawObjects = (List<Map<String, Object>>) raw.get("objects");
    List<RepoObjectMetadata> builtObjects = new ArrayList<>(rawObjects.size());
    for (Map<String, Object> rawObject : rawObjects) {
      builtObjects.add(new RepoObjectMetadata(rawObject));
    }
    return ImmutableList.copyOf(builtObjects);
  }

  public ImmutableList<RepoObjectMetadata> getObjects() {
    return objects;
  }

}
