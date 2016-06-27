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
