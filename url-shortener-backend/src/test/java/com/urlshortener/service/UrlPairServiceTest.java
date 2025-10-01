package com.urlshortener.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.urlshortener.repository.UrlPairRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UrlPairServiceTest {

  @Mock private UrlPairRepository urlPairRepository;

  private UrlPairService urlPairService;

  @BeforeEach
  void setUp() {}
}
