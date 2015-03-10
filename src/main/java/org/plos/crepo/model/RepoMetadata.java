package org.plos.crepo.model;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
      String key = entry.getKey();
      Object value = entry.getValue();
      value = "userMetadata".equals(key) ? recursiveUnmodifiableNullableCopy(value) : recursiveImmutableCopy(value);
      builder.put(key, value);
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

  /**
   * Make a deep immutable copy of user-defined metadata. Allows null values in maps and lists. Because it was parsed
   * from JSON, require all maps to have only non-null {@code String}s as keys.
   */
  private static Object recursiveUnmodifiableNullableCopy(Object obj) {
    if (obj instanceof Collection) {
      List<Object> list = new ArrayList<>(((Collection<?>) obj).size());
      for (Object element : (Collection<?>) obj) {
        list.add(recursiveUnmodifiableNullableCopy(element));
      }
      return Collections.unmodifiableList(list);
    }
    if (obj instanceof Map) {
      Map<Object, Object> map = Maps.newHashMapWithExpectedSize(((Map<?, ?>) obj).size());
      for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
        String key = Preconditions.checkNotNull((String) entry.getKey());
        map.put(key, recursiveUnmodifiableNullableCopy(entry.getValue()));
      }
      return Collections.unmodifiableMap(map);
    }
    if (obj != null && !(obj instanceof String || obj instanceof Number || obj instanceof Boolean)) {
      // TODO: Throw an exception if a non-JSON-compatible, possibly mutable type is detected?
    }
    return obj;
  }

  public ImmutableMap<String, Object> getMapView() {
    return raw;
  }

  public RepoVersion getVersion() {
    String key = (String) raw.get("key");
    String versionChecksum = (String) raw.get("versionChecksum");
    return RepoVersion.createFromHex(key, versionChecksum);
  }

  public RepoVersionNumber getVersionNumber() {
    String key = (String) raw.get("key");
    int versionNumber = ((Number) raw.get("versionNumber")).intValue();
    return new RepoVersionNumber(key, versionNumber);
  }

  public Optional<RepoVersionTag> getTag() {
    String key = (String) raw.get("key");
    String tag = (String) raw.get("tag");
    return (tag == null) ? Optional.<RepoVersionTag>absent() : Optional.of(new RepoVersionTag(key, tag));
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

  public Optional<Object> getJsonUserMetadata() {
    Optional<String> raw = getRawUserMetadata();
    if (!raw.isPresent()) return Optional.absent();

    Object parsed;
    try {
      parsed = new Gson().fromJson(raw.get(), Object.class);
    } catch (JsonSyntaxException e) {
      return Optional.absent(); // TODO: Exception more appropriate instead?
    }
    
    if (parsed == null) {
      // Possible if the user submitted the string "null" as the JSON value
      return Optional.absent();
    }
    return Optional.of(recursiveUnmodifiableNullableCopy(parsed));
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
