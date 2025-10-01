package com.urlshortener.service;

import com.urlshortener.exception.AliasAlreadyExistsException;
import com.urlshortener.exception.AliasNotFoundException;
import com.urlshortener.model.UrlPair;
import com.urlshortener.model.dto.ShortenUrlRequest;
import com.urlshortener.model.dto.ShortenUrlResponse;
import com.urlshortener.model.dto.UrlListResponse;
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
  private final String baseUrl;
  private final UrlPairRepository urlPairRepository;

  public UrlPairService(int aliasLength, UrlPairRepository urlPairRepository, String baseUrl) {
    this.aliasLength = aliasLength;
    this.urlPairRepository = urlPairRepository;
    this.baseUrl = baseUrl;
  }

  public ShortenUrlResponse shortenUrl(ShortenUrlRequest request) {
    String customAlias = determineAlias(request.getCustomAlias());

    if (urlPairRepository.existsByAlias(customAlias)) {
      throw new AliasAlreadyExistsException(customAlias);
    }

    urlPairRepository.save(new UrlPair(customAlias, request.getFullUrl()));

    String shortUrl = baseUrl + "/" + customAlias;
    return new ShortenUrlResponse(shortUrl);
  }

  private String determineAlias(String customAlias) {
    if (customAlias != null && !customAlias.trim().isEmpty()) {
      return customAlias.trim();
    }
    return generateAlias();
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
        .orElseThrow(() -> new AliasNotFoundException(alias));
  }

  //        | - helper delete url
  public void deleteUrlPair(String alias) {
    if (!urlPairRepository.existsByAlias(alias)) {
      throw new AliasNotFoundException(alias);
    }
    urlPairRepository.deleteByAlias(alias);
  }

  //        | - get all urls
  @Transactional(readOnly = true)
  public List<UrlListResponse> getAllUrls() {
    return urlPairRepository.findAll().stream()
        .map(
            urlPair ->
                new UrlListResponse(
                    urlPair.getAlias(), urlPair.getUrl(), baseUrl + "/" + urlPair.getAlias()))
        .collect(Collectors.toList());
  }
}
