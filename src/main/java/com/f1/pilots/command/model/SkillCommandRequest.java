package com.f1.pilots.command.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SkillCommandRequest {
    @JsonIgnore
    private String pilotCodeA3;
    @JsonIgnore
    private String skillCode;
    private int value;
}
