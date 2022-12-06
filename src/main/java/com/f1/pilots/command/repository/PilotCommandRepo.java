package com.f1.pilots.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface PilotCommandRepo extends JpaRepository<PilotCommandEntity, Integer> {
    Optional<PilotCommandEntity> findFirstByCodeA3OrderByCreationDateDesc(String codeA3);
    List<PilotCommandEntity> findAllByCreationDateAfterOrderById(Timestamp date);
}
