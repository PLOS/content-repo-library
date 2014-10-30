package org.plos.crepo.service.buckets;

import java.util.List;
import java.util.Map;

/**
 * Content Repo Bucket service interface.
 */
public interface CRepoBucketService {

  List<Map<String, Object>> getBuckets();

  Map<String, Object> getBucket(String key);

  Map<String, Object> createBucket(String key);

}
