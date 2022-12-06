package com.f1.pilots.query.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;

@AllArgsConstructor @NoArgsConstructor
@Builder
public class PilotoResponse {
    public String codigo;
    public String nombre;
    public Date fecha_nacimiento;
    public HashMap<String, Integer> habilidades;
}
