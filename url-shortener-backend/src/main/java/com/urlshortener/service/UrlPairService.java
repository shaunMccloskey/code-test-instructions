package com.urlshortener.service;

import com.urlshortener.repository.UrlPairRepository;
import java.security.SecureRandom;

public class UrlPairService {

  private static final String URLCHARACTERS =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final SecureRandom RANDOM = new SecureRandom();

  private final int aliasLength;
  private final UrlPairRepository urlPairRepository;

  public UrlPairService(int aliasLength, UrlPairRepository urlPairRepository) {
    this.aliasLength = aliasLength;
    this.urlPairRepository = urlPairRepository;
  }

  //    shorten method to take in alias and url, if no alias given generate random alias,  check if
  // alias is unique and save new alias
  public String shortenUrl(String alias) {
    String customAlias = alias;
    if (alias == null || alias.trim().isEmpty()) {
      customAlias = generateAlias();
    }

    return customAlias;
  }

  private String generateAlias() {
    String alias;
    int attempts = 0;
    do {
      StringBuilder result = new StringBuilder(aliasLength);
      for (int i = 0; i < aliasLength; i++) {
        int index = RANDOM.nextInt(URLCHARACTERS.length());
        char nextChar = URLCHARACTERS.charAt(index);
        result.append(nextChar);
      }
      alias = result.toString();
      attempts++;
      if (attempts > 100) {
        throw new RuntimeException("Unable to generate unique alias after 100 attempts");
      }
    } while (urlPairRepository.existsByAlias(alias));
    return alias;
  }

  //        | - generateAlias method to generate random string
  //        | - helper get url
  //        | - helper delete url
  //        | - get all urls
  //        | - generate random alias
  //
  //

}
