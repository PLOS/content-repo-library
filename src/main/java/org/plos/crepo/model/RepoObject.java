package org.plos.crepo.model;

import java.io.File;
import java.sql.Timestamp;

public class RepoObject {

  private String key; // what the user specifies
  private String downloadName;
  private String contentType;
  private String tag;
  private Timestamp creationDate;
  private Timestamp timestamp;   // last modification time
  private File fileContent;
  private byte[] byteContent;

  private RepoObject(RepoObjectBuilder builder){
    this.key = builder.key;
    this.downloadName = builder.downloadName;
    this.contentType = builder.contentType;
    this.tag = builder.tag;
    this.creationDate = builder.creationDate;
    this.timestamp = builder.timestamp;
    this.fileContent = builder.fileContent;
    this.byteContent = builder.byteContent;
  }

  public String getKey() {
    return key;
  }

  public String getDownloadName() {
    return downloadName;
  }

  public String getContentType() {
    return contentType;
  }

  public String getTag() {
    return tag;
  }

  public Timestamp getCreationDate() {
    return creationDate;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public File getFileContent() {
    return fileContent;
  }

  public byte[] getByteContent() {
    return byteContent;
  }

  public static class RepoObjectBuilder {

    private String key; // what the user specifies
    private String downloadName;
    private String contentType;
    private String tag;
    private Timestamp creationDate;
    private Timestamp timestamp;   // last modification time
    private File fileContent;
    private byte[] byteContent;

    public RepoObjectBuilder(String key){
      this.key = key;
    }

    public RepoObjectBuilder downloadName(String downloadName){
      this.downloadName = downloadName;
      return this;
    }

    public RepoObjectBuilder contentType(String contentType){
      this.contentType = contentType;
      return this;
    }

    public RepoObjectBuilder tag(String tag){
      this.tag = tag;
      return this;
    }

    public RepoObjectBuilder creationDate(Timestamp creationDate){
      this.creationDate = creationDate;
      return this;
    }

    public RepoObjectBuilder timestamp(Timestamp timestamp){
      this.timestamp = timestamp;
      return this;
    }

    public RepoObjectBuilder fileContent(File fileContent){
      this.fileContent = fileContent;
      return this;
    }

    public RepoObjectBuilder byteContent(byte[] byteContent){
      this.byteContent = byteContent;
      return this;
    }

    public RepoObject build(){
      return new RepoObject(this);
    }

  }

}
