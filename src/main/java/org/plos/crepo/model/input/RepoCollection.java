package org.plos.crepo.model.input;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import org.plos.crepo.model.identity.RepoVersion;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents input from the client, describing a collection to create or modify.
 */
public class RepoCollection {

  private final String bucketName;
  private final String key; // what the user specifies
  private final ImmutableCollection<RepoVersion> objects;
  private final String timestamp;   // created time
  private final String tag;
  private final String userMetadata;
  private final String creationDateTime;   // created time

  private RepoCollection(Builder builder) {
    this.bucketName = Objects.requireNonNull(builder.bucketName);
    this.key = Objects.requireNonNull(builder.key);
    this.objects = ImmutableList.copyOf(builder.objects);
    this.userMetadata = builder.userMetadata;

    this.timestamp = builder.timestamp;
    this.tag = builder.tag;
    this.creationDateTime = builder.creationDateTime;
  }

  public static RepoCollection create(String key, Collection<RepoVersion> objects) {
    return builder()
        .setKey(key)
        .setObjects(objects)
        .build();
  }

  public String getBucketName() {
    return bucketName;
  }

  public String getKey() {
    return key;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getTag() {
    return tag;
  }

  public ImmutableCollection<RepoVersion> getObjects() {
    return objects;
  }

  public String getUserMetadata() {
    return userMetadata;
  }

  public String getCreationDateTime() {
    return creationDateTime;
  }


  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String bucketName;
    private String key;
    private String timestamp;
    private String tag;
    private Collection<RepoVersion> objects;
    private String userMetadata;
    private String creationDateTime;

    private Builder() {
    }

    public RepoCollection build() {
      return new RepoCollection(this);
    }

    public String getBucketName() {
      return bucketName;
    }

    public Builder setBucketName(String bucketName) {
      this.bucketName = bucketName;
      return this;
    }

    public String getKey() {
      return key;
    }

    public Builder setKey(String key) {
      this.key = key;
      return this;
    }

    public String getTimestamp() {
      return timestamp;
    }

    public Builder setTimestamp(String timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public String getTag() {
      return tag;
    }

    public Builder setTag(String tag) {
      this.tag = tag;
      return this;
    }

    public Collection<RepoVersion> getObjects() {
      return objects;
    }

    public Builder setObjects(Collection<RepoVersion> objects) {
      this.objects = objects;
      return this;
    }

    public String getUserMetadata() {
      return userMetadata;
    }

    public Builder setUserMetadata(String userMetadata) {
      this.userMetadata = userMetadata;
      return this;
    }

    public String getCreationDateTime() {
      return creationDateTime;
    }

    public Builder setCreationDateTime(String creationDateTime) {
      this.creationDateTime = creationDateTime;
      return this;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoCollection that = (RepoCollection) o;

    if (bucketName != null ? !bucketName.equals(that.bucketName) : that.bucketName != null) return false;
    if (key != null ? !key.equals(that.key) : that.key != null) return false;
    if (objects != null ? !objects.equals(that.objects) : that.objects != null) return false;
    if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
    if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;
    if (userMetadata != null ? !userMetadata.equals(that.userMetadata) : that.userMetadata != null) return false;
    return creationDateTime != null ? creationDateTime.equals(that.creationDateTime) : that.creationDateTime == null;

  }

  @Override
  public int hashCode() {
    int result = bucketName != null ? bucketName.hashCode() : 0;
    result = 31 * result + (key != null ? key.hashCode() : 0);
    result = 31 * result + (objects != null ? objects.hashCode() : 0);
    result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
    result = 31 * result + (tag != null ? tag.hashCode() : 0);
    result = 31 * result + (userMetadata != null ? userMetadata.hashCode() : 0);
    result = 31 * result + (creationDateTime != null ? creationDateTime.hashCode() : 0);
    return result;
  }
}


