package org.plos.crepo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Generates the content repo urls for config services.
 */
@Component
public class ConfigUrlGenerator extends BaseUrlGenerator{

  private static final String hasReproxyUrl = "${repoServer}/hasXReproxy";
  private static final String repoConfigUrl = "${repoServer}/config";
  private static final String repoStatusUrl = "${repoServer}/status";

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
