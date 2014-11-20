package org.plos.crepo.util;

import static org.plos.crepo.util.BaseUrlGenerator.getUrlBasicMap;
import static org.plos.crepo.util.BaseUrlGenerator.replaceUrl;

/**
 * Generates the content repo urls for config services.
 */
public class ConfigUrlGenerator {

  private static final String HAS_REPROXY_URL = "${repoServer}/hasXReproxy";
  private static final String REPO_CONFIG_URL = "${repoServer}/config";
  private static final String REPO_STATUS_URL = "${repoServer}/status";

  public static String getHasReproxyUrl(String repoServer) {
    return replaceUrl(HAS_REPROXY_URL, getUrlBasicMap(repoServer));
  }

  public static String getRepoConfigUrl(String repoServer) {
    return replaceUrl(REPO_CONFIG_URL, getUrlBasicMap(repoServer));
  }

  public static String getRepoStatusUrl(String repoServer) {
    return replaceUrl(REPO_STATUS_URL, getUrlBasicMap(repoServer));
  }

}
