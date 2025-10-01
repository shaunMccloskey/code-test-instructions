package com.urlshortener.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortenUrlRequest {

  @NotBlank(message = "Full URL is required")
  @URL(message = "Invalid URL format")
  private String fullUrl;

  @Pattern(
      regexp = "^[a-zA-Z0-9_-]*$",
      message = "Custom alias can only contain letters, numbers, hyphens, and underscores")
  private String customAlias;
}
