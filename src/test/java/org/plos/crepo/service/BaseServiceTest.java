package org.plos.crepo.service;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.mockito.Mockito;
import org.plos.crepo.model.RepoObjectVersion;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;

public class BaseServiceTest {

  protected static final String KEY = "key";
  protected static final String JSON_MSG = "{\"test\":\"mockJsonTest\" }";
  protected static final String FAIL_MSG = "a ContentRepoException was expected";

  protected Gson gson;

  protected void mockStatics(HttpResponse mockResponse){
    PowerMockito.mockStatic(HttpResponseUtil.class);
    Mockito.when(HttpResponseUtil.getResponseAsString(mockResponse)).thenReturn(JSON_MSG);
  }

  protected static RepoObjectVersion createDummyVersion(String key, String dummyChecksum) {
    byte[] bytes = Hashing.sha1().hashString(dummyChecksum, Charsets.UTF_8).asBytes();
    return RepoObjectVersion.create(key, bytes);
  }

}
