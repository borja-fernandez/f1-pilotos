package es.f1.pilotos.sync.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SINCRONIZACION")
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Sincronizacion {
    @Id
    @Column(name="TIPO_SINCRONIZACION")
    @Enumerated(EnumType.STRING)
    private TipoSincronizacion.tipoSincronizacion tipoSincronizacion;

    @Column(name="FECHA_ULTIMA_SINCRONIZACION")
    private Timestamp fechaUltimaSincronizacion;
}
