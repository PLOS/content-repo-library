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

package org.plos.crepo.util;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.HashMap;
import java.util.Map;

class BaseUrlGenerator {

  static String replaceUrl(String url, Map<String, String> values){
    StrSubstitutor sub = new StrSubstitutor(values);
    return sub.replace(url);
  }

  static Map<String, String> getUrlBasicMap(String repoServer){
    Map<String, String> values = new HashMap<String, String>();
    values.put("repoServer", repoServer);
   return values;
  }

  static Map<String, String> getBucketBasicMap(String repoServer, String bucketName){
    Map<String, String> values = getUrlBasicMap(repoServer);
    values.put("bucketName", bucketName);
    return values;
  }

  static Map<String,String> getContentInBucketMap(String repoServer, String bucketName, int offset, int limit, boolean includeDelete) {
    Map<String, String> values = getBucketBasicMap(repoServer, bucketName);
    values.put("offset", String.valueOf(offset));
    values.put("limit", String.valueOf(limit));
    values.put("includeDeleted", String.valueOf(includeDelete));
    return values;
  }

  static Map<String,String> getContentInBucketMap(String repoServer, String bucketName, int offset, int limit, boolean includeDeleted, String tag) {
    Map<String, String> values = getContentInBucketMap(repoServer, bucketName, offset, limit, includeDeleted);
    values.put("tag", tag);
    return values;
  }


}
