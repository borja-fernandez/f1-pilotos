package es.f1.pilotos.command.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.apache.tomcat.jni.Time.now;

@Entity
@Table(name = "PILOTO")
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Piloto {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="SQ_PILOTO")
    private int id;
    @Column(name="CODIGO_A3")
    private String codigo;
    @Column(name="NOMBRE")
    private String nombre;
    @Column(name="FECHA_NACIMIENTO")
    private Date fechaNacimiento;

}
