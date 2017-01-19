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

package org.plos.crepo.config;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

public interface ContentRepoAccessConfig {

  /**
   * Provide the root URL of the content repo server to connect to.
   *
   * @return the content repo server URL
   */
  String getRepoServer();

  /**
   * Provide the default bucket name. Any request method for an operation that is specific to a bucket, if it does not
   * take a bucket name as an argument, will apply to the default bucket.
   *
   * @return the name of the default bucket
   */
  String getBucketName();

  /**
   * Open a response to a given URI.
   * <p/>
   * This is part of the configuration interface so that a connection manager can be plugged in to this library from the
   * application context. If your application context maintains a {@link org.apache.http.conn.HttpClientConnectionManager},
   * you can set up a {@code ContentRepoAccessConfig} object to use that connection manager to open responses.
   * <p/>
   * The implementation of this method doesn't provide any information about repo URIs. All such information would be
   * provided in the {@code request} argument.
   *
   * @param request a request to a URI
   * @return the response object
   * @throws IOException if an error occurs opening the request
   */
  CloseableHttpResponse open(HttpUriRequest request) throws IOException;

}
