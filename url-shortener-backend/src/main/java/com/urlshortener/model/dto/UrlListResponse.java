package com.urlshortener.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlListResponse {
  private String alias;
  private String fullUrl;
  private String shortUrl;
}
