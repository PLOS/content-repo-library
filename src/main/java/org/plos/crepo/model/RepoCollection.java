/*
 * Copyright 2017 Public Library of Science
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

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
  private final String userMetadata;
  private final String creationDateTime;   // created time

  private RepoCollection(Builder builder) {
    this.key = Preconditions.checkNotNull(builder.key);
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

    if (creationDateTime != null ? !creationDateTime.equals(that.creationDateTime) : that.creationDateTime != null)
      return false;
    if (!key.equals(that.key)) return false;
    if (!objects.equals(that.objects)) return false;
    if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;
    if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
    if (userMetadata != null ? !userMetadata.equals(that.userMetadata) : that.userMetadata != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = key.hashCode();
    result = 31 * result + objects.hashCode();
    result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
    result = 31 * result + (tag != null ? tag.hashCode() : 0);
    result = 31 * result + (userMetadata != null ? userMetadata.hashCode() : 0);
    result = 31 * result + (creationDateTime != null ? creationDateTime.hashCode() : 0);
    return result;
  }
}


