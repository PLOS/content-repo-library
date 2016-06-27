package org.plos.crepo.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * This is part of the configuration so that a connection manager can be plugged in to this library from the application
 * context. If your application context maintains a {@link org.apache.http.conn.HttpClientConnectionManager}, you can
 * set up a {@code ContentRepoAccessConfig} object to use that connection manager to open responses.
 * <p/>
 * The implementation of this interface doesn't provide any information about repo URIs. All such information would be
 * provided in the {@code request} argument.
 */
@FunctionalInterface
public interface HttpClientFunction {

  /**
   * Open a response to a given URI.
   *
   * @param request a request to a URI
   * @return the response object
   * @throws IOException if an error occurs opening the request
   */
  CloseableHttpResponse open(HttpUriRequest request) throws IOException;

  public static HttpClientFunction from(CloseableHttpClient client) {
    return client::execute;
  }

}
