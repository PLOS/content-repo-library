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

package org.plos.crepo.model;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents output to the client, describing a collection.
 */
public class RepoCollectionList extends RepoCollectionMetadata {

  private final ImmutableList<RepoObjectMetadata> objects;

  public RepoCollectionList(Map<String, Object> raw) {
    super(raw);
    objects = parseObjects(raw);
  }

  private static ImmutableList<RepoObjectMetadata> parseObjects(Map<String, Object> raw) {
    List<Map<String, Object>> rawObjects = (List<Map<String, Object>>) raw.get("objects");
    List<RepoObjectMetadata> builtObjects = new ArrayList<>(rawObjects.size());
    for (Map<String, Object> rawObject : rawObjects) {
      builtObjects.add(new RepoObjectMetadata(rawObject));
    }
    return ImmutableList.copyOf(builtObjects);
  }

  public ImmutableList<RepoObjectMetadata> getObjects() {
    return objects;
  }

}
