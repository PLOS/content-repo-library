package org.plos.crepo.model.metadata;

import java.util.Map;

/**
 * Represents output to the client, describing a collection.
 */
public class RepoCollectionMetadata extends RepoMetadata {

  public RepoCollectionMetadata(Map<String, Object> raw) {
    super(raw);
  }

}
