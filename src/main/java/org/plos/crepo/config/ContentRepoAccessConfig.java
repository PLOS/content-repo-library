package org.plos.crepo.config;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.util.Objects;

public class ContentRepoAccessConfig {

  private final String repoServer;
  private final HttpClientFunction client;

  public ContentRepoAccessConfig(String repoServer, HttpClientFunction client) {
    this.repoServer = Objects.requireNonNull(repoServer);
    this.client = Objects.requireNonNull(client);
  }

  /**
   * Provide the root URL of the content repo server to connect to.
   *
   * @return the content repo server URL
   */
  public String getRepoServer() {
    return repoServer;
  }

  public CloseableHttpResponse open(HttpUriRequest request) throws IOException {
    return client.open(request);
  }

}
