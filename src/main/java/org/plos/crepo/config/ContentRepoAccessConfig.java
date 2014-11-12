package org.plos.crepo.config;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

public interface ContentRepoAccessConfig {

  String getBucketName();

  String getRepoServer();

  CloseableHttpResponse open(HttpUriRequest request) throws IOException;

}
