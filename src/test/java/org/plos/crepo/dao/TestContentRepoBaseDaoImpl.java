package org.plos.crepo.dao;

import org.plos.crepo.config.ContentRepoAccessConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ContentRepoBaseDao just for testing purpose
 */
public class TestContentRepoBaseDaoImpl extends ContentRepoBaseDao {

  private static final Logger log = LoggerFactory.getLogger(TestContentRepoBaseDaoImpl.class);

  protected TestContentRepoBaseDaoImpl(ContentRepoAccessConfig accessConfig) {
    super(accessConfig);
  }

  @Override
  public Logger getLog() {
    return log;
  }
}

