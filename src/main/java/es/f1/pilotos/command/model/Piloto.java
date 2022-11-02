package es.f1.pilotos.command.model;

import com.sun.istack.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.Date;
import java.util.HashMap;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class Piloto {
    @NotNull
    private String codigo;
    @NotNull
    private String nombre;
    private Date fechaNacimiento;
    private HashMap<String, Integer> habilidades;

//    public static PilotoBuilder builder() {return new PilotoBuilder();}
}
