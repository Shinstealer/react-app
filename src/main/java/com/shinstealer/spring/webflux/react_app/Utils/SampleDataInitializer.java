package com.shinstealer.spring.webflux.react_app.Utils;

import java.util.UUID;
import com.shinstealer.spring.webflux.react_app.repository.ProfileRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@Slf4j
@Profile("demo")
public class SampleDataInitializer implements ApplicationListener<ApplicationReadyEvent> {

  private final ProfileRepository repository;

  public SampleDataInitializer(ProfileRepository repository) {
    this.repository = repository;
  }

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    repository
        .deleteAll()
        .thenMany(Flux.just("A", "B", "C", "D")
            .map(name -> new com.shinstealer.spring.webflux.react_app.document.Profile(
                UUID.randomUUID().toString(), name + "@email.com"))
            .flatMap(repository::save)
            
            )

        .thenMany(repository.findAll())
        .subscribe(profile -> log.info("saving " + profile.toString()));

  }

}
