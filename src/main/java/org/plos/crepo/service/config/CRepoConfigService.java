package org.plos.crepo.service.config;

import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoObject;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by lmasola on 10/28/14.
 */
public interface CRepoConfigService {

  /**
   * Query whether filestore provides HTTP redirects to files. The
   * underlying assumption is that the filestore is/also knows about
   * the reproxy server.
   *
   * @return - true if filestore can provide redirects for HTTP access to files.
   */
  @Deprecated
  public Boolean hasXReproxy();

  Map<String,Object> getRepoConfig();

  Map<String,Object> getRepoStatus();

}
