package es.f1.pilotos.query.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

//@Repository
public interface PilotoQueryRepo extends MongoRepository<Piloto, String> {
    Optional<Piloto> findByCodigo(String codigo);
}
