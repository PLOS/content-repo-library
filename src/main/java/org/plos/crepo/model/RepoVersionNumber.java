package org.plos.crepo.model;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * An identifier for a version of a repo object or collection, using the version number.
 */
public class RepoVersionNumber {

  private final String key;
  private final int number;

  public RepoVersionNumber(String key, int number) {
    RepoVersion.validateKey(key);
    this.key = Preconditions.checkNotNull(key);
    this.number = number;
    Preconditions.checkArgument(this.number >= 0);
  }

  public String getKey() {
    return key;
  }

  public int getNumber() {
    return number;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RepoVersionNumber that = (RepoVersionNumber) o;
    return number == that.number && key.equals(that.key);
  }

  @Override
  public int hashCode() {
    return 31 * key.hashCode() + number;
  }

  @Override
  public String toString() {
    return String.format("%s(\"%s\", %d)", getClass().getSimpleName(), StringEscapeUtils.escapeJava(key), number);
  }
}
