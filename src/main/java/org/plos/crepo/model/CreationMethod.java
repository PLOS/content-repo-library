package org.plos.crepo.model;

public enum CreationMethod {

  NEW("new"),
  VERSION("version"),
  AUTO("auto");

  private String value;

  CreationMethod(String value){
    this.value = value;
  }

  public String getValue(){
    return this.value;
  }

}

