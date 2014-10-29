package org.plos.crepo.service.config.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.plos.crepo.dao.config.ContentRepoConfigDao;
import org.plos.crepo.service.config.CRepoConfigService;
import org.plos.crepo.util.HttpResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CRepoConfigServiceImpl implements CRepoConfigService {

  private static Gson gson = new Gson();

  private static final Logger log = LoggerFactory.getLogger(CRepoConfigServiceImpl.class);

  @Autowired
  private ContentRepoConfigDao contentRepoConfigDao;

  public Boolean hasXReproxy() {

    HttpResponse response = contentRepoConfigDao.hasReProxy();
    String resString = HttpResponseUtil.getResponseAsString(response);
    return Boolean.parseBoolean(resString);

  }

  @Override
  public Map<String, Object> getRepoConfig() {
    HttpResponse response = contentRepoConfigDao.getRepoConfig();
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

  @Override
  public Map<String, Object> getRepoStatus() {
    HttpResponse response = contentRepoConfigDao.getRepoStatus();
    return gson.fromJson(HttpResponseUtil.getResponseAsString(response), new TypeToken<Map<String, Object>>() {}.getType());
  }

}
