package org.plos.crepo.service.config.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.plos.crepo.dao.config.ContentRepoConfigDao;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.service.config.CRepoConfigService;
import org.plos.crepo.util.HttpResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CRepoConfigServiceImpl implements CRepoConfigService {

  private static final Logger log = LoggerFactory.getLogger(CRepoConfigServiceImpl.class);

  private final Gson gson;
  private final ContentRepoConfigDao contentRepoConfigDao;

  public CRepoConfigServiceImpl(ContentRepoConfigDao contentRepoCollectionDao) {
    this.contentRepoConfigDao = contentRepoCollectionDao;
    gson = new Gson();
  }

  public Boolean hasXReproxy() {
    try (CloseableHttpResponse response = contentRepoConfigDao.hasReProxy()){
      String resString = HttpResponseUtil.getResponseAsString(response);
      return Boolean.parseBoolean(resString);
    } catch (IOException e) {
      log.error("Error handling the response when fetching the reproxy information. RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }
  }

  @Override
  public Map<String, Object> getRepoConfig() {
    try (CloseableHttpResponse response = contentRepoConfigDao.getRepoConfig()){
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      log.error("Error handling the response whenfetching the repo configuration. RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }
  }

  @Override
  public Map<String, Object> getRepoStatus() {
    try (CloseableHttpResponse response = contentRepoConfigDao.getRepoStatus()){
      return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {
      }.getType());
    } catch (IOException e) {
      log.error("Error handling the response when fetching the repo status information. RepoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
          .baseException(e)
          .build();
    }

  }

}
