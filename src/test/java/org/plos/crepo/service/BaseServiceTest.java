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

package org.plos.crepo.service;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.plos.crepo.model.RepoMetadata;
import org.plos.crepo.model.RepoVersion;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseServiceTest {

  protected static final String KEY = "key";
  protected static final String JSON_MSG = "{\"test\":\"mockJsonTest\" }";
  protected static final String FAIL_MSG = "a ContentRepoException was expected";

  protected static final ImmutableMap<String, Object> TEST_METADATA = ImmutableMap.<String, Object>of("testField", "testValue");
  protected static final ImmutableList<Map<String, Object>> TEST_METADATA_LIST = ImmutableList.<Map<String, Object>>of(TEST_METADATA);

  protected final Gson gson = new Gson();

  protected CloseableHttpResponse mockJsonResponse(final Object responseBody) throws IOException {
    CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
    HttpEntity mockEntity = mock(HttpEntity.class);
    when(httpResponse.getEntity()).thenReturn(mockEntity);
    when(mockEntity.getContent()).thenAnswer(invocationOnMock -> {
      String json = gson.toJson(responseBody);
      return new ByteArrayInputStream(json.getBytes(Charsets.UTF_8));
    });
    return httpResponse;
  }

  public static List<Map<String, Object>> asRawList(List<? extends RepoMetadata> metadataList) {
    return Lists.transform(metadataList,
        new Function<RepoMetadata, Map<String, Object>>() {
          @Override
          public Map<String, Object> apply(RepoMetadata repoMetadata) {
            return repoMetadata.getMapView();
          }
        });
  }

  protected static class TestExpectedException extends IOException {
  }

}
