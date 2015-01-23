package org.plos.crepo.model;

import com.google.common.base.Preconditions;

/**
 * An identifier for a version of a repo object, using the version number.
 */
public class RepoObjectVersionNumber {

  private final String key;
  private final int number;

  public RepoObjectVersionNumber(String key, int number) {
    RepoObject.validateObjectKey(key);
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
    RepoObjectVersionNumber that = (RepoObjectVersionNumber) o;
    return number == that.number && key.equals(that.key);
  }

  @Override
  public int hashCode() {
    return 31 * key.hashCode() + number;
  }
}
