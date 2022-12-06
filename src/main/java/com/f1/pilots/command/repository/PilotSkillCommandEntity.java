package com.f1.pilots.command.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "HABILIDAD_PILOTO")
@NoArgsConstructor @AllArgsConstructor
@Builder
@Getter
public class PilotSkillCommandEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="SQ_HABILIDAD_PILOTO")
    private int id;
    @Column(name="TIPO_REGISTRO")
    @Enumerated(EnumType.STRING)
    private Movement.type type;
    @Column(name="FECHA_CREACION_REGISTRO")
    private Timestamp creationDate;
    @Column(name="CODIGO_A3_PILOTO")
    private String pilotCodeA3;
    @Column(name="SQ_HABILIDAD")
    private int skillId;
    @Column(name="CANTIDAD")
    private int value;
    @ManyToOne
    @JoinColumn(name="SQ_HABILIDAD", nullable=false, insertable = false, updatable = false)
    private SkillCommandEntity skillCommandEntity;

    public boolean isANewSkill() {
        return this.type.equals(Movement.type.ALTA_HABILIDAD);
    }

    public boolean isAnExistingSkill() {
        return this.type.equals(Movement.type.MODIFICACION_HABILIDAD);
    }
}
