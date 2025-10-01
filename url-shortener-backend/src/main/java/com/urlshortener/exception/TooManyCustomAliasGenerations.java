package com.urlshortener.exception;

public class TooManyCustomAliasGenerations extends RuntimeException {
  public TooManyCustomAliasGenerations(String message) {
    super(message);
  }
}
