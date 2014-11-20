package org.plos.crepo.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.io.IOException;

/**
 * A basic configuration object. Stores constant string values for the repo server URL and bucket name. Maintains a
 * {@link PoolingHttpClientConnectionManager} instance with configurable values for {@code setMaxTotal} and {@code
 * setDefaultMaxPerRoute}.
 * <p/>
 * Note that every instance of this class constructs its own {@link PoolingHttpClientConnectionManager} object.
 * Generally, only one instance of {@code BasicContentRepoAccessConfig} should be configured per application context, in
 * order to avoid having redundant connection managers, which could cause performance problems.
 * <p/>
 * If your application context already has a separate {@link HttpClientConnectionManager} object, it is better not to
 * use this class. Instead, implement your own {@link ContentRepoAccessConfig} object whose {@link
 * ContentRepoAccessConfig#open} method uses your existing connection manager to open a response.
 */
public class BasicContentRepoAccessConfig implements ContentRepoAccessConfig {

  private final String bucketName;
  private final String repoServer;
  private final HttpClientConnectionManager connectionManager;

  private BasicContentRepoAccessConfig(Builder builder) {
    this.bucketName = builder.bucketName;
    this.repoServer = builder.repoServer;

    Preconditions.checkState(!Strings.isNullOrEmpty(bucketName), "bucketName required");
    Preconditions.checkState(!Strings.isNullOrEmpty(repoServer), "repoServer required");
    Preconditions.checkState(builder.connectionsMaxTotal > 0, "connectionsMaxTotal must be positive");
    Preconditions.checkState(builder.connectionsMaxDefault > 0, "connectionsMaxDefault must be positive");

    this.connectionManager = createConnectionManager(builder.connectionsMaxTotal, builder.connectionsMaxDefault);
  }

  private static HttpClientConnectionManager createConnectionManager(int connectionsMaxTotal, int connectionsMaxDefault) {
    PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
    manager.setMaxTotal(connectionsMaxTotal);
    manager.setDefaultMaxPerRoute(connectionsMaxDefault);
    return manager;
  }

  @Override
  public String getBucketName() {
    return bucketName;
  }

  @Override
  public String getRepoServer() {
    return repoServer;
  }

  @Override
  public CloseableHttpResponse open(HttpUriRequest request) throws IOException {
    return HttpClientBuilder.create().setConnectionManager(connectionManager).build().execute(request);
  }


  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String bucketName;
    private String repoServer;
    private int connectionsMaxTotal = 400;
    private int connectionsMaxDefault = 20;

    private Builder() {
    }

    public BasicContentRepoAccessConfig build() {
      return new BasicContentRepoAccessConfig(this);
    }

    public Builder setBucketName(String bucketName) {
      this.bucketName = bucketName;
      return this;
    }

    public Builder setRepoServer(String repoServer) {
      this.repoServer = repoServer;
      return this;
    }

    public Builder setConnectionsMaxTotal(int connectionsMaxTotal) {
      this.connectionsMaxTotal = connectionsMaxTotal;
      return this;
    }

    public Builder setConnectionsMaxDefault(int connectionsMaxDefault) {
      this.connectionsMaxDefault = connectionsMaxDefault;
      return this;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Builder builder = (Builder) o;

      if (connectionsMaxDefault != builder.connectionsMaxDefault) return false;
      if (connectionsMaxTotal != builder.connectionsMaxTotal) return false;
      if (bucketName != null ? !bucketName.equals(builder.bucketName) : builder.bucketName != null) return false;
      if (repoServer != null ? !repoServer.equals(builder.repoServer) : builder.repoServer != null) return false;

      return true;
    }

    @Override
    public int hashCode() {
      int result = bucketName != null ? bucketName.hashCode() : 0;
      result = 31 * result + (repoServer != null ? repoServer.hashCode() : 0);
      result = 31 * result + connectionsMaxTotal;
      result = 31 * result + connectionsMaxDefault;
      return result;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    BasicContentRepoAccessConfig that = (BasicContentRepoAccessConfig) o;

    if (!bucketName.equals(that.bucketName)) return false;
    if (!connectionManager.equals(that.connectionManager)) return false;
    if (!repoServer.equals(that.repoServer)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = bucketName.hashCode();
    result = 31 * result + repoServer.hashCode();
    result = 31 * result + connectionManager.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "BasicContentRepoAccessConfig{" +
        "bucketName='" + bucketName + '\'' +
        ", repoServer='" + repoServer + '\'' +
        ", connectionManager=" + connectionManager +
        '}';
  }
}
