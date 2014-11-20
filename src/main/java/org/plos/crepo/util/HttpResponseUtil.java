package org.plos.crepo.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class used to handle the Http responses.
 */
public class HttpResponseUtil {

  private static final Logger log = LoggerFactory.getLogger(HttpResponseUtil.class);

  public static String getResponseAsString(HttpResponse response) {

    try {

      HttpEntity entity = response.getEntity();
      return EntityUtils.toString(entity, CharEncoding.UTF_8);

    } catch (Exception e) {
      log.error("error streaming to string", e);
      return null;
    }
  }

  public static String getErrorMessage(HttpResponse response) {

    try {

      HttpEntity entity = response.getEntity();
      String responseMessage =  EntityUtils.toString(entity, CharEncoding.UTF_8);
      Gson gson = new Gson();
      JsonObject jsonObject = gson.fromJson(responseMessage, JsonObject.class);
      return jsonObject.get("message").getAsString();

    } catch (Exception e) {
      log.error("error streaming to string", e);
      return null;
    }
  }

}
