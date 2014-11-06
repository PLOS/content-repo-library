package org.plos.crepo.service;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.mockito.Mockito;
import org.plos.crepo.util.HttpResponseUtil;
import org.powermock.api.mockito.PowerMockito;

public class BaseServiceTest {

  protected static final String KEY = "key";
  protected static final java.lang.String JSON_MSG = "{\"test\":\"mockJsonTest\" }";

  protected Gson gson;

  protected void mockStatics(HttpResponse mockResponse){
    PowerMockito.mockStatic(HttpResponseUtil.class);
    Mockito.when(HttpResponseUtil.getResponseAsString(mockResponse)).thenReturn(JSON_MSG);
  }
}
