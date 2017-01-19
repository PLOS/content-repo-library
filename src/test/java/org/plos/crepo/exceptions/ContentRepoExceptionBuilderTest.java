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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContentRepoExceptionBuilderTest {

  private ContentRepoException.ContentRepoExceptionBuilder builder;

  private static final String BASE_EXCEPTION_MESSAGE = "test exception";
  private static final String REPO_MESSAGE = "test repoMessage";
  private static final String KEY = "keyTest";
  private static final String URL = "http://testUrl";
  private ErrorType errorType = ErrorType.ErrorFetchingObjectMeta;
  private Exception baseException = new Exception(BASE_EXCEPTION_MESSAGE);

  @Before
  public void setUp(){
    builder = new ContentRepoException.ContentRepoExceptionBuilder(errorType);
  }

  @Test
  public void buildExceptionTest(){

    ContentRepoException contentRepoExcp = builder.baseException(baseException)
        .repoMessage(REPO_MESSAGE)
        .key(KEY)
        .url(URL)
        .build();

    assertNotNull(contentRepoExcp);
    assertEquals(errorType, contentRepoExcp.getErrorType());
    assertEquals(baseException, contentRepoExcp.getCause());
    assertTrue(contentRepoExcp.getMessage().contains(REPO_MESSAGE));
    assertTrue(contentRepoExcp.getMessage().contains(KEY));
    assertTrue(contentRepoExcp.getMessage().contains(URL));

  }

}
