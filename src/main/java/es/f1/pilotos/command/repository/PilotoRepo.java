package es.f1.pilotos.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PilotoRepo extends JpaRepository<Piloto, Integer> {
    Optional<Piloto> findByCodigo(String codigo);
}
