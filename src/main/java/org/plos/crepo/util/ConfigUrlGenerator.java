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

import static org.plos.crepo.util.BaseUrlGenerator.getUrlBasicMap;
import static org.plos.crepo.util.BaseUrlGenerator.replaceUrl;

/**
 * Generates the content repo urls for config services.
 */
public class ConfigUrlGenerator {

  private static final String HAS_REPROXY_URL = "${repoServer}/hasXReproxy";
  private static final String REPO_CONFIG_URL = "${repoServer}/config";
  private static final String REPO_STATUS_URL = "${repoServer}/status";

  public static String getHasReproxyUrl(String repoServer) {
    return replaceUrl(HAS_REPROXY_URL, getUrlBasicMap(repoServer));
  }

  public static String getRepoConfigUrl(String repoServer) {
    return replaceUrl(REPO_CONFIG_URL, getUrlBasicMap(repoServer));
  }

  public static String getRepoStatusUrl(String repoServer) {
    return replaceUrl(REPO_STATUS_URL, getUrlBasicMap(repoServer));
  }

}
