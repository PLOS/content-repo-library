package org.plos.crepo.service.buckets.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.plos.crepo.dao.buckets.ContentRepoBucketsDao;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.service.buckets.CRepoBucketService;
import org.plos.crepo.util.HttpResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CRepoBucketServiceImpl implements CRepoBucketService {

  @Autowired
  private Gson gson;

  @Autowired
  private ContentRepoBucketsDao contentRepoBucketsDao;

  @Override
  public List<Map<String, Object>> getBuckets(){
    HttpResponse response = this.contentRepoBucketsDao.getBuckets();
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>(){}.getType());
  }

  @Override
  public Map<String, Object> getBucket(String key){
    validateBucketKey(key);
    HttpResponse response = this.contentRepoBucketsDao.getBucket(key);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  private void validateBucketKey(String key) {
    if (StringUtils.isEmpty(key)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyBucketKey)
          .build();
    }
  }

  @Override
  public Map<String, Object> createBucket(String key){
    HttpResponse response = this.contentRepoBucketsDao.createBucket(key);
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

}
