package org.plos.crepo.model.identity;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;

import java.util.UUID;

/**
 * The native identifier for a version of a repo object or collection. Uses a key and UUID.
 */
public class RepoVersion {

  private final String key; // what the user specifies
  private final UUID uuid;

  private RepoVersion(String key, UUID uuid) {
    validateKey(key);
    this.key = Preconditions.checkNotNull(key);
    this.uuid = Preconditions.checkNotNull(uuid);
  }

  public static void validateKey(String key) {
    if (StringUtils.isEmpty(key)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyKey)
          .build();
    }
  }

  /**
   * Represent a version of a repo object.
   *
   * @param key  the object key
   * @param uuid the version's UUID as a string
   * @return the repo object version
   * @throws IllegalArgumentException if {@code uuid} is not a valid UUID
   */
  public static RepoVersion create(String key, String uuid) {
    if (StringUtils.isEmpty(uuid)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyUuid).build();
    }
    return create(key, UUID.fromString(uuid));
  }

  /**
   * Represent a version of a repo object.
   *
   * @param key  the object key
   * @param uuid the version's UUID
   * @return the repo object version
   */
  public static RepoVersion create(String key, UUID uuid) {
    return new RepoVersion(key, uuid);
  }

  public String getKey() {
    return key;
  }

  public UUID getUuid() {
    return uuid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoVersion that = (RepoVersion) o;

    if (!key.equals(that.key)) return false;
    if (!uuid.equals(that.uuid)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = key.hashCode();
    result = 31 * result + uuid.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s(\"%s\", \"%s\")", getClass().getSimpleName(), StringEscapeUtils.escapeJava(key), uuid);
  }
}
