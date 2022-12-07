package com.f1.pilots.rabbitMQ.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;

@Builder
@AllArgsConstructor @NoArgsConstructor
public class PilotoRabbit {
    public Movement.type type;
    public String code;
    public String name;
    public Date date_of_birth;
    public HashMap<String, Integer> skills;
}
