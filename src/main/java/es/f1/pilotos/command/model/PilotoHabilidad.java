package es.f1.pilotos.command.model;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PilotoHabilidad {
    private String codigoPiloto;
    private String codigoHabilidad;
    private int valor;
}
