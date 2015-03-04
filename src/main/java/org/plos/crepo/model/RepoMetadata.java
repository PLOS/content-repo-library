package org.plos.crepo.model;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * Represents metadata about a repo entity, to output to the client.
 */
public abstract class RepoMetadata {
  protected final Map<String, Object> raw;

  RepoMetadata(Map<String, Object> raw) {
    this.raw = (Map<String, Object>) recursiveImmutableCopy(raw);
  }

  protected static Object recursiveImmutableCopy(Object obj) {
    Preconditions.checkNotNull(obj);
    if (obj instanceof Iterable) {
      ImmutableList.Builder<Object> builder = ImmutableList.builder();
      for (Object element : (Iterable<?>) obj) {
        builder.add(recursiveImmutableCopy(element));
      }
      return builder.build();
    }
    if (obj instanceof Map) {
      ImmutableMap.Builder<Object, Object> builder = ImmutableMap.builder();
      for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
        builder.put(recursiveImmutableCopy(entry.getKey()), recursiveImmutableCopy(entry.getValue()));
      }
      return builder.build();
    }
    return obj;
  }

  public Map<String, Object> getMapView() {
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
