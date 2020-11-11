package com.shinstealer.spring.webflux.react_app.repository;

import com.shinstealer.spring.webflux.react_app.document.Profile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {

}
