package org.plos.crepo.service.buckets;

import java.util.List;
import java.util.Map;

/**
 * Content Repo Bucket service interface.
 */
public interface CRepoBucketService {

  /**
   * Returns all buckets in the content repo.
   * @return a List with the data of every bucket
   */
  List<Map<String, Object>> getBuckets();

  /**
   * Returns the data of the bucket that matches the given <code>key</code>
   * @param key a single string representing the key of the bucket
   * @return a map with the bucket information
   */
  Map<String, Object> getBucket(String key);


  /**
   * Creates a new bucket with the given <code>key</code>
   * @param key
   * @return
   */
  Map<String, Object> createBucket(String key);

}
