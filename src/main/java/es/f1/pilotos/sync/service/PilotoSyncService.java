package es.f1.pilotos.sync.service;

import es.f1.pilotos.command.service.PilotoCommandService;
import es.f1.pilotos.query.service.PilotoQueryService;
import es.f1.pilotos.sync.repository.Sincronizacion;
import es.f1.pilotos.sync.repository.SincronizacionRepo;
import es.f1.pilotos.sync.repository.TipoSincronizacion;
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

        List<es.f1.pilotos.command.repository.Piloto> pilotosH2 = recuperarPilotosH2(ultimaSincronizacion);
        for(es.f1.pilotos.command.repository.Piloto pH2: pilotosH2){
            grabarPilotoMongoDB(pH2);
            sincronizacionRepo.save(Sincronizacion.builder()
                            .tipoSincronizacion(TipoSincronizacion.tipoSincronizacion.PILOTOS)
                            .fechaUltimaSincronizacion(pH2.getFechaCreacion())
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

    private List<es.f1.pilotos.command.repository.Piloto> recuperarPilotosH2(Timestamp ultimaSincronizacion){
        log.info("Se recuperan registro del H2 que no se hayan sincronizado. Fecha recibida de última sincronización: " + ultimaSincronizacion);
        List<es.f1.pilotos.command.repository.Piloto> pilotosH2 = pilotoCommandService.recuperarPilotosASincronizar(ultimaSincronizacion);
        log.info("Número de registros de pilotos recuperados: " + pilotosH2.size());
        return pilotosH2;
    }

    private void grabarPilotoMongoDB(es.f1.pilotos.command.repository.Piloto pH2){
        log.info("Se graba la info en MongoDB con lo recibido");
        if (!pH2.estaEnVigor()) {
            pilotoQueryServiceQuery.eliminarPiloto(pH2.getCodigo());
        } else {
            es.f1.pilotos.query.repository.Piloto pMongoDB = mapearPilotoDeH2AMongoDB(pH2);
            pilotoQueryServiceQuery.grabarPiloto(pMongoDB);
        }
    }

    private es.f1.pilotos.query.repository.Piloto mapearPilotoDeH2AMongoDB(es.f1.pilotos.command.repository.Piloto pH2){
        log.info("Se mapea el piloto de H2 a MongoDB");
        return es.f1.pilotos.query.repository.Piloto.builder()
                            .codigo(pH2.getCodigo())
                            .nombre(pH2.getNombre())
                            .fecha_nacimiento(pH2.getFechaNacimiento())
                            .habilidades(mapearHabilidadesDeH2AMongoDB(pilotoCommandService.recuperarHabilidadesPilotoDesdeCodigoPiloto(pH2.getCodigo())))
                            .build();
    }

    private HashMap<String, Integer> mapearHabilidadesDeH2AMongoDB(List<es.f1.pilotos.command.repository.PilotoHabilidad> habilidadtesPilotoH2){
        HashMap<String, Integer> habilidadtesPilotosMongoDB = new HashMap<String, Integer>();
        for(es.f1.pilotos.command.repository.PilotoHabilidad habilidadPilotoH2: habilidadtesPilotoH2){
            if (habilidadPilotoH2.esAlta()){
                habilidadtesPilotosMongoDB.put(habilidadPilotoH2.getHabilidad().getCodigo(), habilidadPilotoH2.getValor());
            } else if (habilidadPilotoH2.esModification()) {
                habilidadtesPilotosMongoDB.replace(habilidadPilotoH2.getHabilidad().getCodigo(), habilidadtesPilotosMongoDB.get(habilidadPilotoH2.getHabilidad().getCodigo()) + habilidadPilotoH2.getValor());
            }
        }
        return habilidadtesPilotosMongoDB;
    }
}
