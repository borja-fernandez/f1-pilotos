package es.f1.pilotos.sync.service;

import es.f1.pilotos.command.service.PilotoCommandService;
import es.f1.pilotos.query.service.PilotoQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class PilotoSyncService {

    @Autowired
    PilotoCommandService pilotoCommandServiceCommand;

    @Autowired
    PilotoQueryService pilotoQueryServiceQuery;

    public void syncPilotos(){
        pilotoQueryServiceQuery.eliminarTodosPilotos();
        List<es.f1.pilotos.command.repository.Piloto> pilotosH2 = pilotoCommandServiceCommand.recuperarTodosPilotos();
        List<es.f1.pilotos.query.repository.Piloto> pilotosMongo = mapearPilotosDeH2AMongoDB(pilotosH2);
        pilotoQueryServiceQuery.grabarPilotos(pilotosMongo);
    }

    private List<es.f1.pilotos.query.repository.Piloto> mapearPilotosDeH2AMongoDB(List<es.f1.pilotos.command.repository.Piloto> pilotosH2){
        List<es.f1.pilotos.query.repository.Piloto> pilotosMongo = new ArrayList<>();
        for(es.f1.pilotos.command.repository.Piloto pilotoH2: pilotosH2){

            if (pilotoH2.esAlta()){
                pilotosMongo.add(es.f1.pilotos.query.repository.Piloto.builder()
                                    .codigo(pilotoH2.getCodigo())
                                    .nombre(pilotoH2.getNombre())
                                    .fecha_nacimiento(pilotoH2.getFechaNacimiento())
                                    .habilidades(mapearHabilidadesDeH2AMongoDB(pilotoH2.getHabilidadesPiloto()))
                                    .build());
            } else if (pilotoH2.esModification()) {
                Optional<es.f1.pilotos.query.repository.Piloto> pilotoMongoOptional = pilotosMongo.stream().filter(p -> p.codigo.equalsIgnoreCase(pilotoH2.getCodigo())).findFirst();
                if (pilotoMongoOptional.isPresent()){
                    pilotosMongo.removeIf(p -> p.codigo.equalsIgnoreCase(pilotoH2.getCodigo()));
                    pilotosMongo.add(es.f1.pilotos.query.repository.Piloto.builder()
                            .codigo(pilotoH2.getCodigo())
                            .nombre(pilotoH2.getNombre())
                            .fecha_nacimiento(pilotoH2.getFechaNacimiento())
                            .habilidades(pilotoMongoOptional.get().habilidades)
                            .build());
                }
                pilotosMongo.stream().filter(p -> p.codigo.equalsIgnoreCase(pilotoH2.getCodigo())).findFirst();

            } else if (!pilotoH2.estaEnVigor()) {
                pilotosMongo.removeIf(p -> p.codigo.equalsIgnoreCase(pilotoH2.getCodigo()));
            }
        }
        return pilotosMongo;
    }

    private HashMap<String, Integer> mapearHabilidadesDeH2AMongoDB(List<es.f1.pilotos.command.repository.PilotoHabilidad> habilidadtesPilotoH2){
        HashMap<String, Integer> habilidadtesPilotosMongoDB = new HashMap<String, Integer>();
        for(es.f1.pilotos.command.repository.PilotoHabilidad habilidadPilotoH2: habilidadtesPilotoH2){

            if (habilidadPilotoH2.esAlta()){
                habilidadtesPilotosMongoDB.put(habilidadPilotoH2.getHabilidad().getCodigo(), habilidadPilotoH2.getValor());
            } else if (habilidadPilotoH2.esModification()) {
                habilidadtesPilotosMongoDB.replace(habilidadPilotoH2.getHabilidad().getCodigo(), habilidadtesPilotosMongoDB.get(habilidadPilotoH2.getHabilidad().getCodigo()) + habilidadPilotoH2.getValor());
            } else if (!habilidadPilotoH2.estaEnVigor()) {
                habilidadtesPilotosMongoDB.remove(habilidadPilotoH2.getHabilidad().getCodigo());
            }
        }
        return habilidadtesPilotosMongoDB;
    }
}
