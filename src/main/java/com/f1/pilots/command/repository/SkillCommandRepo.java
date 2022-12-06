package com.f1.pilots.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillCommandRepo extends JpaRepository<SkillCommandEntity, Integer> {
    Optional<SkillCommandEntity> findByCode(String code);
}
