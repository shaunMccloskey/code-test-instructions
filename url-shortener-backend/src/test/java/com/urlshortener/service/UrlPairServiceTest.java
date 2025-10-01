package com.urlshortener.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.urlshortener.exception.AliasNotFoundException;
import com.urlshortener.model.UrlPair;
import com.urlshortener.repository.UrlPairRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UrlPairServiceTest {

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
    UrlPair urlPairExample = new UrlPair("test", "http://example.com");
    when(urlPairRepository.findByAlias("test")).thenReturn(Optional.of(urlPairExample));
    assertThrows(AliasNotFoundException.class, () -> urlPairService.getUrlPairByAlias("not_there"));
  }

  @Test
  void deleteUrlPair_whenAliasExists_returnAlias() {
    UrlPair urlPairExample = new UrlPair("test", "http://example.com");
    when(urlPairRepository.findByAlias("test")).thenReturn(Optional.of(urlPairExample));
    urlPairService.deleteUrlPair("test");
    verify(urlPairRepository).deleteByAlias("test");
  }

  @Test
  void deleteUrlPair_whenAliasDoesNotExists_return() {
    UrlPair urlPairExample = new UrlPair("test", "http://example.com");
    when(urlPairRepository.findByAlias("test")).thenReturn(Optional.of(urlPairExample));
    assertThrows(AliasNotFoundException.class, () -> urlPairService.deleteUrlPair("not_there"));
  }
}
