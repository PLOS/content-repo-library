/*
 * Copyright 2017 Public Library of Science
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.plos.crepo.dao.config.impl;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.ContentRepoBaseDao;
import org.plos.crepo.dao.config.ContentRepoConfigDao;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.ConfigUrlGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
