package org.plos.crepo.config;


import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectionConfig {

  @Value("${connections.max.total}")
  private int conMaxTotal;

  @Value("${connections.max.default}")
  private int conMaxDefault;

  @Autowired
  ApplicationContext context;

  @Bean
  public CloseableHttpClient httpClient(){

    PoolingHttpClientConnectionManager ccm = new PoolingHttpClientConnectionManager();

    ccm.setMaxTotal(conMaxTotal);
    ccm.setDefaultMaxPerRoute(conMaxDefault);

    return HttpClients.custom().setConnectionManager(ccm).build();

  }

}
