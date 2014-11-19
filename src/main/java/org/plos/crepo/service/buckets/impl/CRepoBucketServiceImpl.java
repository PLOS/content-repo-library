package org.plos.crepo.service.buckets.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.dao.buckets.ContentRepoBucketsDao;
import org.plos.crepo.dao.buckets.impl.ContentRepoBucketDaoImpl;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.service.buckets.CRepoBucketService;
import org.plos.crepo.util.HttpResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CRepoBucketServiceImpl implements CRepoBucketService {

  private static final Logger log = LoggerFactory.getLogger(CRepoBucketServiceImpl.class);
  private final Gson gson;
  private final ContentRepoBucketsDao contentRepoBucketsDao;


  public CRepoBucketServiceImpl(ContentRepoBucketsDao contentRepoBucketsDao) {
    this.contentRepoBucketsDao = contentRepoBucketsDao;
    gson = new Gson();
  }

  @Override
  public List<Map<String, Object>> getBuckets() {
    try (CloseableHttpResponse response = this.contentRepoBucketsDao.getBuckets()){
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<List<Map<String, Object>>>() {
      }.getType());
    } catch (IOException e) {
      log.error("Error handling the response when getting all the buckets. RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
        .baseException(e)
        .build();
    }
  }

  @Override
  public Map<String, Object> getBucket(String key) {
    validateBucketKey(key);
    try (CloseableHttpResponse response = this.contentRepoBucketsDao.getBucket(key)){
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      log.error("Error handling the response when getting a bucket. Key:  " + key + " RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }

  }

  private void validateBucketKey(String key) {
    if (StringUtils.isEmpty(key)) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyBucketKey)
          .build();
    }
  }

  @Override
  public Map<String, Object> createBucket(String key) {
    try (CloseableHttpResponse response = this.contentRepoBucketsDao.createBucket(key)){
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      log.error("Error handling the response when creating a bucket. RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }

  }

}
