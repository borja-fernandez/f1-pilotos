package es.f1.pilotos.query.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

//@Repository
public interface PilotoQueryRepo extends MongoRepository<Piloto, String> {
}
