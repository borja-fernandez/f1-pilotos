package es.f1.pilotos.command.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PilotoHabilidad {
    private String codigoPiloto;
    private String codigoHabilidad;
    private int valor;
}
