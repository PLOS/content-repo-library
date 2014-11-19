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
