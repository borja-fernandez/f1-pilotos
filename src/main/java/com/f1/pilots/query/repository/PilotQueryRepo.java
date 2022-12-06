package com.f1.pilots.query.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

//@Repository
public interface PilotQueryRepo extends MongoRepository<PilotQueryDocument, String> {
    Optional<PilotQueryDocument> findByCode(String code);
}
