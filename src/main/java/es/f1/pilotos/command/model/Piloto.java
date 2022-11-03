package es.f1.pilotos.command.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class Piloto {
    @JsonIgnore
    private String codigo;
    private String nombre;
    private Date fechaNacimiento;
}
