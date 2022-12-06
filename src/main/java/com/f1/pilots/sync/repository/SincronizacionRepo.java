package com.f1.pilots.sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SincronizacionRepo extends JpaRepository<Sincronizacion, TipoSincronizacion.tipoSincronizacion> {
    Optional<Sincronizacion> findByTipoSincronizacion(TipoSincronizacion.tipoSincronizacion codigo);
}
