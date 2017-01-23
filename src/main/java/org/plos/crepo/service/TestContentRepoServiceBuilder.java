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

package org.plos.crepo.service;

import com.google.gson.Gson;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.buckets.ContentRepoBucketsDao;
import org.plos.crepo.dao.collections.ContentRepoCollectionDao;
import org.plos.crepo.dao.config.ContentRepoConfigDao;
import org.plos.crepo.dao.objects.ContentRepoObjectDao;

class TestContentRepoServiceBuilder {

  private ContentRepoAccessConfig accessConfig;
  private Gson gson;
  private ContentRepoConfigDao configDao;
  private ContentRepoBucketsDao bucketsDao;
  private ContentRepoObjectDao objectDao;
  private ContentRepoCollectionDao collectionDao;

  TestContentRepoServiceBuilder() {
  }

  public ContentRepoService build() {
    return new ContentRepoServiceImpl(this);
  }

  public ContentRepoAccessConfig getAccessConfig() {
    return accessConfig;
  }

  public TestContentRepoServiceBuilder setAccessConfig(ContentRepoAccessConfig accessConfig) {
    this.accessConfig = accessConfig;
    return this;
  }

  public Gson getGson() {
    return gson;
  }

  public TestContentRepoServiceBuilder setGson(Gson gson) {
    this.gson = gson;
    return this;
  }

  public ContentRepoConfigDao getConfigDao() {
    return configDao;
  }

  public TestContentRepoServiceBuilder setConfigDao(ContentRepoConfigDao configDao) {
    this.configDao = configDao;
    return this;
  }

  public ContentRepoBucketsDao getBucketsDao() {
    return bucketsDao;
  }

  public TestContentRepoServiceBuilder setBucketsDao(ContentRepoBucketsDao bucketsDao) {
    this.bucketsDao = bucketsDao;
    return this;
  }

  public ContentRepoObjectDao getObjectDao() {
    return objectDao;
  }

  public TestContentRepoServiceBuilder setObjectDao(ContentRepoObjectDao objectDao) {
    this.objectDao = objectDao;
    return this;
  }

  public ContentRepoCollectionDao getCollectionDao() {
    return collectionDao;
  }

  public TestContentRepoServiceBuilder setCollectionDao(ContentRepoCollectionDao collectionDao) {
    this.collectionDao = collectionDao;
    return this;
  }

}
