package com.f1.pilots.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PilotSkillCommandRepo extends JpaRepository<PilotSkillCommandEntity, Integer> {
    Optional<PilotSkillCommandEntity> findFirstByPilotCodeA3AndSkillIdOrderByCreationDateDesc(String codigoPiloto, int idHabilidad);
    List<PilotSkillCommandEntity> findByPilotCodeA3OrderBySkillId(String codigoPiloto);
}
