/*
 * Copyright 2017 Public Library of Science
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
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
