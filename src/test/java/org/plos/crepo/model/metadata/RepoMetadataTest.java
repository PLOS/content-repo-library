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

package org.plos.crepo.model.metadata;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class RepoMetadataTest {

  private static void assertIsUnmodifiable(Collection<?> collection) {
    try {
      collection.add(null);
      fail("Collection allowed insertion");
    } catch (UnsupportedOperationException expected) {
      assertNotNull(expected);
    }
  }

  private static void assertIsUnmodifiable(Map<?, ?> map) {
    try {
      map.put(null, null);
      fail("Map allowed insertion");
    } catch (UnsupportedOperationException expected) {
      assertNotNull(expected);
    }
  }

  @Test
  public void testConvertJsonToImmutable() {
    String json = "{\"string\": \"foo\", \"number\": 5, \"boolean\": true, \"null\": null, " +
        "\"map\": {\"bar\": \"baz\"}, \"list\": [\"a\",\"b\",1,2,true,false], " +
        "\"deepNested\": [[]] } ";

    Map<String, Object> expected = new HashMap<>();
    expected.put("string", "foo");
    expected.put("number", 5.0);
    expected.put("boolean", true);
    expected.put("null", null);
    expected.put("map", ImmutableMap.of("bar", "baz"));
    expected.put("list", ImmutableList.of("a", "b", 1.0, 2.0, true, false));
    expected.put("deepNested", ImmutableList.of(ImmutableList.of()));

    JsonElement jsonElement = new Gson().fromJson(json, JsonElement.class);
    Map<?, ?> actual = (Map<?, ?>) RepoMetadata.convertJsonToImmutable(jsonElement);

    assertEquals(expected, actual);
    assertIsUnmodifiable(actual);
    assertIsUnmodifiable((Map<?, ?>) actual.get("map"));
    assertIsUnmodifiable((Collection<?>) actual.get("list"));
    assertIsUnmodifiable((Collection<?>) ((List<?>) actual.get("deepNested")).get(0));
  }

}
