package com.f1.pilots.command.repository;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "HABILIDAD")
@Getter
public class SkillCommandEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SQ_HABILIDAD")
    private int id;
    private String code;
    private String description;
}
