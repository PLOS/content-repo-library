package org.plos.crepo.config;


import com.google.gson.Gson;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ConnectionConfig {

  @Autowired
  private ContentRepoClientConfig clientConfig;

  @Autowired
  ApplicationContext context;

  @Bean
  public CloseableHttpClient httpClient() {

    PoolingHttpClientConnectionManager ccm = new PoolingHttpClientConnectionManager();

    ccm.setMaxTotal(clientConfig.getConnectionsMaxTotal());
    ccm.setDefaultMaxPerRoute(clientConfig.getConnectionsMaxDefault());

    return HttpClients.custom().setConnectionManager(ccm).build();

  }

  @Bean
  @Scope("prototype")
  public Gson gson() {
    return new Gson();
  }

}
