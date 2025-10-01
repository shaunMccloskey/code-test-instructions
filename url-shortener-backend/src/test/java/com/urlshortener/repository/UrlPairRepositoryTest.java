package com.urlshortener.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.urlshortener.model.UrlPair;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataMongoTest
@Testcontainers
class UrlPairRepositoryTest {

  @Container static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

  @DynamicPropertySource
  static void setProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
  }

  @Autowired private UrlPairRepository urlPairRepository;

  @BeforeEach
  void setup() {
    urlPairRepository.deleteAll();
  }

  @Test
  void findByAlias_whenAliasDoesNotExist_returnsEmpty() {
    Optional<UrlPair> result = urlPairRepository.findByAlias("not_there");

    assertFalse(result.isPresent());
  }

  @Test
  void findByAlias_whenAliasExist_returnsUrlPair() {
    UrlPair urlPair = new UrlPair("site", "https://example.com");
    urlPairRepository.save(urlPair);

    Optional<UrlPair> result = urlPairRepository.findByAlias("site");

    assertTrue(result.isPresent());
    assertEquals("site", result.get().getAlias());
    assertEquals("https://example.com", result.get().getUrl());
  }

  @Test
  void existsByAlias_whenAliasDoesNotExist_returnFalse() {
    UrlPair urlPair = new UrlPair("site", "https://example.com");
    urlPairRepository.save(urlPair);

    boolean result = urlPairRepository.existsByAlias("nope");

    assertFalse(result);
  }

  @Test
  void existsByAlias_whenAliasExists_returnTrue() {
    UrlPair urlPairGift = new UrlPair("gift", "https://example.gift.com");
    urlPairRepository.save(urlPairGift);
    UrlPair urlPairHide = new UrlPair("hide", "https://example.hide.com");
    urlPairRepository.save(urlPairHide);

    boolean result = urlPairRepository.existsByAlias("gift");

    assertTrue(result);
  }

  @Test
  void deleteByAlias_whenAliasDoesNotExist_will() {
    UrlPair urlPairHide = new UrlPair("hide", "https://example.hide.com");
    urlPairRepository.save(urlPairHide);
    long initialCount = urlPairRepository.count();

    urlPairRepository.deleteByAlias("gift");

    assertEquals(initialCount, urlPairRepository.count());
    assertTrue(urlPairRepository.existsByAlias("hide"));
    assertTrue(urlPairRepository.findByAlias("hide").isPresent());
  }

  @Test
  void deleteByAlias_whenAliasExists_deleteSucessfully() {
    UrlPair urlPairHide = new UrlPair("hide", "https://example.hide.com");
    urlPairRepository.save(urlPairHide);
    UrlPair urlPairGift = new UrlPair("gift", "https://example.gift.com");
    urlPairRepository.save(urlPairGift);
    long initialCount = urlPairRepository.count();

    urlPairRepository.deleteByAlias("gift");

    assertEquals(initialCount - 1, urlPairRepository.count());
    assertTrue(urlPairRepository.existsByAlias("hide"));
    assertTrue(urlPairRepository.findByAlias("hide").isPresent());
    assertFalse(urlPairRepository.existsByAlias("gift"));
    assertFalse(urlPairRepository.findByAlias("gift").isPresent());
  }
}
