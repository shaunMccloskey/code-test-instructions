package com.urlshortener.repository;

import com.urlshortener.model.UrlPair;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlPairRepository extends MongoRepository<UrlPair, String> {

  Optional<UrlPair> findByAlias(String alias);

  boolean existsByAlias(String alias);

  void deleteByAlias(String alias);
}
