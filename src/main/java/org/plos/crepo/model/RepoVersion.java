package org.plos.crepo.model;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.StringUtils;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;

/**
 * The native identifier for a version of a repo object or collection. Uses a checksum.
 */
public class RepoVersion {

  private final String key; // what the user specifies
  private final byte[] versionChecksum;

  private RepoVersion(String key, byte[] versionChecksum) {
    validateKey(key);
    this.key = Preconditions.checkNotNull(key);
    this.versionChecksum = Preconditions.checkNotNull(versionChecksum);
    validate(this.versionChecksum);
  }

  private static void validate(byte[] versionChecksum) {
    // TODO
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
   * @param key             the object key
   * @param versionChecksum the version's checksum in hexadecimal
   * @return the repo object version
   * @throws IllegalArgumentException if {@code versionChecksum} is not valid hexadecimal
   */
  public static RepoVersion createFromHex(String key, String versionChecksum) {
    validateObjectCks(versionChecksum);
    return new RepoVersion(key, BaseEncoding.base16().decode(versionChecksum));
  }

  /**
   * Represent a version of a repo object.
   *
   * @param key             the object key
   * @param versionChecksum the version's checksum
   * @return
   */
  public static RepoVersion create(String key, byte[] versionChecksum) {
    byte[] defensiveCopy = versionChecksum.clone(); // prevent alterations after building
    return new RepoVersion(key, defensiveCopy);
  }

  public String getKey() {
    return key;
  }

  public String getHexVersionChecksum() {
    return BaseEncoding.base16().encode(versionChecksum);
  }

  public byte[] getVersionChecksum() {
    return versionChecksum.clone(); // prevent alterations by foreign code
  }

  private static void validateObjectCks(String versionChecksum) {
    if (StringUtils.isEmpty(versionChecksum)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyCks)
          .build();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoVersion that = (RepoVersion) o;

    if (!key.equals(that.key)) return false;
    if (!versionChecksum.equals(that.versionChecksum)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = key.hashCode();
    result = 31 * result + versionChecksum.hashCode();
    return result;
  }

}
