package org.plos.crepo.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Utility class used to handle the Http responses.
 */
public class HttpResponseUtil {

  public static String getErrorMessage(HttpResponse response) {
    HttpEntity entity = response.getEntity();
    String responseMessage;
    try {
      responseMessage = EntityUtils.toString(entity, CharEncoding.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Gson gson = new Gson();
    JsonElement responseElement = gson.fromJson(responseMessage, JsonElement.class);
    JsonElement message = responseElement.getAsJsonObject().get("message");
    return (message == null) ? "No error message" : message.getAsString();
  }

}
