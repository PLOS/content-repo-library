package org.plos.crepo.service.contentRepo;

import org.plos.crepo.service.buckets.CRepoBucketService;
import org.plos.crepo.service.collections.CRepoCollectionService;
import org.plos.crepo.service.config.CRepoConfigService;
import org.plos.crepo.service.objects.CRepoObjectService;

public interface ContentRepoService
    extends CRepoObjectService, CRepoConfigService, CRepoCollectionService, CRepoBucketService {
}
