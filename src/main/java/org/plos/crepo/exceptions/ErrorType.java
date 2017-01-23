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

package org.plos.crepo.exceptions;

/**
 * Created by lmasola on 10/16/14.
 */
public enum ErrorType {

  ServerError(0, "Server error"), // this message is not used
  EmptyKey(1, "Empty repo object or collection key"),
  EmptyUuid(2, "Empty repo object or collection UUID"),
  EmptyTag(3, "Empty repo object or collection tag"),
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
  ErrorAutoCreatingCollection(306, "Error auto-creating collection"),
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

