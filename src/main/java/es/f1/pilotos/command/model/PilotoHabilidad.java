package es.f1.pilotos.command.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PilotoHabilidad {
    @JsonIgnore
    private String codigoPiloto;
    @JsonIgnore
    private String codigoHabilidad;
    private int valor;
}
