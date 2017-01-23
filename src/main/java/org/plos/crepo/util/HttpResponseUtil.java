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
