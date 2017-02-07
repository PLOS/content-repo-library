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

package org.plos.crepo.model.identity;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;

import java.util.Objects;

/**
 * The native identifier for all version of a repo object or collection.
 */
public final class RepoId {

  private final String bucketName;
  private final String key;

  private RepoId(String bucketName, String key) {
    this.bucketName = Objects.requireNonNull(bucketName);
    this.key = Objects.requireNonNull(key);
    validateKey(this.key);
  }

  private static void validateKey(String key) {
    if (StringUtils.isEmpty(key)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyKey)
          .build();
    }
  }

  public static RepoId create(String bucketName, String key) {
    return new RepoId(bucketName, key);
  }


  public String getBucketName() {
    return bucketName;
  }

  public String getKey() {
    return key;
  }


  @Override
  public boolean equals(Object o) {
    return this == o || o != null && getClass() == o.getClass()
        && bucketName.equals(((RepoId) o).bucketName) && key.equals(((RepoId) o).key);
  }

  @Override
  public int hashCode() {
    return 31 * bucketName.hashCode() + key.hashCode();
  }

  @Override
  public String toString() {
    return String.format("%s(\"%s\", \"%s\")", getClass().getSimpleName(),
        StringEscapeUtils.escapeJava(bucketName), StringEscapeUtils.escapeJava(key));
  }
}
