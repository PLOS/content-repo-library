package org.plos.crepo.util;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class BaseUrlGenerator {

  protected String replaceUrl(String url, Map<String, String> values){
    StrSubstitutor sub = new StrSubstitutor(values);
    return sub.replace(url);
  }

  protected Map<String, String> getUrlBasicMap(String repoServer){
    Map<String, String> values = new HashMap<String, String>();
    values.put("repoServer", repoServer);
   return values;
  }

  protected Map<String, String> getBucketBasicMap(String repoServer, String bucketName){
    Map<String, String> values = getUrlBasicMap(repoServer);
    values.put("bucketName", bucketName);
    return values;
  }


}
