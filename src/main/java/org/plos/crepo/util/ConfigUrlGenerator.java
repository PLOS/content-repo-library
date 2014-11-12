package org.plos.crepo.util;

import static org.plos.crepo.util.BaseUrlGenerator.getUrlBasicMap;
import static org.plos.crepo.util.BaseUrlGenerator.replaceUrl;

/**
 * Generates the content repo urls for config services.
 */
public class ConfigUrlGenerator {

  private static final String hasReproxyUrl = "${repoServer}/hasXReproxy";
  private static final String repoConfigUrl = "${repoServer}/config";
  private static final String repoStatusUrl = "${repoServer}/status";

  public static String getHasReproxyUrl(String repoServer) {
    return replaceUrl(hasReproxyUrl, getUrlBasicMap(repoServer));
  }

  public static String getRepoConfigUrl(String repoServer) {
    return replaceUrl(repoConfigUrl, getUrlBasicMap(repoServer));
  }

  public static String getRepoStatusUrl(String repoServer) {
    return replaceUrl(repoStatusUrl, getUrlBasicMap(repoServer));
  }

}
