package com.urlshortener.exception;

public class AliasNotFoundException extends RuntimeException {
  public AliasNotFoundException(String alias) {
    super("Alias '" + alias + "' already exists");
  }
}
