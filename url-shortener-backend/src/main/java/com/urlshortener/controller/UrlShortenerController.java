package com.urlshortener.controller;

import com.urlshortener.model.UrlPair;
import com.urlshortener.model.dto.ShortenUrlRequest;
import com.urlshortener.model.dto.ShortenUrlResponse;
import com.urlshortener.model.dto.UrlListResponse;
import com.urlshortener.service.UrlPairService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "${cors.allowed-origins}")
@RequiredArgsConstructor
public class UrlShortenerController {

  private final UrlPairService urlPairService;

  @PostMapping("/shorten")
  public ResponseEntity<ShortenUrlResponse> shortenUrl(
      @Valid @RequestBody ShortenUrlRequest request) {
    ShortenUrlResponse response = urlPairService.shortenUrl(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{alias}")
  public ResponseEntity<Void> redirectToUrl(@PathVariable String alias) {
    UrlPair urlPair = urlPairService.getUrlPairByAlias(alias);
    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlPair.getUrl())).build();
  }

  @DeleteMapping("/{alias}")
  public ResponseEntity<Void> deleteUrl(@PathVariable String alias) {
    urlPairService.deleteUrlPair(alias);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/urls")
  public ResponseEntity<List<UrlListResponse>> listUrls() {
    List<UrlListResponse> urls = urlPairService.getAllUrls();
    return ResponseEntity.ok(urls);
  }
}
