package com.f1.pilots.sync.service;

import com.f1.pilots.command.repository.PilotCommandEntity;
import com.f1.pilots.command.repository.PilotSkillCommandEntity;
import com.f1.pilots.command.service.PilotoCommandService;
import com.f1.pilots.query.service.PilotoQueryService;
import com.f1.pilots.sync.repository.Sincronizacion;
import com.f1.pilots.sync.repository.SincronizacionRepo;
import com.f1.pilots.sync.repository.TipoSincronizacion;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class PilotoSyncService {

    @Autowired
    PilotoCommandService pilotoCommandService;

    @Autowired
    PilotoQueryService pilotoQueryServiceQuery;

    @Autowired
    SincronizacionRepo sincronizacionRepo;

    public void syncPilotos(){
        log.info("Inicio del proceso de sincronización");

        Timestamp ultimaSincronizacion = recuperarTSUltimaSincronizacion();

        List<PilotCommandEntity> pilotosH2 = recuperarPilotosH2(ultimaSincronizacion);
        for(PilotCommandEntity pH2: pilotosH2){
            grabarPilotoMongoDB(pH2);
            sincronizacionRepo.save(Sincronizacion.builder()
                            .tipoSincronizacion(TipoSincronizacion.tipoSincronizacion.PILOTOS)
                            .fechaUltimaSincronizacion(pH2.getCreationDate())
                            .build());
        }
    }

    private Timestamp recuperarTSUltimaSincronizacion(){
        log.info("Se recupera la fecha de la última sincronización");
        Optional<Sincronizacion> ultimaSincronizacion = sincronizacionRepo.findByTipoSincronizacion(TipoSincronizacion.tipoSincronizacion.PILOTOS);
        if (ultimaSincronizacion.isPresent()){
            log.info("La fecha recuperada es: " + ultimaSincronizacion.get().getFechaUltimaSincronizacion());
            return ultimaSincronizacion.get().getFechaUltimaSincronizacion();
        } else {
            log.info("No ha habido todavía ninguna sincronización");
            return Timestamp.from(Instant.now().plusSeconds(-999999999));
        }
    }

    private List<PilotCommandEntity> recuperarPilotosH2(Timestamp ultimaSincronizacion){
        log.info("Se recuperan registro del H2 que no se hayan sincronizado. Fecha recibida de última sincronización: " + ultimaSincronizacion);
        List<PilotCommandEntity> pilotosH2 = pilotoCommandService.recuperarPilotosASincronizar(ultimaSincronizacion);
        log.info("Número de registros de pilotos recuperados: " + pilotosH2.size());
        return pilotosH2;
    }

    private void grabarPilotoMongoDB(PilotCommandEntity pH2){
        log.info("Se graba la info en MongoDB con lo recibido");
        if (!pH2.isAvailable()) {
            pilotoQueryServiceQuery.eliminarPiloto(pH2.getCodeA3());
        } else {
            com.f1.pilots.query.repository.Piloto pMongoDB = mapearPilotoDeH2AMongoDB(pH2);
            pilotoQueryServiceQuery.grabarPiloto(pMongoDB);
        }
    }

    private com.f1.pilots.query.repository.Piloto mapearPilotoDeH2AMongoDB(PilotCommandEntity pH2){
        log.info("Se mapea el piloto de H2 a MongoDB");
        return com.f1.pilots.query.repository.Piloto.builder()
                            .codigo(pH2.getCodeA3())
                            .nombre(pH2.getName())
                            .fecha_nacimiento(pH2.getBirthDate())
                            .habilidades(mapearHabilidadesDeH2AMongoDB(pilotoCommandService.recuperarHabilidadesPilotoDesdeCodigoPiloto(pH2.getCodeA3())))
                            .build();
    }

    private HashMap<String, Integer> mapearHabilidadesDeH2AMongoDB(List<PilotSkillCommandEntity> habilidadtesPilotoH2){
        HashMap<String, Integer> habilidadtesPilotosMongoDB = new HashMap<String, Integer>();
        for(PilotSkillCommandEntity habilidadPilotoH2: habilidadtesPilotoH2){
            if (habilidadPilotoH2.isANewSkill()){
                habilidadtesPilotosMongoDB.put(habilidadPilotoH2.getSkillCommandEntity().getCode(), habilidadPilotoH2.getValue());
            } else if (habilidadPilotoH2.isAnExistingSkill()) {
                habilidadtesPilotosMongoDB.replace(habilidadPilotoH2.getSkillCommandEntity().getCode(), habilidadtesPilotosMongoDB.get(habilidadPilotoH2.getSkillCommandEntity().getCode()) + habilidadPilotoH2.getValue());
            }
        }
        return habilidadtesPilotosMongoDB;
    }
}
