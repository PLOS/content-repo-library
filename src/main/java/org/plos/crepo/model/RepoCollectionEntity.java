package org.plos.crepo.model;

import com.google.common.collect.ImmutableCollection;

public class RepoCollectionEntity {

  private final String key; // what the user specifies
  private final ImmutableCollection<RepoVersion> objects;
  private final String timestamp;   // created time
  private final String tag;
  private final String userMetadata;
  private final String creationDateTime;   // created time

  private final String bucketName; // what the user specifies
  private final String create;   // created time


  public RepoCollectionEntity(RepoCollection collection, String bucketName, String create) {
    this.bucketName = bucketName;
    this.create = create;

    this.key = collection.getKey();
    this.objects = collection.getObjects();
    this.timestamp = collection.getTimestamp();
    this.tag = collection.getTag();
    this.userMetadata = collection.getUserMetadata();
    this.creationDateTime = collection.getCreationDateTime();
  }

  public String getBucketName() {
    return bucketName;
  }

  public String getCreate() {
    return create;
  }

  public String getKey() {
    return key;
  }

  public ImmutableCollection<RepoVersion> getObjects() {
    return objects;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getTag() {
    return tag;
  }

  public String getUserMetadata() {
    return userMetadata;
  }

  public String getCreationDateTime() {
    return creationDateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoCollectionEntity that = (RepoCollectionEntity) o;

    if (bucketName != null ? !bucketName.equals(that.bucketName) : that.bucketName != null) return false;
    if (create != null ? !create.equals(that.create) : that.create != null) return false;
    if (creationDateTime != null ? !creationDateTime.equals(that.creationDateTime) : that.creationDateTime != null)
      return false;
    if (key != null ? !key.equals(that.key) : that.key != null) return false;
    if (objects != null ? !objects.equals(that.objects) : that.objects != null) return false;
    if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;
    if (userMetadata != null ? !userMetadata.equals(that.userMetadata) : that.userMetadata != null) return false;
    if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = key != null ? key.hashCode() : 0;
    result = 31 * result + (objects != null ? objects.hashCode() : 0);
    result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
    result = 31 * result + (tag != null ? tag.hashCode() : 0);
    result = 31 * result + (userMetadata != null ? userMetadata.hashCode() : 0);
    result = 31 * result + (creationDateTime != null ? creationDateTime.hashCode() : 0);
    result = 31 * result + (bucketName != null ? bucketName.hashCode() : 0);
    result = 31 * result + (create != null ? create.hashCode() : 0);
    return result;
  }

}


