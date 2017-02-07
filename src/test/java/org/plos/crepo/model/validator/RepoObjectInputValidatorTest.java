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

package org.plos.crepo.model.validator;

import org.junit.Before;
import org.junit.Test;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.input.RepoObjectInput;

import java.io.ByteArrayInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepoObjectInputValidatorTest {

  private RepoObjectValidator repoObjectValidator;
  private static String KEY = "testKey";

  private static final RepoObjectInput.ContentAccessor CONTENT_ACCESSOR = () -> new ByteArrayInputStream(new byte[1]);

  @Before
  public void setUp() {
    repoObjectValidator = new RepoObjectValidator();
  }

  @Test
  public void valideRepoObjectTest() {

    RepoObjectInput repoObjectInput = mock(RepoObjectInput.class);

    when(repoObjectInput.getKey()).thenReturn("validaKey");
    when(repoObjectInput.getContentAccessor()).thenReturn(CONTENT_ACCESSOR);
    when(repoObjectInput.getContentType()).thenReturn("text/plain");

    repoObjectValidator.validate(repoObjectInput);

    verify(repoObjectInput).getKey();
    verify(repoObjectInput, atLeastOnce()).getContentAccessor();
    verify(repoObjectInput).getContentType();

  }

  @Test
  public void emptyKeyTest() {

    RepoObjectInput repoObjectInput = mock(RepoObjectInput.class);

    try {
      repoObjectValidator.validate(repoObjectInput);
      fail("A content repo app was expected. ");
    } catch (ContentRepoException e) {
      assertEquals(ErrorType.EmptyKey, e.getErrorType());
    }

  }

  @Test
  public void emptyContentRepoObjectTest() {

    RepoObjectInput repoObjectInput = mock(RepoObjectInput.class);

    when(repoObjectInput.getKey()).thenReturn("validaKey");
    when(repoObjectInput.getContentAccessor()).thenReturn(null);

    try {
      repoObjectValidator.validate(repoObjectInput);
      fail("A content repo app was expected. ");
    } catch (ContentRepoException e) {
      assertEquals(ErrorType.EmptyContent, e.getErrorType());
      verify(repoObjectInput, times(2)).getKey();
      verify(repoObjectInput).getContentAccessor();
    }

  }

  @Test
  public void emptyContentTypeRepoObjectTest() {

    RepoObjectInput repoObjectInput = mock(RepoObjectInput.class);

    when(repoObjectInput.getKey()).thenReturn("validaKey");
    when(repoObjectInput.getContentAccessor()).thenReturn(CONTENT_ACCESSOR);
    when(repoObjectInput.getContentType()).thenReturn(null);
    when(repoObjectInput.getKey()).thenReturn(KEY);

    try {
      repoObjectValidator.validate(repoObjectInput);
      fail("A content repo app was expected. ");
    } catch (ContentRepoException e) {
      assertEquals(ErrorType.EmptyContentType, e.getErrorType());
      assertTrue(e.getMessage().contains(KEY));
      verify(repoObjectInput, times(2)).getKey();
      verify(repoObjectInput, atLeastOnce()).getContentAccessor();
    }

  }

}
