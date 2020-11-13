package com.shinstealer.spring.webflux.react_app.controller;

import java.net.URI;
import com.shinstealer.spring.webflux.react_app.document.Profile;
import com.shinstealer.spring.webflux.react_app.service.ProfileService;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping(value = "/profiles")
@org.springframework.context.annotation.Profile("classic")
public class ProfileRestController {

  private ProfileService profileService;

  // private MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;

  public ProfileRestController(ProfileService profileService) {
    this.profileService = profileService;
  }

  @GetMapping(value = "/")
  Publisher<Profile> getAll() {
    log.info("get all command");
    return this.profileService.all();
    
  }

  @GetMapping("/{id}")
  Publisher<Profile> getById(@PathVariable("id") String id) {
    return this.profileService.getById(id);
  }

  @PostMapping
  Publisher<ResponseEntity<Profile>> create(@RequestBody Profile profile) {
    return this.profileService.create(profile.getEmail())
        .map(p -> ResponseEntity.created(URI.create("/profiles/" + p.getId())).contentType(MediaType.APPLICATION_JSON).build());
  }

  @DeleteMapping("/{id}")
  Publisher<Profile> deleteById(@PathVariable String id) {
    return this.profileService.deleteById(id);
  }

  @PutMapping("/{id}")
  Publisher<ResponseEntity<Profile>> updateById(@PathVariable String id,
      @RequestBody Profile profile) {
    return Mono.just(profile).flatMap(p -> this.profileService.updateById(id, p.getEmail()))
        .map(p -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).build());
  }
}
