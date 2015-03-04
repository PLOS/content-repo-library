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
