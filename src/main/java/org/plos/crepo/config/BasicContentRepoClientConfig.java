package org.plos.crepo.config;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class BasicContentRepoClientConfig implements ContentRepoClientConfig {

  private final String bucketName;
  private final String repoServer;
  private final int connectionsMaxTotal;
  private final int connectionsMaxDefault;

  private BasicContentRepoClientConfig(Builder builder) {
    this.bucketName = builder.bucketName;
    this.repoServer = builder.repoServer;
    this.connectionsMaxTotal = builder.connectionsMaxTotal;
    this.connectionsMaxDefault = builder.connectionsMaxDefault;

    Preconditions.checkState(!Strings.isNullOrEmpty(bucketName), "bucketName required");
    Preconditions.checkState(!Strings.isNullOrEmpty(repoServer), "repoServer required");
    Preconditions.checkState(connectionsMaxTotal > 0, "connectionsMaxTotal must be positive");
    Preconditions.checkState(connectionsMaxDefault > 0, "connectionsMaxDefault must be positive");
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
  public int getConnectionsMaxTotal() {
    return connectionsMaxTotal;
  }

  @Override
  public int getConnectionsMaxDefault() {
    return connectionsMaxDefault;
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

    public BasicContentRepoClientConfig build() {
      return new BasicContentRepoClientConfig(this);
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

    BasicContentRepoClientConfig that = (BasicContentRepoClientConfig) o;

    if (connectionsMaxDefault != that.connectionsMaxDefault) return false;
    if (connectionsMaxTotal != that.connectionsMaxTotal) return false;
    if (!bucketName.equals(that.bucketName)) return false;
    if (!repoServer.equals(that.repoServer)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = bucketName.hashCode();
    result = 31 * result + repoServer.hashCode();
    result = 31 * result + connectionsMaxTotal;
    result = 31 * result + connectionsMaxDefault;
    return result;
  }

  @Override
  public String toString() {
    return "BasicContentRepoClientConfig{" +
        "bucketName='" + bucketName + '\'' +
        ", repoServer='" + repoServer + '\'' +
        ", connectionsMaxTotal=" + connectionsMaxTotal +
        ", connectionsMaxDefault=" + connectionsMaxDefault +
        '}';
  }
}
