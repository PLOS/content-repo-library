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
import org.plos.crepo.model.RepoObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RepoObjectValidatorTest {

  private RepoObjectValidator repoObjectValidator;
  private static String KEY = "testKey";

  private static final RepoObject.ContentAccessor CONTENT_ACCESSOR = () -> new ByteArrayInputStream(new byte[1]);

  @Before
  public void setUp(){
    repoObjectValidator = new RepoObjectValidator();
  }

  @Test
  public void valideRepoObjectTest(){

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
  public void emptyKeyTest(){

    RepoObject repoObject = mock(RepoObject.class);

    try{
      repoObjectValidator.validate(repoObject);
      fail("A content repo app was expected. ");
    } catch(ContentRepoException e){
      assertEquals(ErrorType.EmptyKey, e.getErrorType());
    }

  }

  @Test
  public void emptyContentRepoObjectTest(){

    RepoObject repoObject = mock(RepoObject.class);

    when(repoObject.getKey()).thenReturn("validaKey");
    when(repoObject.getContentAccessor()).thenReturn(null);

    try{
      repoObjectValidator.validate(repoObject);
      fail("A content repo app was expected. ");
    } catch(ContentRepoException e){
      assertEquals(ErrorType.EmptyContent, e.getErrorType());
      verify(repoObject, times(2)).getKey();
      verify(repoObject).getContentAccessor();
    }

  }

  @Test
  public void emptyContentTypeRepoObjectTest(){

    RepoObject repoObject = mock(RepoObject.class);

    when(repoObject.getKey()).thenReturn("validaKey");
    when(repoObject.getContentAccessor()).thenReturn(CONTENT_ACCESSOR);
    when(repoObject.getContentType()).thenReturn(null);
    when(repoObject.getKey()).thenReturn(KEY);

    try{
      repoObjectValidator.validate(repoObject);
      fail("A content repo app was expected. ");
    } catch(ContentRepoException e){
      assertEquals(ErrorType.EmptyContentType, e.getErrorType());
      assertTrue(e.getMessage().contains(KEY));
      verify(repoObject, times(2)).getKey();
      verify(repoObject, atLeastOnce()).getContentAccessor();
    }

  }

}
