package com.f1.pilots.command.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "PILOTO")
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class PilotCommandEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="SQ_PILOTO")
    private int id;
    @Column(name="TIPO_REGISTRO")
    @Enumerated(EnumType.STRING)
    private Movement.type type;
    @Column(name="FECHA_CREACION_REGISTRO")
    private Timestamp creationDate;
    @Column(name="CODIGO_A3")
    private String codeA3;
    @Column(name="NOMBRE")
    private String name;
    @Column(name="FECHA_NACIMIENTO")
    private Date birthDate;
//    @OneToMany()
//    @JoinColumn(name="CODIGO_A3_PILOTO", nullable=false, insertable = false, updatable = false)
//    List<PilotoHabilidad> habilidadesPiloto;

    public boolean isAvailable() {
        if (this.type.equals(Movement.type.BORRADO_PILOTO)) {
            return false;
        } else {
            return true;
        }
    }
}
