package es.f1.pilotos.command.repository;

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
public class Piloto {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="SQ_PILOTO")
    private int id;
    @Column(name="TIPO_REGISTRO")
    @Enumerated(EnumType.STRING)
    private TipoRegistro.tipoRegistro tipoRegistro;
    @Column(name="FECHA_CREACION_REGISTRO")
    private Timestamp fechaCreacion = new Timestamp(System.currentTimeMillis());
    @Column(name="CODIGO_A3")
    private String codigo;
    @Column(name="NOMBRE")
    private String nombre;
    @Column(name="FECHA_NACIMIENTO")
    private Date fechaNacimiento;

    public boolean estaEnVigor() {
        if (this.tipoRegistro.equals(TipoRegistro.tipoRegistro.BORRADO)) {
            return false;
        } else {
            return true;
        }
    }
}