package org.plos.crepo.exceptions;

/**
 * Created by lmasola on 10/16/14.
 */
public enum ErrorType {

  ServerError(0, "Server error"), // this message is not used

  ErrorAccessingFile(10, "Error accessing file"),
  ErrorCreatingObject(11, "Error creating new object"),
  ErrorVersioningObject(12, "Error versioning new object"),
  ErrorFetchingObject(13, "Error fetching object from content repo"),
  ErrorFetchingObjectMeta(14, "Error fetching object meta data from content repo"),
  ErrorFetchingObjectVersions(15, "Error fetching object versions from content repo"),
  ErrorCreatingBucket(16, "Error creating bucket on server"),
  EmptyContentType(17, "Empty Content Type"),
  EmptyContent(18, "Content is empty. A File object or a byte[] must be specified. "),


  ErrorFetchingBucketMeta(20, "Error fetching buckets meta"),
  ErrorFetchingReproxyData(21, "Error fetching reproxy information"),

  ErrorCreatingCollection(30, "Error creating collection"),
  ErrorVersioningCollection(31, "Error versioning collection"),
  ErrorDeletingCollection(32, "Error deleting collection"),
  ErrorFetchingCollection(33, "Error fetching collection information"),


  ErrorFetchingReProxyUrl(12, "Problem fetching reproxy URLs"),
  ErrorCopyingFileFrom(13, "Error copying file from store"),
  ErrorCopyingFileTo(14, "Error copying file to store"),
  ErrorFetchingMetaData(15, "Error fetching metadata"),
  ErrorDeletingObject(16,"Error deleting object"),
  ErrorRenameNotSupported(17,"ContentRepo:renameFile not supported"),



  ;

  private final int value;
  private final String message;

  private ErrorType(int value, String message) {
    this.value = value;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public int getValue() {
    return value;
  }

}

