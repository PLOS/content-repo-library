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
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;

/**
 * An identifier for a version of a repo object or collection, using a user-defined tag.
 */
public class RepoVersionTag {

  private final String key;
  private final String tag;

  public RepoVersionTag(String key, String tag) {
    RepoVersion.validateKey(key);
    validateObjectTag(tag);
    this.key = Preconditions.checkNotNull(key);
    this.tag = Preconditions.checkNotNull(tag);
  }

  public String getKey() {
    return key;
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
    return key.equals(that.key) && tag.equals(that.tag);
  }

  @Override
  public int hashCode() {
    return 31 * key.hashCode() + tag.hashCode();
  }

  @Override
  public String toString() {
    return String.format("%s(\"%s\", \"%s\")", getClass().getSimpleName(),
        StringEscapeUtils.escapeJava(key), StringEscapeUtils.escapeJava(tag));
  }
}
