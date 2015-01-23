package org.plos.crepo.model;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;

/**
 * An identifier for a version of a repo object, using a user-defined tag.
 */
public class RepoObjectVersionTag {

  private final String key;
  private final String tag;

  public RepoObjectVersionTag(String key, String tag) {
    RepoObject.validateObjectKey(key);
    validateObjectTag(tag);
    this.key = Preconditions.checkNotNull(key);
    this.tag = Preconditions.checkNotNull(tag);
  }

  public String getKey() {
    return key;
  }

  public String getTag() {
    return tag;
  }

  private static void validateObjectTag(String tag) {
    if (StringUtils.isEmpty(tag)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyObjectTag)
          .build();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RepoObjectVersionTag that = (RepoObjectVersionTag) o;
    return key.equals(that.key) && tag.equals(that.tag);
  }

  @Override
  public int hashCode() {
    return 31 * key.hashCode() + tag.hashCode();
  }
}
