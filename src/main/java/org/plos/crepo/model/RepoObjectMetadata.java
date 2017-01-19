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

import com.google.common.base.Optional;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents output to the client, describing an object.
 */
public class RepoObjectMetadata extends RepoMetadata {

  public RepoObjectMetadata(Map<String, Object> raw) {
    super(raw);
  }

  public long getSize() {
    return ((Number) raw.get("size")).longValue();
  }

  public Optional<String> getContentType() {
    return Optional.fromNullable((String) raw.get("contentType"));
  }

  public Optional<String> getDownloadName() {
    return Optional.fromNullable((String) raw.get("downloadName"));
  }

  public List<URL> getReproxyUrls() {
    List<String> rawUrls = (List<String>) raw.get("reproxyURL");
    if (rawUrls == null) return new ArrayList<>(0);
    List<URL> urls = new ArrayList<>(rawUrls.size());
    for (String rawUrl : rawUrls) {
      try {
        urls.add(new URL(rawUrl));
      } catch (MalformedURLException e) {
        throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingReProxyUrl)
            .baseException(e)
            .build();
      }
    }
    return urls;
  }

}
