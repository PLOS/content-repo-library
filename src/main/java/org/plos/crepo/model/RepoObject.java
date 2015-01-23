package org.plos.crepo.model;

import org.apache.commons.lang3.StringUtils;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;

import java.io.File;
import java.sql.Timestamp;
import java.util.Arrays;

public class RepoObject {

  private final String key; // what the user specifies
  private final String downloadName;
  private final String contentType;
  private final String tag;
  private final Timestamp creationDate;
  private final Timestamp timestamp;   // last modification time
  private final File fileContent;
  private final byte[] byteContent;

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

  public static void validateObjectKey(String key) {
    if (StringUtils.isEmpty(key)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyObjectKey)
          .build();
    }
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoObject that = (RepoObject) o;

    if (!Arrays.equals(byteContent, that.byteContent)) return false;
    if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) return false;
    if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
    if (downloadName != null ? !downloadName.equals(that.downloadName) : that.downloadName != null) return false;
    if (fileContent != null ? !fileContent.equals(that.fileContent) : that.fileContent != null) return false;
    if (key != null ? !key.equals(that.key) : that.key != null) return false;
    if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;
    if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = key != null ? key.hashCode() : 0;
    result = 31 * result + (downloadName != null ? downloadName.hashCode() : 0);
    result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
    result = 31 * result + (tag != null ? tag.hashCode() : 0);
    result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
    result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
    result = 31 * result + (fileContent != null ? fileContent.hashCode() : 0);
    result = 31 * result + (byteContent != null ? Arrays.hashCode(byteContent) : 0);
    return result;
  }
}
