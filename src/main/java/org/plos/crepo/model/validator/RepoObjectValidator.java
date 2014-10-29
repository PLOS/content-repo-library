package org.plos.crepo.model.validator;

import org.apache.commons.lang3.StringUtils;
import org.plos.crepo.exceptions.ContentRepoException;
import org.plos.crepo.exceptions.ErrorType;
import org.plos.crepo.model.RepoObject;
import org.springframework.stereotype.Component;

@Component
public class RepoObjectValidator {

  public void validate(RepoObject repoObject){

    if (repoObject.getFileContent() == null && repoObject.getByteContent() == null){
      throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyContent)
          .key(repoObject.getKey())
          .build();
    }

    if (repoObject.getByteContent() != null){
      if (StringUtils.isEmpty(repoObject.getContentType())){
        throw new ContentRepoException.ContentRepoExceptionBuilder(ErrorType.EmptyContentType)
            .key(repoObject.getKey())
            .build();
      }
    }

  }

}
