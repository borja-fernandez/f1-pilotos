package com.f1.pilots.sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SynchroRepo extends JpaRepository<SynchroEntity, Synchronization.type> {
    Optional<SynchroEntity> findByType(Synchronization.type codigo);
}
