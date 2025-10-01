package com.urlshortener.service;

import com.urlshortener.exception.AliasAlreadyExistsException;
import com.urlshortener.model.UrlPair;
import com.urlshortener.repository.UrlPairRepository;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

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

  public String shortenUrl(String alias, String fullUrl) {
    String customAlias;
    if (alias == null || alias.trim().isEmpty()) {
      customAlias = generateAlias();
    } else {
      customAlias = alias.trim();
    }
    urlPairRepository.save(new UrlPair(customAlias, fullUrl));

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

  //        | - helper get url
  @Transactional(readOnly = true)
  public UrlPair getUrlPairByAlias(String alias) {
    return urlPairRepository
        .findByAlias(alias)
        .orElseThrow(() -> new AliasAlreadyExistsException(alias));
  }

  //        | - helper delete url
  public void deleteUrlPair(String alias) {
    urlPairRepository.deleteByAlias(alias);
  }

  //        | - get all urls
  @Transactional(readOnly = true)
  public List<UrlPair> getAllUrls() {
    return urlPairRepository.findAll().stream().collect(Collectors.toList());
  }
}
