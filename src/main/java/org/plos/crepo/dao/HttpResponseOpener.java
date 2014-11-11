package org.plos.crepo.dao;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;

public interface HttpResponseOpener {

  CloseableHttpResponse openResponse(HttpUriRequest request) throws IOException;

}
