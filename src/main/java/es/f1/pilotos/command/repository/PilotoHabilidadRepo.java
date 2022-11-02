package es.f1.pilotos.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface PilotoHabilidadRepo  extends JpaRepository<PilotoHabilidad, Integer> {
    Optional<PilotoHabilidad> findFirstByIdPilotoAndAndIdHabilidadOrderByFechaCreacionDesc(int idPiloto, int idHabilidad);
    Collection<PilotoHabilidad> findByIdPilotoOrderByIdHabilidad(int idPiloto);
}
