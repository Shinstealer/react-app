package com.shinstealer.spring.webflux.react_app.service;

import java.util.UUID;
import java.util.function.Predicate;
import com.shinstealer.spring.webflux.react_app.document.Profile;
import com.shinstealer.spring.webflux.react_app.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@Import(ProfileService.class)
public class ProfileServiceTest {

  @Autowired
  private ProfileService profileService;
  @Autowired
  private ProfileRepository repository;


  @Test
  public void getAll() {
    Flux<Profile> saved = repository.saveAll(
        Flux.just(new Profile(null, "Josh"), new Profile(null, "Matt"), new Profile(null, "Jane")));
    Flux<Profile> composite = profileService.all().thenMany(saved);

    Predicate<Profile> match = profile -> saved.any(saveItem -> saveItem.equals(profile)).block();

    StepVerifier.create(composite).expectNextMatches(match).expectNextMatches(match)
        .expectNextMatches(match).verifyComplete();
  }

  @Test
  public void save() {
    Mono<Profile> profileMono = this.profileService.create("example");

    StepVerifier.create(profileMono).expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
        .verifyComplete();
  }

  @Test
  public void delete() {
    String test = "test";

    Mono<Profile> deleted = this.profileService.create(test).flatMap(saved -> this.profileService.deleteById(saved.getId()));

    StepVerifier
    .create(deleted)
    .expectNextMatches(p -> p.getEmail().equalsIgnoreCase("test"))
    .verifyComplete();
  }

  @Test
  public void update() {
    String test = "test";

    Mono<Profile> saved = this.profileService
    .create(test).flatMap(p -> this.profileService.updateById(p.getId(), "test-updated"));

    StepVerifier
    .create(saved)
    .expectNextMatches(p -> p.getEmail().equalsIgnoreCase("test-updated"))
    .verifyComplete();
  }

  @Test
  public void getById() {
    String test = UUID.randomUUID().toString();

    Mono<Profile> deleted = this.profileService
    .create(test)
    .flatMap(saved -> this.profileService.getById(saved.getId()));

    StepVerifier
    .create(deleted)
    .expectNextMatches(p -> StringUtils.hasText(p.getId()) && test.equalsIgnoreCase(p.getEmail()))
    .verifyComplete();
  }

  
}
