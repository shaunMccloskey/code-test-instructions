package com.urlshortener.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.urlshortener.repository.UrlPairRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UrlPairServiceTest {

  @Mock private UrlPairRepository urlPairRepository;

  private UrlPairService urlPairService;

  private int aliasLength = 6;

  @BeforeEach
  void setUp() {
    urlPairService = new UrlPairService(aliasLength);
  }

  @Test
  void shortenUrl_whenCustomAlias_returnAlias() {
    String result = urlPairService.shortenUrl("testUrl");
    assertTrue(result.equals("testUrl"));
  }

  @Test
  void shortenUrl_whenNullCustomAlias_returnAlias() {
    String result = urlPairService.shortenUrl(null);
    assertEquals(result.length(), aliasLength);
  }

  @Test
  void shortenUrl_whenEmptyCustomAlias_returnAlias() {
    String result = urlPairService.shortenUrl("");
    assertEquals(result.length(), aliasLength);
  }

  @Test
  void shortenUrl_whenEmptyCustomAliasConflict_returnAlias() {
    String result = urlPairService.shortenUrl("");
    // TODO needs to check repo and loop until finds free value, how should it handel nothing free??
  }
}
