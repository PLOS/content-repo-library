package org.plos.crepo.model.validator;

/**
 * Created by lmasola on 10/27/14.
 */
public interface RepoValidator<T> {

  public void validate(T t);

}
