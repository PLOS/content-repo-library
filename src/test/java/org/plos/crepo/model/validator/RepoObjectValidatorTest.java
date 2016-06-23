package org.plos.crepo.model.validator;

import org.junit.Before;
import org.junit.Test;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.input.RepoObject;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepoObjectValidatorTest {

  private RepoObjectValidator repoObjectValidator;
  private static String KEY = "testKey";

  private static final RepoObject.ContentAccessor CONTENT_ACCESSOR = () -> new ByteArrayInputStream(new byte[1]);

  @Before
  public void setUp() {
    repoObjectValidator = new RepoObjectValidator();
  }

  @Test
  public void valideRepoObjectTest() {

    RepoObject repoObject = mock(RepoObject.class);

    when(repoObject.getKey()).thenReturn("validaKey");
    when(repoObject.getContentAccessor()).thenReturn(CONTENT_ACCESSOR);
    when(repoObject.getContentType()).thenReturn("text/plain");

    repoObjectValidator.validate(repoObject);

    verify(repoObject).getKey();
    verify(repoObject, atLeastOnce()).getContentAccessor();
    verify(repoObject).getContentType();

  }

  @Test
  public void emptyKeyTest() {

    RepoObject repoObject = mock(RepoObject.class);

    try {
      repoObjectValidator.validate(repoObject);
      fail("A content repo app was expected. ");
    } catch (ContentRepoException e) {
      assertEquals(ErrorType.EmptyKey, e.getErrorType());
    }

  }

  @Test
  public void emptyContentRepoObjectTest() {

    RepoObject repoObject = mock(RepoObject.class);

    when(repoObject.getKey()).thenReturn("validaKey");
    when(repoObject.getContentAccessor()).thenReturn(null);

    try {
      repoObjectValidator.validate(repoObject);
      fail("A content repo app was expected. ");
    } catch (ContentRepoException e) {
      assertEquals(ErrorType.EmptyContent, e.getErrorType());
      verify(repoObject, times(2)).getKey();
      verify(repoObject).getContentAccessor();
    }

  }

  @Test
  public void emptyContentTypeRepoObjectTest() {

    RepoObject repoObject = mock(RepoObject.class);

    when(repoObject.getKey()).thenReturn("validaKey");
    when(repoObject.getContentAccessor()).thenReturn(CONTENT_ACCESSOR);
    when(repoObject.getContentType()).thenReturn(null);
    when(repoObject.getKey()).thenReturn(KEY);

    try {
      repoObjectValidator.validate(repoObject);
      fail("A content repo app was expected. ");
    } catch (ContentRepoException e) {
      assertEquals(ErrorType.EmptyContentType, e.getErrorType());
      assertTrue(e.getMessage().contains(KEY));
      verify(repoObject, times(2)).getKey();
      verify(repoObject, atLeastOnce()).getContentAccessor();
    }

  }

}
