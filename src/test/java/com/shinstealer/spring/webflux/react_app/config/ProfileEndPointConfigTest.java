package com.shinstealer.spring.webflux.react_app.config;

import com.shinstealer.spring.webflux.react_app.config.ProfileEndPointConfig.ProfileHandler;
import com.shinstealer.spring.webflux.react_app.document.Profile;
import com.shinstealer.spring.webflux.react_app.repository.ProfileRepository;
import com.shinstealer.spring.webflux.react_app.service.ProfileService;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;


@WebFluxTest

public abstract class ProfileEndPointConfigTest {

  @Mock
  private ProfileRepository repository;

  @Autowired
  private WebTestClient client;

  @BeforeEach
  public void init() {
    MockitoAnnotations.initMocks(this);
  }


  @Test
  public void getAll() {

    Mockito.when(repository.findAll())
        .thenReturn(Flux.just(new Profile("id-1", "test1"), new Profile("id-2", "test2")));

    this.client.get().uri("/profiles").accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
        .isOk().expectHeader().contentType(MediaType.APPLICATION_JSON).expectBody()
        .jsonPath("$.[0].id").isEqualTo("id-1").jsonPath("$.[0].email").isEqualTo("test1")
        .jsonPath("$.[1].id").isEqualTo("id-2").jsonPath("$.[1].email").isEqualTo("test2");
  }

}
