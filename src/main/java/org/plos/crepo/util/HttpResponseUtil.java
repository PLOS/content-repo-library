package org.plos.crepo.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
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
    String errorMessage;
    try {
      responseMessage = EntityUtils.toString(entity, CharEncoding.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      Gson gson = new Gson();
      JsonElement responseElement = gson.fromJson(responseMessage, JsonElement.class);
      JsonElement message = responseElement.getAsJsonObject().get("message");
      errorMessage = (message == null)? "No error message":message.getAsString();
    } catch (JsonSyntaxException e) { // Catch the possibles NOT JSON responses.
      errorMessage = "There was an error trying to obtain the JSON response error: " + response.getStatusLine();
    }
    return errorMessage;
  }

}
