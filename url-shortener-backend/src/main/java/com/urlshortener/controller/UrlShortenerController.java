package com.urlshortener.controller;

import com.urlshortener.model.UrlPair;
import com.urlshortener.model.dto.ShortenUrlRequest;
import com.urlshortener.model.dto.ShortenUrlResponse;
import com.urlshortener.model.dto.UrlListResponse;
import com.urlshortener.service.UrlPairService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "URL Shortener", description = "API for managing shortened URLs")
public class UrlShortenerController {

  private final UrlPairService urlPairService;

  @PostMapping("/shorten")
  @Operation(
      summary = "Shorten a URL",
      description = "Create a shortened URL with optional custom alias")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "URL successfully shortened"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "409", description = "Custom alias already exists")
      })
  public ResponseEntity<ShortenUrlResponse> shortenUrl(
      @Valid @RequestBody ShortenUrlRequest request) {
    ShortenUrlResponse response = urlPairService.shortenUrl(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/{alias}")
  @Operation(
      summary = "Redirect to original URL",
      description = "Redirect to the original URL using the shortened alias")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "302", description = "Redirect to original URL"),
        @ApiResponse(responseCode = "404", description = "Alias not found")
      })
  public ResponseEntity<Void> redirectToUrl(
      @Parameter(description = "Shortened URL alias") @PathVariable String alias) {
    UrlPair urlPair = urlPairService.getUrlPairByAlias(alias);
    return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(urlPair.getUrl())).build();
  }

  @DeleteMapping("/{alias}")
  @Operation(
      summary = "Delete a shortened URL",
      description = "Delete a shortened URL by its alias")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204", description = "URL successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Alias not found")
      })
  public ResponseEntity<Void> deleteUrl(
      @Parameter(description = "Shortened URL alias") @PathVariable String alias) {
    urlPairService.deleteUrlPair(alias);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/urls")
  @Operation(summary = "List all shortened URLs", description = "Get a list of all shortened URLs")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of URLs")
      })
  public ResponseEntity<List<UrlListResponse>> listUrls() {
    List<UrlListResponse> urls = urlPairService.getAllUrls();
    return ResponseEntity.ok(urls);
  }
}
