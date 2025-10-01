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
public class UrlPairRepositoryTest {

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
}
