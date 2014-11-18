package org.plos.crepo.service.contentRepo.impl.factory;

import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.buckets.ContentRepoBucketsDao;
import org.plos.crepo.dao.buckets.impl.ContentRepoBucketDaoImpl;
import org.plos.crepo.dao.collections.ContentRepoCollectionDao;
import org.plos.crepo.dao.collections.impl.ContentRepoCollectionDaoImpl;
import org.plos.crepo.dao.config.ContentRepoConfigDao;
import org.plos.crepo.dao.config.impl.ContentRepoConfigDaoImpl;
import org.plos.crepo.dao.objects.ContentRepoObjectDao;
import org.plos.crepo.dao.objects.impl.ContentRepoObjectDaoImpl;
import org.plos.crepo.service.buckets.CRepoBucketService;
import org.plos.crepo.service.buckets.impl.CRepoBucketServiceImpl;
import org.plos.crepo.service.collections.CRepoCollectionService;
import org.plos.crepo.service.collections.impl.CRepoCollectionServiceImpl;
import org.plos.crepo.service.config.CRepoConfigService;
import org.plos.crepo.service.config.impl.CRepoConfigServiceImpl;
import org.plos.crepo.service.contentRepo.impl.ContentRepoServiceImpl;
import org.plos.crepo.service.objects.CRepoObjectService;
import org.plos.crepo.service.objects.impl.CRepoObjectServiceImpl;

/**
 * Content Repo Service Factory class used to create an instance of the content repo service.
 */
public class ContentRepoServiceFactory {


  public ContentRepoServiceImpl createContentRepoService(ContentRepoAccessConfig accessConfig){

    CRepoBucketService cRepoBucketService = this.createCRepoBucketService(accessConfig);
    CRepoCollectionService cRepoCollectionService = this.createCrepoCollectionService(accessConfig);
    CRepoConfigService cRepoConfigService = this.createCrepoConfigService(accessConfig);
    CRepoObjectService cRepoObjectService = this.createCrepoObjectService(accessConfig);

    return new ContentRepoServiceImpl(cRepoBucketService, cRepoCollectionService, cRepoConfigService, cRepoObjectService);

  }

  public CRepoBucketService createCRepoBucketService(ContentRepoAccessConfig accessConfig){
    ContentRepoBucketsDao contentRepoBucketsDao = new ContentRepoBucketDaoImpl(accessConfig);
    return new CRepoBucketServiceImpl(contentRepoBucketsDao);
  }

  private CRepoCollectionService createCrepoCollectionService(ContentRepoAccessConfig accessConfig) {
    ContentRepoCollectionDao contentRepoCollectionDao = new ContentRepoCollectionDaoImpl(accessConfig);
    return new CRepoCollectionServiceImpl(accessConfig, contentRepoCollectionDao);
  }

  private CRepoObjectService createCrepoObjectService(ContentRepoAccessConfig accessConfig) {
    ContentRepoObjectDao contentRepoCollectionDao = new ContentRepoObjectDaoImpl(accessConfig);
    return new CRepoObjectServiceImpl(accessConfig, contentRepoCollectionDao);
  }

  private CRepoConfigService createCrepoConfigService(ContentRepoAccessConfig accessConfig) {
    ContentRepoConfigDao contentRepoCollectionDao = new ContentRepoConfigDaoImpl(accessConfig);
    return new CRepoConfigServiceImpl(contentRepoCollectionDao);
  }

}
