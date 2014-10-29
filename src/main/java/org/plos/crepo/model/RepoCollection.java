package org.plos.crepo.model;

import java.util.Collections;
import java.util.List;

public class RepoCollection {

  protected String key; // what the user specifies
  protected String timestamp;   // created time
  protected String tag;
  protected List<RepoCollectionObject> objects;
  protected String creationDateTime;   // created time

  public RepoCollection(String key, List<RepoCollectionObject> objects){
    this.key = key;
    this.objects = objects;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setCreationDateTime(String creationDateTime) {
    this.creationDateTime = creationDateTime;
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

  public List<RepoCollectionObject> getObjects() {
    return Collections.unmodifiableList(objects);
  }

  public String getCreationDateTime() {
    return creationDateTime;
  }

}


