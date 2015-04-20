package org.plos.crepo.model.validator;

import org.apache.commons.lang3.StringUtils;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.RepoObject;

public class RepoObjectValidator {

  public static void validate(RepoObject repoObject) {

    if (StringUtils.isEmpty(repoObject.getKey())) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyKey)
          .build();
    }

    if (repoObject.getContentAccessor() == null) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyContent)
          .key(repoObject.getKey())
          .build();
    }

    if (!repoObject.canGetContentType()) {
      if (StringUtils.isEmpty(repoObject.getContentType())) {
        throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyContentType)
            .key(repoObject.getKey())
            .build();
      }
    }

  }

}
