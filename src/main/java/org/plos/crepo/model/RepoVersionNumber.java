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

/**
 * An identifier for a version of a repo object or collection, using the version number.
 */
public class RepoVersionNumber {

  private final String key;
  private final int number;

  public RepoVersionNumber(String key, int number) {
    RepoVersion.validateKey(key);
    this.key = Preconditions.checkNotNull(key);
    this.number = number;
    Preconditions.checkArgument(this.number >= 0);
  }

  public String getKey() {
    return key;
  }

  public int getNumber() {
    return number;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RepoVersionNumber that = (RepoVersionNumber) o;
    return number == that.number && key.equals(that.key);
  }

  @Override
  public int hashCode() {
    return 31 * key.hashCode() + number;
  }

  @Override
  public String toString() {
    return String.format("%s(\"%s\", %d)", getClass().getSimpleName(), StringEscapeUtils.escapeJava(key), number);
  }
}
