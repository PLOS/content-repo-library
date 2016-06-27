package org.plos.crepo.exceptions;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ContentRepoExceptionBuilderTest {

  private ContentRepoException.ContentRepoExceptionBuilder builder;

  private static final String BASE_EXCEPTION_MESSAGE = "test exception";
  private static final String REPO_MESSAGE = "test repoMessage";
  private static final String KEY = "keyTest";
  private static final String URL = "http://testUrl";
  private ErrorType errorType = ErrorType.ErrorFetchingObjectMeta;
  private Exception baseException = new Exception(BASE_EXCEPTION_MESSAGE);

  @Before
  public void setUp() {
    builder = new ContentRepoException.ContentRepoExceptionBuilder(errorType);
  }

  @Test
  public void buildExceptionTest() {

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
