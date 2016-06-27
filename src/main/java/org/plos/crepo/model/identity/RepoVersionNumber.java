package org.plos.crepo.model.identity;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Objects;

/**
 * An identifier for a version of a repo object or collection, using the version number.
 */
public final class RepoVersionNumber {

  private final RepoId id;
  private final int number;

  private RepoVersionNumber(RepoId id, int number) {
    this.id = Objects.requireNonNull(id);
    this.number = number;
    Preconditions.checkArgument(this.number >= 0);
  }

  public static RepoVersionNumber create(RepoId id, int number) {
    return new RepoVersionNumber(id, number);
  }

  public static RepoVersionNumber create(String bucketName, String key, int number) {
    return create(RepoId.create(bucketName, key), number);
  }


  public RepoId getId() {
    return id;
  }

  public int getNumber() {
    return number;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RepoVersionNumber that = (RepoVersionNumber) o;
    return number == that.number && id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return 31 * id.hashCode() + number;
  }

  @Override
  public String toString() {
    return String.format("%s(\"%s\", \"%s\", \"%s\")", getClass().getSimpleName(),
        StringEscapeUtils.escapeJava(id.getBucketName()), StringEscapeUtils.escapeJava(id.getKey()), number);
  }
}
