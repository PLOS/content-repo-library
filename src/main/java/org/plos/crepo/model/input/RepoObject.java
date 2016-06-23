package org.plos.crepo.model.input;

import com.google.common.base.Preconditions;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.Objects;

public class RepoObject {

  public static interface ContentAccessor {
    InputStream open() throws IOException;
  }

  private static class FileAccessor implements ContentAccessor {
    private final File file;

    private FileAccessor(File file) {
      this.file = Preconditions.checkNotNull(file);
    }

    @Override
    public InputStream open() throws FileNotFoundException {
      return new FileInputStream(file);
    }
  }

  private static class ByteArrayAccessor implements ContentAccessor {
    private final byte[] bytes;

    private ByteArrayAccessor(byte[] bytes) {
      this.bytes = Preconditions.checkNotNull(bytes);
    }

    @Override
    public InputStream open() {
      return new ByteArrayInputStream(bytes);
    }
  }


  private final String bucketName;
  private final String key; // what the user specifies
  private final String downloadName;
  private final String contentType;
  private final String tag;
  private final Timestamp creationDate;
  private final Timestamp timestamp;   // last modification time
  private final String userMetadata;
  private final ContentAccessor contentAccessor;

  private RepoObject(Builder builder) {
    this.bucketName = builder.bucketName;
    this.key = builder.key;
    this.downloadName = builder.downloadName;
    this.contentType = builder.contentType;
    this.tag = builder.tag;
    this.creationDate = builder.creationDate;
    this.timestamp = builder.timestamp;
    this.userMetadata = builder.userMetadata;
    this.contentAccessor = builder.contentAccessor;
  }

  public String getBucketName() {
    return bucketName;
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

  public String getUserMetadata() {
    return userMetadata;
  }

  public ContentAccessor getContentAccessor() {
    return contentAccessor;
  }


  /**
   * Return the set content type or, if this object was supplied with a file on disk via {@link Builder#setFileContent},
   * probe the file for its content type using enviornmental {@link java.nio.file.spi.FileTypeDetector}s.
   */
  public String probeContentType() throws IOException {
    if (contentType != null) return contentType;
    if (contentAccessor instanceof FileAccessor) {
      File file = ((FileAccessor) contentAccessor).file;
      return Files.probeContentType(file.toPath());
    }
    throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyContentType)
        .key(getKey()).build();
  }

  /**
   * Checks whether {@link #probeContentType()} can return something.
   */
  public boolean canGetContentType() {
    return (contentType != null) || (contentAccessor instanceof FileAccessor);
  }


  public static Builder builder(String bucketName, String key) {
    return new Builder(bucketName, key);
  }

  public static class Builder {

    private final String bucketName;
    private final String key; // what the user specifies
    private String downloadName;
    private String contentType;
    private String tag;
    private Timestamp creationDate;
    private Timestamp timestamp;   // last modification time
    private String userMetadata;
    private ContentAccessor contentAccessor;

    public Builder(String bucketName, String key) {
      this.bucketName = Objects.requireNonNull(bucketName);
      this.key = Objects.requireNonNull(key);
    }

    public Builder setDownloadName(String downloadName) {
      this.downloadName = downloadName;
      return this;
    }

    public Builder setContentType(String contentType) {
      this.contentType = contentType;
      return this;
    }

    public Builder setTag(String tag) {
      this.tag = tag;
      return this;
    }

    public Builder setCreationDate(Timestamp creationDate) {
      this.creationDate = creationDate;
      return this;
    }

    public Builder setTimestamp(Timestamp timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder setUserMetadata(String userMetadata) {
      this.userMetadata = userMetadata;
      return this;
    }

    public Builder setFileContent(File fileContent) {
      this.contentAccessor = new FileAccessor(fileContent);
      return this;
    }

    public Builder setByteContent(byte[] byteContent) {
      this.contentAccessor = new ByteArrayAccessor(byteContent);
      return this;
    }

    public Builder setContentAccessor(ContentAccessor contentAccessor) {
      this.contentAccessor = contentAccessor;
      return this;
    }

    public RepoObject build() {
      return new RepoObject(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RepoObject that = (RepoObject) o;

    if (bucketName != null ? !bucketName.equals(that.bucketName) : that.bucketName != null) return false;
    if (key != null ? !key.equals(that.key) : that.key != null) return false;
    if (downloadName != null ? !downloadName.equals(that.downloadName) : that.downloadName != null) return false;
    if (contentType != null ? !contentType.equals(that.contentType) : that.contentType != null) return false;
    if (tag != null ? !tag.equals(that.tag) : that.tag != null) return false;
    if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) return false;
    if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
    if (userMetadata != null ? !userMetadata.equals(that.userMetadata) : that.userMetadata != null) return false;
    return contentAccessor != null ? contentAccessor.equals(that.contentAccessor) : that.contentAccessor == null;

  }

  @Override
  public int hashCode() {
    int result = bucketName != null ? bucketName.hashCode() : 0;
    result = 31 * result + (key != null ? key.hashCode() : 0);
    result = 31 * result + (downloadName != null ? downloadName.hashCode() : 0);
    result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
    result = 31 * result + (tag != null ? tag.hashCode() : 0);
    result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
    result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
    result = 31 * result + (userMetadata != null ? userMetadata.hashCode() : 0);
    result = 31 * result + (contentAccessor != null ? contentAccessor.hashCode() : 0);
    return result;
  }
}
