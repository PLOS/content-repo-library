package org.plos.crepo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Generates the content repo urls for config services.
 */
@Component
public class ConfigUrlGenerator extends BaseUrlGenerator{

  @Value("${crepo.url.hasReproxyUrl}")
  private String hasReproxyUrl;

  @Value("${crepo.url.repoConfigUrl}")
  private String repoConfigUrl;

  @Value("${crepo.url.repoStatusUrl}")
  private String  repoStatusUrl;

  public String getHasReproxyUrl(String repoServer){
    return replaceUrl(hasReproxyUrl, getUrlBasicMap(repoServer));
  }

  public String getRepoConfigUrl(String repoServer){
    return replaceUrl(repoConfigUrl, getUrlBasicMap(repoServer));
  }

  public String getRepoStatusUrl(String repoServer){
    return replaceUrl(repoStatusUrl, getUrlBasicMap(repoServer));
  }

}
