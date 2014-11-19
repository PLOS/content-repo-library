package org.plos.crepo.dao.config;


import org.apache.http.client.methods.CloseableHttpResponse;

public interface ContentRepoConfigDao {

  CloseableHttpResponse hasReProxy();

  CloseableHttpResponse getRepoConfig();

  CloseableHttpResponse getRepoStatus();

}
