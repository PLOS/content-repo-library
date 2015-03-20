package org.plos.crepo.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Utility class used to handle the Http responses.
 */
public class HttpResponseUtil {

  public static String getResponseAsString(HttpResponse response) {
    HttpEntity entity = response.getEntity();
    try {
      return EntityUtils.toString(entity, CharEncoding.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getErrorMessage(HttpResponse response) {
    HttpEntity entity = response.getEntity();
    String responseMessage;
    try {
      responseMessage = EntityUtils.toString(entity, CharEncoding.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Gson gson = new Gson();
    JsonObject jsonObject = gson.fromJson(responseMessage, JsonObject.class);
    return jsonObject.get("message").getAsString();
  }

}
