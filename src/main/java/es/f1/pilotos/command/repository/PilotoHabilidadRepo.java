package es.f1.pilotos.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PilotoHabilidadRepo  extends JpaRepository<PilotoHabilidad, Integer> {
    Optional<PilotoHabilidad> findFirstByCodigoPilotoAndAndIdHabilidadOrderByFechaCreacionDesc(String codigoPiloto, int idHabilidad);
    List<PilotoHabilidad> findByCodigoPilotoOrderByIdHabilidad(String codigoPiloto);
}
