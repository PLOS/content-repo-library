package org.plos.crepo.model.validator;

import org.apache.commons.lang3.StringUtils;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.input.RepoObjectInput;

public class RepoObjectValidator {

  public static void validate(RepoObjectInput repoObjectInput) {

    if (StringUtils.isEmpty(repoObjectInput.getKey())) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyKey)
          .build();
    }

    if (repoObjectInput.getContentAccessor() == null) {
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyContent)
          .key(repoObjectInput.getKey())
          .build();
    }

    if (!repoObjectInput.canGetContentType()) {
      if (StringUtils.isEmpty(repoObjectInput.getContentType())) {
        throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyContentType)
            .key(repoObjectInput.getKey())
            .build();
      }
    }

  }

}
