package org.plos.crepo.util;

import org.apache.commons.lang3.text.StrSubstitutor;

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

  protected Map<String,String> getContentInBucketMap(String repoServer, String bucketName, int offset, int limit, boolean includeDelete) {
    Map<String, String> values = getBucketBasicMap(repoServer, bucketName);
    values.put("offset", String.valueOf(offset));
    values.put("limit", String.valueOf(limit));
    values.put("includeDeleted", String.valueOf(includeDelete));
    return values;
  }

  protected Map<String,String> getContentInBucketMap(String repoServer, String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    Map<String, String> values = getContentInBucketMap(repoServer, bucketName, offset, limit, includeDeleted);
    values.put("tag", tag);
    return values;
  }


}
