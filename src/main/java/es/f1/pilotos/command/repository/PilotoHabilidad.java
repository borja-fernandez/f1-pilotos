package es.f1.pilotos.command.repository;

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
public class PilotoHabilidad {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="SQ_HABILIDAD_PILOTO")
    private int id;
    @Column(name="TIPO_REGISTRO")
    @Enumerated(EnumType.STRING)
    private TipoRegistro.tipoRegistro tipoRegistro;
    @Column(name="FECHA_CREACION_REGISTRO")
    private Timestamp fechaCreacion;
    @Column(name="SQ_PILOTO")
    private int idPiloto;
    @Column(name="SQ_HABILIDAD")
    private int idHabilidad;
    @Column(name="CANTIDAD")
    private int valor;
    @ManyToOne
    @JoinColumn(name="SQ_HABILIDAD", nullable=false, insertable = false, updatable = false)
    private Habilidad habilidad;

    public boolean estaEnVigor() {
        if (this.tipoRegistro.equals(TipoRegistro.tipoRegistro.BORRADO)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean esAlta() {
        return this.tipoRegistro.equals(TipoRegistro.tipoRegistro.ALTA);
    }

    public boolean esModification() {
        return this.tipoRegistro.equals(TipoRegistro.tipoRegistro.MODIFICACION);
    }
}
