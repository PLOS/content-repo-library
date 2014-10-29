package org.plos.crepo.dao;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.util.HttpResponseUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public abstract class ContentRepoBaseDao {

  @Autowired
  @Qualifier("httpClient")
  protected CloseableHttpClient httpClient;

  @Value("${crepo.repoServer}")
  protected String repoServer;

  protected HttpResponse executeRequest(HttpRequestBase request, ErrorType errorType){
    try (CloseableHttpResponse response = httpClient.execute(request)){

      if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK && response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
        String cause = HttpResponseUtil.getErrorMessage(response);
        getLog().error("uri: " + request.getURI().toString() + " repoMessage: " + cause);
        throw new ContentRepoException.ContentRepoExceptionBuilder(errorType)
            .url(request.getURI().toString())
            .repoMessage(cause)
            .build();
      }

      return response;

    } catch (IOException e) {
      getLog().error("Error handling the response, uri: " + request.getURI().toString() + " repoMessage: ", e);
      throw new ContentRepoException.ContentRepoExceptionBuilder(errorType)
          .baseException(e)
          .url(request.getURI().toString())
          .build();
    } finally {
      request.releaseConnection();
    }
  }

  public abstract Logger getLog();

}
