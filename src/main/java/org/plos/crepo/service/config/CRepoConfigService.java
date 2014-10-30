package org.plos.crepo.service.config;

import java.util.Map;

/**
 * Content Repo Configuration service interface.
 */
public interface CRepoConfigService {

  /**
   * Query whether content repo provides HTTP redirects to files.
   *
   * @return - true if the repo can provide redirects for HTTP access to files.
   */
  public Boolean hasXReproxy();

  /**
   * Returns the content repo server configuration
   * @return a map with the configuration fields.
   */
  Map<String,Object> getRepoConfig();

  /**
   * Returns the server status.
   * @return a map with the status fields.
   */
  Map<String,Object> getRepoStatus();

}
