package org.plos.crepo.model.metadata;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import org.plos.crepo.model.Status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Represents metadata about a repo entity, to output to the client.
 */
public abstract class RepoMetadata {
  protected final ImmutableMap<String, Object> raw;

  @SuppressWarnings("unchecked")
    // recursiveImmutableCopy guarantees type safety
  RepoMetadata(Map<String, Object> raw) {
    this.raw = (ImmutableMap<String, Object>) recursiveImmutableCopy(raw);
  }

  private static ImmutableMap<String, Object> defensiveCopy(Map<String, Object> raw) {
    ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
    for (Map.Entry<String, Object> entry : raw.entrySet()) {
      builder.put(entry.getKey(), recursiveImmutableCopy(entry.getValue()));
    }
    return builder.build();
  }

  /**
   * Make a deep immutable copy of the raw metadata. Requires all elements to be non-null. Because it was parsed from
   * JSON, require all maps to have only {@code String}s as keys.
   */
  private static Object recursiveImmutableCopy(Object obj) {
    Preconditions.checkNotNull(obj);
    if (obj instanceof Iterable) {
      ImmutableList.Builder<Object> builder = ImmutableList.builder();
      for (Object element : (Iterable<?>) obj) {
        builder.add(recursiveImmutableCopy(element));
      }
      return builder.build();
    }
    if (obj instanceof Map) {
      ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
      for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
        String key = (String) entry.getKey();
        Object value = recursiveImmutableCopy(entry.getValue());
        builder.put(key, value);
      }
      return builder.build();
    }
    if (!(obj instanceof String || obj instanceof Number || obj instanceof Boolean)) {
      // TODO: Throw an exception if a non-JSON-compatible, possibly mutable type is detected?
    }
    return obj;
  }

  public ImmutableMap<String, Object> getMapView() {
    return raw;
  }

  public String getKey() {
    return (String) raw.get("key");
  }

  public UUID getUuid() {
    return UUID.fromString((String) raw.get("uuid"));
  }

  public Timestamp getTimestamp() {
    return Timestamp.valueOf((String) raw.get("timestamp"));
  }

  public Timestamp getCreationDate() {
    return Timestamp.valueOf((String) raw.get("creationDate"));
  }

  public Status getStatus() {
    return Status.valueOf((String) raw.get("status"));
  }

  public Optional<String> getRawUserMetadata() {
    return Optional.fromNullable((String) raw.get("userMetadata"));
  }

  // Store to avoid redundant parsing. Null means uninitialized; absent means this has no userMetadata.
  private transient Optional<Object> jsonUserMetadata = null;

  public Optional<Object> getJsonUserMetadata() {
    if (jsonUserMetadata != null) return jsonUserMetadata;
    Optional<String> raw = getRawUserMetadata();
    if (!raw.isPresent()) return Optional.absent();

    final Gson gson = new Gson();
    JsonElement parsed;
    try {
      parsed = gson.fromJson(raw.get(), JsonElement.class);
    } catch (JsonSyntaxException e) {
      return Optional.absent(); // TODO: Exception more appropriate instead?
    }

    Object converted = convertJsonToImmutable(parsed);
    return jsonUserMetadata = Optional.fromNullable(converted);
  }

  @VisibleForTesting
  static Object convertJsonToImmutable(JsonElement element) {
    if (element.isJsonNull()) {
      return null;
    }
    if (element.isJsonPrimitive()) {
      JsonPrimitive primitive = element.getAsJsonPrimitive();
      if (primitive.isString()) return primitive.getAsString();
      if (primitive.isNumber()) return asNumber(primitive);
      if (primitive.isBoolean()) return primitive.getAsBoolean();
      throw new RuntimeException("JsonPrimitive is not one of the expected primitive types");
    }
    if (element.isJsonArray()) {
      JsonArray array = element.getAsJsonArray();
      if (array.size() == 0) return Collections.emptyList();
      List<Object> convertedList = new ArrayList<>(array.size());
      for (JsonElement arrayElement : array) {
        Object convertedElement = convertJsonToImmutable(arrayElement);
        convertedList.add(convertedElement);
      }
      return Collections.unmodifiableList(convertedList);
    }
    if (element.isJsonObject()) {
      Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet();
      if (entries.size() == 0) return Collections.emptyMap();
      Map<String, Object> convertedMap = Maps.newHashMapWithExpectedSize(entries.size());
      for (Map.Entry<String, JsonElement> entry : entries) {
        String key = Preconditions.checkNotNull(entry.getKey());
        Object value = convertJsonToImmutable(entry.getValue());
        convertedMap.put(key, value);
      }
      return Collections.unmodifiableMap(convertedMap);
    }
    throw new RuntimeException("JsonElement is not one of the expected subtypes");
  }

  /**
   * Special case for parsing numbers from JSON.
   * <p/>
   * This is used instead of {@link JsonPrimitive#getAsNumber}, which may return a {@link
   * com.google.gson.internal.LazilyParsedNumber}. The problem with LazilyParsedNumber is that it doesn't belong to one
   * of the familiar Number subtypes (e.g., Integer, Double, Long), so its {@link Object#equals} and {@link
   * Object#hashCode} don't behave intuitively.
   * <p/>
   * Our imperfect solution is to use {@link Number#doubleValue()}, to be consistent with how Gson produces numbers
   * nested in arrays and objects. That is:
   * <pre>
   *   new Gson().fromJson("[0]", List.class).get(0).getClass() // is Double
   * </pre>
   * Compare to:
   * <pre>
   *   new Gson().fromJson("0", Number.class).getClass() // is LazilyParsedNumber
   * </pre>
   * This ensures that numbers parsed from JSON can be compared consistently with {@link Object#equals}, whether they
   * were formatted as integers or decimals. The client should treat such objects as abstract Numbers and, if they
   * require a particular subtype (such as Integer), call the appropriate method (such as {@link Number#intValue}).
   *
   * @param jsonPrimitive a primitive JSON number
   * @return a Java-native Number object of equivalent value
   */
  private static Number asNumber(JsonPrimitive jsonPrimitive) {
    return jsonPrimitive.getAsNumber().doubleValue();
  }

  public Map<String, Object> inlineJsonUserMetadata() {
    Map<String, Object> thisMetadata = new LinkedHashMap<>(raw);
    Optional<Object> userMetadata = getJsonUserMetadata();
    if (userMetadata.isPresent()) {
      thisMetadata.put("userMetadata", userMetadata.get());
    } else {
      thisMetadata.remove("userMetadata");
    }
    return thisMetadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return raw.equals(((RepoMetadata) o).raw);
  }

  @Override
  public int hashCode() {
    return raw.hashCode();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + raw;
  }
}
