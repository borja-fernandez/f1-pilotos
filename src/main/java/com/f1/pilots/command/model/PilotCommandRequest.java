package com.f1.pilots.command.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Builder
public class PilotCommandRequest {
    @JsonIgnore
    private String codeA3;
    private String name;
    private Date dateOfBirth;
}
