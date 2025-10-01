package com.urlshortener.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortener.exception.AliasAlreadyExistsException;
import com.urlshortener.exception.AliasNotFoundException;
import com.urlshortener.model.UrlPair;
import com.urlshortener.model.dto.ShortenUrlRequest;
import com.urlshortener.model.dto.ShortenUrlResponse;
import com.urlshortener.model.dto.UrlListResponse;
import com.urlshortener.service.UrlPairService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UrlShortenerController.class)
class UrlShortenerControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private UrlPairService urlPairService;

  @Test
  void shortenUrl_ValidRequest_ShouldReturnCreated() throws Exception {
    ShortenUrlRequest request = new ShortenUrlRequest("https://example.com", "custom");
    ShortenUrlResponse response = new ShortenUrlResponse("http://localhost:8080/custom");

    when(urlPairService.shortenUrl(any(ShortenUrlRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.shortUrl").value("http://localhost:8080/custom"));
  }

  @Test
  void shortenUrl_InvalidUrl_ShouldReturnBadRequest() throws Exception {
    ShortenUrlRequest request = new ShortenUrlRequest("invalid-url", null);

    mockMvc
        .perform(
            post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shortenUrl_AliasAlreadyExists_ShouldReturnBadRequest() throws Exception {
    ShortenUrlRequest request = new ShortenUrlRequest("https://example.com", "existing");

    when(urlPairService.shortenUrl(any(ShortenUrlRequest.class)))
        .thenThrow(new AliasAlreadyExistsException("existing"));

    mockMvc
        .perform(
            post("/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Alias 'existing' already exists"));
  }

  @Test
  void redirectToUrl_ExistingAlias_ShouldRedirect() throws Exception {
    String alias = "test";
    UrlPair urlPair = new UrlPair(alias, "https://example.com");

    when(urlPairService.getUrlPairByAlias(alias)).thenReturn(urlPair);

    mockMvc
        .perform(get("/" + alias))
        .andExpect(status().isFound())
        .andExpect(header().string("Location", "https://example.com"));
  }

  @Test
  void redirectToUrl_NonExistingAlias_ShouldReturnNotFound() throws Exception {
    String alias = "nonexistent";

    when(urlPairService.getUrlPairByAlias(alias)).thenThrow(new AliasNotFoundException(alias));

    mockMvc.perform(get("/" + alias)).andExpect(status().isNotFound());
  }

  @Test
  void deleteUrl_ExistingAlias_ShouldReturnNoContent() throws Exception {
    String alias = "test";

    mockMvc.perform(delete("/" + alias)).andExpect(status().isNoContent());

    verify(urlPairService).deleteUrlPair(alias);
  }

  @Test
  void deleteUrl_NonExistingAlias_ShouldReturnNotFound() throws Exception {
    String alias = "nonexistent";

    doThrow(new AliasNotFoundException(alias)).when(urlPairService).deleteUrlPair(alias);

    mockMvc.perform(delete("/" + alias)).andExpect(status().isNotFound());
  }

  @Test
  void listUrls_ShouldReturnListOfUrls() throws Exception {
    List<UrlListResponse> urls =
        List.of(
            new UrlListResponse("alias1", "https://example1.com", "http://localhost:8080/alias1"),
            new UrlListResponse("alias2", "https://example2.com", "http://localhost:8080/alias2"));

    when(urlPairService.getAllUrls()).thenReturn(urls);

    mockMvc
        .perform(get("/urls"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].alias").value("alias1"))
        .andExpect(jsonPath("$[0].fullUrl").value("https://example1.com"))
        .andExpect(jsonPath("$[0].shortUrl").value("http://localhost:8080/alias1"))
        .andExpect(jsonPath("$[1].alias").value("alias2"));
  }
}
