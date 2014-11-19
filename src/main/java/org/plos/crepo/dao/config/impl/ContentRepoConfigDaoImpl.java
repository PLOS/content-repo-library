package org.plos.crepo.dao.config.impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.ContentRepoBaseDao;
import org.plos.crepo.dao.config.ContentRepoConfigDao;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.ConfigUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ContentRepoConfigDaoImpl extends ContentRepoBaseDao implements ContentRepoConfigDao {

  private static final Logger log = LoggerFactory.getLogger(ContentRepoConfigDaoImpl.class);

  public ContentRepoConfigDaoImpl(ContentRepoAccessConfig accessConfig) {
    super(accessConfig);
  }

  @Override
  public CloseableHttpResponse hasReProxy() {
    HttpGet request = new HttpGet(ConfigUrlGenerator.getHasReproxyUrl(getRepoServer()));
    return executeRequest(request, ErrorType.ErrorFetchingReproxyData);
  }

  @Override
  public CloseableHttpResponse getRepoConfig() {
    HttpGet request = new HttpGet(ConfigUrlGenerator.getRepoConfigUrl(getRepoServer()));
    return executeRequest(request, ErrorType.ErrorFetchingConfig);
  }

  @Override
  public CloseableHttpResponse getRepoStatus() {
    HttpGet request = new HttpGet(ConfigUrlGenerator.getRepoStatusUrl(getRepoServer()));
    return executeRequest(request, ErrorType.ErrorFetchingStatus);
  }


  @Override
  public Logger getLog() {
    return log;
  }
}
