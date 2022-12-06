package com.f1.pilots.query.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;

@AllArgsConstructor @NoArgsConstructor
@Builder
public class PilotQueryResponse {
    public String code;
    public String name;
    public Date date_of_birth;
    public HashMap<String, Integer> skills;
}
