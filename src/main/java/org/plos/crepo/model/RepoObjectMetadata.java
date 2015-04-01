package org.plos.crepo.model;

import com.google.common.base.Optional;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents output to the client, describing an object.
 */
public class RepoObjectMetadata extends RepoMetadata {

  public RepoObjectMetadata(Map<String, Object> raw) {
    super(raw);
  }

  public long getSize() {
    return ((Number) raw.get("size")).longValue();
  }

  public Optional<String> getContentType() {
    return Optional.fromNullable((String) raw.get("contentType"));
  }

  public Optional<String> getDownloadName() {
    return Optional.fromNullable((String) raw.get("downloadName"));
  }

  public List<URL> getReproxyUrls() {
    List<String> rawUrls = (List<String>) raw.get("reproxyURL");
    if (rawUrls == null) return new ArrayList<>(0);
    List<URL> urls = new ArrayList<>(rawUrls.size());
    for (String rawUrl : rawUrls) {
      try {
        urls.add(new URL(rawUrl));
      } catch (MalformedURLException e) {
        throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.ErrorFetchingReProxyUrl)
            .baseException(e)
            .build();
      }
    }
    return urls;
  }

}
