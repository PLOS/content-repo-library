package org.plos.crepo.model;

public class RepoCollectionEntity extends RepoCollection {

  private String bucketName; // what the user specifies
  private String create;   // created time


  public RepoCollectionEntity(RepoCollection repoCollection, String bucketName, String create){
    super(repoCollection.getKey(), repoCollection.getObjects());
    this.timestamp = repoCollection.getTimestamp();
    this.tag = repoCollection.getTag();
    this.creationDateTime = repoCollection.getCreationDateTime();
    this.bucketName = bucketName;
    this.create = create;
  }

  public String getBucketName() {
    return bucketName;
  }

  public String getCreate() {
    return create;
  }
}


