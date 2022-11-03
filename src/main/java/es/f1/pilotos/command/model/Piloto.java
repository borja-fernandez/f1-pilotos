package es.f1.pilotos.command.model;

import com.sun.istack.NotNull;
import lombok.*;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class Piloto {
    @NotNull
    private String codigo;
    @NotNull
    private String nombre;
    private Date fechaNacimiento;
}
