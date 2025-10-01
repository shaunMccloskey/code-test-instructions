package com.urlshortener.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor // Default constructor needed for Spring Data/Reflection
@AllArgsConstructor
@Document(collection = "url_pair")
public class UrlPair {

  @Id private String id;

  @Indexed(unique = true)
  private String alias;

  private String url;

  public UrlPair(String alias, String url) {
    this.alias = alias;
    this.url = url;
  }
}
