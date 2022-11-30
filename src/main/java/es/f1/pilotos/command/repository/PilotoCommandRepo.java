package es.f1.pilotos.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface PilotoCommandRepo extends JpaRepository<Piloto, Integer> {
    Optional<Piloto> findFirstByCodigoOrderByFechaCreacionDesc(String codigo);
    List<Piloto> findAllByFechaCreacionAfterOrderById(Timestamp fecha);
}
