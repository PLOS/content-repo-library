package org.plos.crepo.dao.config.impl;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.plos.crepo.dao.ContentRepoBaseDao;
import org.plos.crepo.dao.config.ContentRepoConfigDao;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.ConfigUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ContentRepoConfigDaoImpl extends ContentRepoBaseDao implements ContentRepoConfigDao {

  private static final Logger log = LoggerFactory.getLogger(ContentRepoConfigDaoImpl.class);

  @Autowired
  private ConfigUrlGenerator configUrlGenerator;

  @Override
  public HttpResponse hasReProxy() {
    HttpGet request = new HttpGet(configUrlGenerator.getHasReproxyUrl(repoServer));
    return executeRequest(request, ErrorType.ErrorFetchingReproxyData);
  }

  @Override
  public HttpResponse getRepoConfig() {
    HttpGet request = new HttpGet(configUrlGenerator.getRepoConfigUrl(repoServer));
    return executeRequest(request, ErrorType.ErrorFetchingConfig);
  }

  @Override
  public HttpResponse getRepoStatus() {
    HttpGet request = new HttpGet(configUrlGenerator.getRepoStatusUrl(repoServer));
    return executeRequest(request, ErrorType.ErrorFetchingStatus);
  }


  @Override
  public Logger getLog() {
    return log;
  }
}
