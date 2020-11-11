package com.shinstealer.spring.webflux.react_app.service;

import com.shinstealer.spring.webflux.react_app.document.Profile;
import com.shinstealer.spring.webflux.react_app.repository.ProfileRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ProfileService {

  private final ApplicationEventPublisher publisher;

  private final ProfileRepository repository;

  public ProfileService(ApplicationEventPublisher publisher, ProfileRepository repository) {
    this.publisher = publisher;
    this.repository = repository;
  }

  public Flux<Profile> all() {
    log.info("search all data");
    return this.repository.findAll();
  }

  public Mono<Profile> getById(String id) {
    log.info("search data by id : " + id);
    return this.repository.findById(id);
  }

  public Mono<Profile> updateById(String id, String email) {
    log.info("update info of email " + email + "data by id : " + id);
    return this.repository.findById(id).map(profile -> new Profile(profile.getId(), email))
        .flatMap(this.repository::save);
  }

  public Mono<Profile> deleteById(String id) {
    log.info("delete by id : " + id);
    return this.repository.findById(id)
        .flatMap(profile -> repository.deleteById(profile.getId()).thenReturn(profile));

  }

  public Mono<Void> deleteAll() {
    log.info("delete all data");
    return this.repository.deleteAll();

  }

  public Mono<Profile> create(String email) {
    log.info("create data : " + email);
    return this.repository.save(new Profile(null, email))
        .doOnSuccess(profile -> this.publisher.publishEvent(profile));
  }

}
