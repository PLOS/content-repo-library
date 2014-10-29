package org.plos.crepo.model;

public class RepoCollectionObject {

  private String key; // what the user specifies
  private String versionChecksum;

  public RepoCollectionObject(String key, String versionChecksum) {
    this.key = key;
    this.versionChecksum = versionChecksum;
  }

  public String getKey() {
    return key;
  }

  public String getVersionChecksum() {
    return versionChecksum;
  }

}


