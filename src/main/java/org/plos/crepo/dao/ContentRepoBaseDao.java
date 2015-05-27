package org.plos.crepo.dao;

import com.google.common.base.Preconditions;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.plos.crepo.config.ContentRepoAccessConfig;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.exceptions.NotFoundException;
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

  protected CloseableHttpResponse executeRequest(HttpRequestBase request, ErrorType errorType) {
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
      final int statusCode = response.getStatusLine().getStatusCode();

      if( statusCode == HttpStatus.SC_NOT_FOUND) {
        throw new NotFoundException(HttpResponseUtil.getErrorMessage(response));
      } else if ( statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED) {
        final String cause = HttpResponseUtil.getErrorMessage(response);

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
