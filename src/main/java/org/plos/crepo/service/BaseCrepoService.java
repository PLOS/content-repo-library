package org.plos.crepo.service;

import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.slf4j.Logger;

public abstract class BaseCrepoService {

  protected ContentRepoException serviceServerException(Exception e, String logMessage){
    getLog().error(logMessage, e);
    return new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ServerError)
        .baseException(e)
        .build();
  }

  protected abstract Logger getLog();

}
