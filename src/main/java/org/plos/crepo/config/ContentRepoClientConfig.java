package org.plos.crepo.config;

public interface ContentRepoClientConfig {

  String getBucketName();

  String getRepoServer();

  int getConnectionsMaxTotal();

  int getConnectionsMaxDefault();

}
