package org.plos.crepo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ContentRepoBaseDao just for testing purpose
 */
public class TestContentRepoBaseDaoImpl extends ContentRepoBaseDao{

  private static final Logger log = LoggerFactory.getLogger(TestContentRepoBaseDaoImpl.class);

  @Override
  public Logger getLog() {
    return log;
  }
}

