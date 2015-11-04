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
