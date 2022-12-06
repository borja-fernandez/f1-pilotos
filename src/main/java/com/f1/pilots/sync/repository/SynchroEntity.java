package com.f1.pilots.sync.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SINCRONIZACION")
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class SynchroEntity {
    @Id
    @Column(name="TIPO_SINCRONIZACION")
    @Enumerated(EnumType.STRING)
    private Synchronization.type type;

    @Column(name="FECHA_ULTIMA_SINCRONIZACION")
    private Timestamp lastSynchroDate;
}
