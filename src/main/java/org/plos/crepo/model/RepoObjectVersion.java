package org.plos.crepo.model;

public class RepoObjectVersion {

  private final String key; // what the user specifies
  private final String versionChecksum;

  public RepoObjectVersion(String key, String versionChecksum) {
    this.key = key;
    this.versionChecksum = versionChecksum;
  }

  public String getKey() {
    return key;
  }

  public String getVersionChecksum() {
    return versionChecksum;
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


