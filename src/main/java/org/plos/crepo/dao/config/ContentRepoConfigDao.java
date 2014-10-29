package org.plos.crepo.dao.config;


import org.apache.http.HttpResponse;
import org.plos.crepo.model.RepoCollection;
import org.plos.crepo.model.RepoObject;

public interface ContentRepoConfigDao {

  HttpResponse hasReProxy();

  HttpResponse getRepoConfig();

  HttpResponse getRepoStatus();

}
