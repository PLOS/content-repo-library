package org.plos.crepo.model;

import com.google.common.base.Preconditions;
import com.google.common.io.BaseEncoding;

public class RepoObjectVersion {

  private final String key; // what the user specifies
  private final byte[] versionChecksum;

  private RepoObjectVersion(String key, byte[] versionChecksum) {
    this.key = Preconditions.checkNotNull(key);
    this.versionChecksum = Preconditions.checkNotNull(versionChecksum);
    validate(this.versionChecksum);
  }

  private static void validate(byte[] versionChecksum) {
    // TODO
  }

  /**
   * Represent a version of a repo object.
   *
   * @param key             the object key
   * @param versionChecksum the version's checksum in hexadecimal
   * @return the repo object version
   * @throws IllegalArgumentException if {@code versionChecksum} is not valid hexadecimal
   */
  public static RepoObjectVersion createFromHex(String key, String versionChecksum) {
    return new RepoObjectVersion(key, BaseEncoding.base16().decode(versionChecksum));
  }

  /**
   * Represent a version of a repo object.
   *
   * @param key             the object key
   * @param versionChecksum the version's checksum
   * @return
   */
  public static RepoObjectVersion create(String key, byte[] versionChecksum) {
    byte[] defensiveCopy = versionChecksum.clone(); // prevent alterations after building
    return new RepoObjectVersion(key, defensiveCopy);
  }

  public String getKey() {
    return key;
  }

  public String getHexadecimalVersionChecksum() {
    return BaseEncoding.base16().encode(versionChecksum);
  }

  public byte[] getVersionChecksum() {
    return versionChecksum.clone(); // prevent alterations by foreign code
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoObjectVersion that = (RepoObjectVersion) o;

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
