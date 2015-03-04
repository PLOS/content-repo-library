package org.plos.crepo.model;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import java.util.Collection;

/**
 * Represents input from the client, describing a collection to create or modify.
 */
public class RepoCollection {

  private final String key; // what the user specifies
  private final ImmutableCollection<RepoVersion> objects;
  private final String timestamp;   // created time
  private final String tag;
  private final String creationDateTime;   // created time

  private RepoCollection(Builder builder) {
    this.key = Preconditions.checkNotNull(builder.key);
    this.objects = ImmutableList.copyOf(builder.objects);

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

  public String getCreationDateTime() {
    return creationDateTime;
  }


  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String key;
    private String timestamp;
    private String tag;
    private Collection<RepoVersion> objects;
    private String creationDateTime;

    private Builder() {
    }

    public RepoCollection build() {
      return new RepoCollection(this);
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

    if (creationDateTime != null ? !creationDateTime.equals(that.creationDateTime) : that.creationDateTime != null)
      return false;
    if (!key.equals(that.key)) return false;
    if (!objects.equals(that.objects)) return false;
    if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;
    if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = key.hashCode();
    result = 31 * result + objects.hashCode();
    result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
    result = 31 * result + (tag != null ? tag.hashCode() : 0);
    result = 31 * result + (creationDateTime != null ? creationDateTime.hashCode() : 0);
    return result;
  }
}


