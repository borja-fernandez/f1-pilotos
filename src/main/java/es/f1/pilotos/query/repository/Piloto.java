package es.f1.pilotos.query.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Document("piloto")
public class Piloto {
    @Id
    public String codigo;
    public String nombre;
    public Date fecha_nacimiento;
    public HashMap<String, Integer> habilidades;
}
