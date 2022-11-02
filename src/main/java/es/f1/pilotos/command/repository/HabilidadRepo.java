package es.f1.pilotos.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HabilidadRepo extends JpaRepository<Habilidad, Integer> {
    Optional<Habilidad> findByCodigo(String codigo);
}
