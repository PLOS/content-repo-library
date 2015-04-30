package org.plos.crepo.model;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents output to the client, describing a collection.
 */
public class RepoCollectionMetadata extends RepoMetadata {

  public RepoCollectionMetadata(Map<String, Object> raw) {
    super(raw);
  }

}
