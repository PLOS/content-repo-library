package org.plos.crepo.exceptions;

/**
 * Created by lmasola on 10/16/14.
 */
public enum ErrorType {

  ServerError(0, "Server error"), // this message is not used
  EmptyObjectKey(1, "Empty repo object key"),
  EmptyObjectCks(2, "Empty repo object version checksum"),
  EmptyObjectTag(3, "Empty repo object tag"),
  EmptyCollectionKey(4, "Empty repo collection key"),
  EmptyCollectionCks(5, "Empty repo collection version checksum"),
  EmptyCollectionTag(6, "Empty repo collection tag"),
  EmptyBucketKey(7, "Empty bucket key"),

  ErrorAccessingFile(100, "Error accessing file"),
  ErrorCreatingObject(101, "Error creating new object"),
  ErrorVersioningObject(102, "Error versioning new object"),
  ErrorAutoCreatingObject(103, "Error when trying to auto create an object"),
  ErrorFetchingObjectMeta(104, "Error fetching object meta data from content repo"),
  ErrorFetchingObjectVersions(105, "Error fetching object versions from content repo"),
  ErrorCreatingBucket(106, "Error creating bucket on server"),
  EmptyContentType(107, "Empty Content Type"),
  EmptyContent(108, "Content is empty. A File object or a byte[] must be specified. "),
  ErrorFetchingReProxyUrl(109, "Problem fetching reproxy URLs"),
  ErrorDeletingObject(110,"Error deleting object"),
  ErrorFetchingObject(111, "Error fetching object from content repo"),


  ErrorFetchingBucketMeta(200, "Error fetching buckets meta"),
  ErrorFetchingReproxyData(201, "Error fetching reproxy information"),
  ErrorFetchingConfig(202, "Error fetching repo configuration"),
  ErrorFetchingStatus(203, "Error fetching repo status"),

  ErrorCreatingCollection(300, "Error creating collection"),
  ErrorVersioningCollection(301, "Error versioning collection"),
  ErrorDeletingCollection(302, "Error deleting collection"),
  ErrorFetchingCollection(303, "Error fetching collection information"),
  ErrorFetchingCollections(304, "Error fetching collections from bucket"),
  ErrorFetchingCollectionVersions(305, "Error fectching collection versions"),
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

