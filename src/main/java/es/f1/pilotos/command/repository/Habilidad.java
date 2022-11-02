package es.f1.pilotos.command.repository;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "HABILIDAD")
@Getter
public class Habilidad {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="SQ_HABILIDAD")
    private int id;
    private String codigo;
    private String descripcion;
}
