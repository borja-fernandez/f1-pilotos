package com.f1.pilots.sync.service;

import com.f1.pilots.command.repository.PilotCommandEntity;
import com.f1.pilots.command.repository.PilotSkillCommandEntity;
import com.f1.pilots.command.service.PilotCommandService;
import com.f1.pilots.query.repository.PilotQueryDocument;
import com.f1.pilots.query.service.PilotQueryService;
import com.f1.pilots.rabbitMQ.RabbitMQSender;
import com.f1.pilots.rabbitMQ.model.Movement;
import com.f1.pilots.rabbitMQ.model.PilotoRabbit;
import com.f1.pilots.sync.repository.SynchroEntity;
import com.f1.pilots.sync.repository.SynchroRepo;
import com.f1.pilots.sync.repository.Synchronization;
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
public class PilotSyncService {

    @Autowired
    PilotCommandService pilotCommandService;

    @Autowired
    PilotQueryService pilotQueryService;

    @Autowired
    SynchroRepo synchroRepo;

    @Autowired
    RabbitMQSender rabbitMQSender;

    public void syncPilotos(){
        log.info("Inicio del proceso de sincronización");

        Timestamp ultimaSincronizacion = recuperarTSUltimaSincronizacion();

        List<PilotCommandEntity> pilotosH2 = recuperarPilotosH2(ultimaSincronizacion);
        for(PilotCommandEntity pH2: pilotosH2){
            publishPilot(grabarPilotoMongoDB(pH2), pH2.isAvailable());
            synchroRepo.save(SynchroEntity.builder()
                            .type(Synchronization.type.PILOTOS)
                            .lastSynchroDate(pH2.getCreationDate())
                            .build());
        }
    }
    private void publishPilot(PilotQueryDocument pilotQueryDocument, boolean isAvailable){
        rabbitMQSender.send(PilotoRabbit.builder()
                        .code(pilotQueryDocument.code)
                        .date_of_birth(pilotQueryDocument.date_of_birth)
                        .name(pilotQueryDocument.name)
                        .skills(pilotQueryDocument.skillList)
                        .type(isAvailable? Movement.type.MODIFICACION:Movement.type.BORRADO)
                .build());
    }

    private Timestamp recuperarTSUltimaSincronizacion(){
        log.info("Se recupera la fecha de la última sincronización");
        Optional<SynchroEntity> ultimaSincronizacion = synchroRepo.findByType(Synchronization.type.PILOTOS);
        if (ultimaSincronizacion.isPresent()){
            log.info("La fecha recuperada es: " + ultimaSincronizacion.get().getLastSynchroDate());
            return ultimaSincronizacion.get().getLastSynchroDate();
        } else {
            log.info("No ha habido todavía ninguna sincronización");
            return Timestamp.from(Instant.now().plusSeconds(-999999999));
        }
    }

    private List<PilotCommandEntity> recuperarPilotosH2(Timestamp ultimaSincronizacion){
        log.info("Se recuperan registro del H2 que no se hayan sincronizado. Fecha recibida de última sincronización: " + ultimaSincronizacion);
        List<PilotCommandEntity> pilotosH2 = pilotCommandService.recuperarPilotosASincronizar(ultimaSincronizacion);
        log.info("Número de registros de pilotos recuperados: " + pilotosH2.size());
        return pilotosH2;
    }

    private PilotQueryDocument grabarPilotoMongoDB(PilotCommandEntity pH2){
        log.info("Se graba la info en MongoDB con lo recibido");
        PilotQueryDocument pMongoDB;
        if (!pH2.isAvailable()) {
            pMongoDB = pilotQueryService.recuperarPilotoQuery(pH2.getCodeA3());
            pilotQueryService.eliminarPiloto(pH2.getCodeA3());
        } else {
            pMongoDB = mapearPilotoDeH2AMongoDB(pH2);
            pilotQueryService.grabarPiloto(pMongoDB);
        }
        return pMongoDB;
    }

    private PilotQueryDocument mapearPilotoDeH2AMongoDB(PilotCommandEntity pH2){
        log.info("Se mapea el piloto de H2 a MongoDB");
        return PilotQueryDocument.builder()
                            .code(pH2.getCodeA3())
                            .name(pH2.getName())
                            .date_of_birth(pH2.getBirthDate())
                            .skillList(mapearHabilidadesDeH2AMongoDB(pilotCommandService.recuperarHabilidadesPilotoDesdeCodigoPiloto(pH2.getCodeA3())))
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
