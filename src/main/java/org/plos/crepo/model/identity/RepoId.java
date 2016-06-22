package org.plos.crepo.model.identity;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;

import java.util.Objects;

/**
 * The native identifier for all version of a repo object or collection.
 */
public final class RepoId {

  private final String bucketName;
  private final String key;

  private RepoId(String bucketName, String key) {
    this.bucketName = Objects.requireNonNull(bucketName);
    this.key = Objects.requireNonNull(key);
    validateKey(this.key);
  }

  private static void validateKey(String key) {
    if (StringUtils.isEmpty(key)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyKey)
          .build();
    }
  }

  public static RepoId create(String bucketName, String key) {
    return new RepoId(bucketName, key);
  }


  public String getBucketName() {
    return bucketName;
  }

  public String getKey() {
    return key;
  }


  @Override
  public boolean equals(Object o) {
    return this == o || o != null && getClass() == o.getClass()
        && bucketName.equals(((RepoId) o).bucketName) && key.equals(((RepoId) o).key);
  }

  @Override
  public int hashCode() {
    return 31 * bucketName.hashCode() + key.hashCode();
  }

  @Override
  public String toString() {
    return String.format("%s(\"%s\", \"%s\")", getClass().getSimpleName(),
        StringEscapeUtils.escapeJava(bucketName), StringEscapeUtils.escapeJava(key));
  }
}
