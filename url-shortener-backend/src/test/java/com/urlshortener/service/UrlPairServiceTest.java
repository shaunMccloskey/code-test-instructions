package com.urlshortener.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.urlshortener.exception.AliasNotFoundException;
import com.urlshortener.model.UrlPair;
import com.urlshortener.repository.UrlPairRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UrlPairServiceTest {

  @Mock private UrlPairRepository urlPairRepository;

  private UrlPairService urlPairService;

  private final int aliasLength = 6;

  @BeforeEach
  void setUp() {
    urlPairService = new UrlPairService(aliasLength, urlPairRepository);
  }

  @Test
  void shortenUrl_whenCustomAlias_returnAlias() {
    String result = urlPairService.shortenUrl("testUrl", "https://example.com");
    assertEquals("testUrl", result);
  }

  @Test
  void shortenUrl_whenNullCustomAlias_returnAlias() {
    when(urlPairRepository.existsByAlias(anyString())).thenReturn(false);
    String result = urlPairService.shortenUrl(null, "https://example.com");
    assertEquals(result.length(), aliasLength);
  }

  @Test
  void shortenUrl_whenEmptyCustomAlias_returnAlias() {
    when(urlPairRepository.existsByAlias(anyString())).thenReturn(false);
    String result = urlPairService.shortenUrl("", "https://example.com");
    assertEquals(result.length(), aliasLength);
  }

  @Test
  void shortenUrl_whenEmptyCustomAliasConflict_returnAlias() {
    when(urlPairRepository.existsByAlias(anyString())).thenReturn(true);
    assertThrows(
        RuntimeException.class, () -> urlPairService.shortenUrl("", "https://example.com"));
  }

  @Test
  void getUrlPairByAlias_whenAliasExists_returnAlias() {
    UrlPair urlPairExample = new UrlPair("test", "http://example.com");
    when(urlPairRepository.findByAlias("test")).thenReturn(Optional.of(urlPairExample));
    UrlPair result = urlPairService.getUrlPairByAlias("test");
    assertEquals(urlPairExample, result);
  }

  @Test
  void getUrlPairByAlias_whenAliasDoesNotExists_return() {
    when(urlPairRepository.findByAlias("test")).thenReturn(Optional.empty());
    assertThrows(AliasNotFoundException.class, () -> urlPairService.getUrlPairByAlias("test"));
  }

  @Test
  void deleteUrlPair_whenAliasExists_returnAlias() {
    when(urlPairRepository.existsByAlias("test")).thenReturn(true);
    urlPairService.deleteUrlPair("test");
    verify(urlPairRepository).deleteByAlias("test");
  }

  @Test
  void deleteUrlPair_whenAliasDoesNotExists_return() {
    when(urlPairRepository.existsByAlias("not_there")).thenReturn(false);
    assertThrows(AliasNotFoundException.class, () -> urlPairService.deleteUrlPair("not_there"));
  }

  @Test
  void getAllUrls_whenAliasExist_returnFullList() {
    List<UrlPair> urls =
        List.of(
            new UrlPair("first", "http://example.com"),
            new UrlPair("second", "http://example.test.com"));
    when(urlPairRepository.findAll()).thenReturn(urls);

    List<UrlPair> result = urlPairService.getAllUrls();

    assertEquals(2, result.size());
    assertEquals("first", result.get(0).getAlias());
    assertEquals("http://example.com", result.get(0).getUrl());
    assertEquals("second", result.get(1).getAlias());
    assertEquals("http://example.test.com", result.get(1).getUrl());
  }
}
