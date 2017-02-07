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
 * An identifier for a version of a repo object or collection, using a user-defined tag.
 */
public final class RepoVersionTag {

  private final RepoId id;
  private final String tag;

  private RepoVersionTag(RepoId id, String tag) {
    this.id = Objects.requireNonNull(id);
    this.tag = Objects.requireNonNull(tag);
    validateObjectTag(this.tag);
  }

  public static RepoVersionTag create(RepoId id, String tag) {
    return new RepoVersionTag(id, tag);
  }

  public static RepoVersionTag create(String bucketName, String key, String tag) {
    return create(RepoId.create(bucketName, key), tag);
  }


  public RepoId getId() {
    return id;
  }

  public String getTag() {
    return tag;
  }

  private static void validateObjectTag(String tag) {
    if (StringUtils.isEmpty(tag)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyTag)
          .build();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RepoVersionTag that = (RepoVersionTag) o;
    return id.equals(that.id) && tag.equals(that.tag);
  }

  @Override
  public int hashCode() {
    return 31 * id.hashCode() + tag.hashCode();
  }

  @Override
  public String toString() {
    return String.format("%s(\"%s\", \"%s\", \"%s\")", getClass().getSimpleName(),
        StringEscapeUtils.escapeJava(id.getBucketName()), StringEscapeUtils.escapeJava(id.getKey()),
        StringEscapeUtils.escapeJava(tag));
  }
}
