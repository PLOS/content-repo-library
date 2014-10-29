package org.plos.crepo.service.buckets;

import java.util.List;
import java.util.Map;

/**
 * Created by lmasola on 10/28/14.
 */
public interface CRepoBucketService {

  List<Map<String, Object>> getBuckets();

  Map<String, Object> getBucket(String key);

  Map<String, Object> createBucket(String key);

}
