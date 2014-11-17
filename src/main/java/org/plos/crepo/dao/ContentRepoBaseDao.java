package org.plos.crepo.dao;

import com.google.common.base.Preconditions;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.HttpResponseUtil;
import org.slf4j.Logger;

import java.io.IOException;

public abstract class ContentRepoBaseDao {

  private final ContentRepoAccessConfig accessConfig;

  protected ContentRepoBaseDao(ContentRepoAccessConfig accessConfig) {
    this.accessConfig = Preconditions.checkNotNull(accessConfig);
  }

  protected String getRepoServer() {
    return accessConfig.getRepoServer();
  }

  protected HttpResponse executeRequest(HttpRequestBase request, ErrorType errorType) {
    CloseableHttpResponse response;
    try {
      response = accessConfig.open(request);
    } catch (IOException e) {
      getLog().error("Error handling the response, uri: " + request.getURI().toString() + " repoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(errorType)
          .baseException(e)
          .url(request.getURI().toString())
          .build();
    }

    try {
      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK && response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
        String cause = HttpResponseUtil.getErrorMessage(response);
        getLog().error("uri: " + request.getURI().toString() + " repoMessage: " + cause);
        throw new ContentRepoException.ContentRepoExceptionBuilder(errorType)
            .url(request.getURI().toString())
            .repoMessage(cause)
            .build();
      }

      return response;
    } catch (RuntimeException e) {
      IOUtils.closeQuietly(response);
      request.releaseConnection();
      throw e;
    }
  }

  public abstract Logger getLog();

}
