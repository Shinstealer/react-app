package com.shinstealer.spring.webflux.react_app.config;


import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import java.net.URI;
import com.shinstealer.spring.webflux.react_app.document.Profile;
import com.shinstealer.spring.webflux.react_app.service.ProfileService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Configuration
public class ProfileEndPointConfig {

  @Bean
  public RouterFunction<ServerResponse> routes(ProfileHandler handler) {
    return route(requestPredicate(GET("/profiles")), handler::all)
    .andRoute(requestPredicate(GET("/profiles/{id}")), handler::getById)
    .andRoute(requestPredicate(DELETE("/profiles/{id}")), handler::deleteById)
    .andRoute(requestPredicate(POST("/profiles")), handler::create)
    .andRoute(requestPredicate(PUT("/profiles/{id}")), handler::updateById);
    
  }

  private static RequestPredicate requestPredicate(RequestPredicate target) {
    return new CaseInsensitiveRequestPredicate(target);
  }


  /**
   * InnerProfileEndPointConfig
   */
  @Component
  public class ProfileHandler {

    private ProfileService profileService;

    public ProfileHandler(ProfileService profileService) {
      this.profileService = profileService;
    }

    Mono<ServerResponse> all(ServerRequest r) {
      return defaultReadResponse(this.profileService.all());
    }

    Mono<ServerResponse> getById(ServerRequest r) {
      return defaultReadResponse(this.profileService.getById(id(r)));
    }

    Mono<ServerResponse> deleteById(ServerRequest r) {
      return defaultReadResponse(this.profileService.deleteById(id(r)));
    }

    Mono<ServerResponse> updateById(ServerRequest r) {
      Flux<Profile> id = r.bodyToFlux(Profile.class)
          .flatMap(p -> this.profileService.updateById(id(r), p.getEmail()));
      return defaultReadResponse(id);
    }

    Mono<ServerResponse> create(ServerRequest r) {
      Flux<Profile> flux =
          r.bodyToFlux(Profile.class).flatMap(p -> this.profileService.create(p.getEmail()));
      return defaultWriteResponse(flux);
    }

    private Mono<ServerResponse> defaultReadResponse(Publisher<Profile> profiles) {
      return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(profiles,
          Profile.class);
    }

    private Mono<ServerResponse> defaultWriteResponse(Publisher<Profile> profiles) {
      return Mono.from(profiles)
          .flatMap(p -> ServerResponse.created(URI.create("/profiles/" + p.getId()))
              .contentType(MediaType.APPLICATION_JSON).build());
    }

    private String id(ServerRequest r) {
      return r.pathVariable("id");
    }

  }

}
