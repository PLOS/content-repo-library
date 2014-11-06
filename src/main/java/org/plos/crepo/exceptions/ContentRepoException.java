/*
 * Copyright (c) 2006-2014 by Public Library of Science
 * http://plos.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.plos.crepo.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Cross layer exception that wraps all the exceptions of content-repo-library
 */
public class ContentRepoException extends RuntimeException {

  private static final long serialVersionUID = -7747202126932506670L;
  private ErrorType errorType;

  public ErrorType getErrorType() {
    return errorType;
  }

  public ContentRepoException(ErrorType errorType, String message) {
    super(message);
    this.errorType = errorType;
  }

  public ContentRepoException(ErrorType errorType, String message, Throwable cause) {
    super(message, cause);
    this.errorType = errorType;
  }


  public static class ContentRepoExceptionBuilder {

    private static final String URL = "url";
    private static final String KEY = "key";
    private static final String REPO_CAUSE = "repoMessage";
    private static final String DELIMITER_1 = ", ";
    private static final String DELIMITER_2 = " : ";

    private ErrorType errorType;
    private Map<String,String> properties;
    private Throwable baseException;

    public ContentRepoExceptionBuilder(ErrorType errorType){
      this.errorType = errorType;
      properties = new HashMap<String, String>();
    }

    public ContentRepoExceptionBuilder baseException(Throwable e){
      baseException = e;
      return this;
    }

    public ContentRepoExceptionBuilder url(String url){
      properties.put(URL, url);
      return this;
    }

    public ContentRepoExceptionBuilder key(String key){
      properties.put(KEY, key);
      return this;
    }

    public ContentRepoExceptionBuilder repoMessage(String cause){
      properties.put(REPO_CAUSE, cause);
      return this;
    }

    public ContentRepoException build(){
      StringBuilder message = new StringBuilder();
      message.append(errorType.getMessage());
      if (properties.size() > 0){
        for (String key : properties.keySet()){
          message.append(DELIMITER_1);
          message.append(key);
          message.append(DELIMITER_2);
          message.append(properties.get(key));
        }
      }
      if (baseException != null){
        return new ContentRepoException(errorType, message.toString(), baseException);
      }
      return new ContentRepoException(errorType, message.toString());
    }

  }

}
