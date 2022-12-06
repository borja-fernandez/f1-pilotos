package com.f1.pilots.query.repository;

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
public class PilotQueryDocument {
    @Id
    public String code;
    public String name;
    public Date date_of_birth;
    public HashMap<String, Integer> skillList;
}
