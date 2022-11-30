package es.f1.pilotos.sync.service;

import es.f1.pilotos.command.service.PilotoCommandService;
import es.f1.pilotos.query.service.PilotoQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PilotoSyncService {

    @Autowired
    PilotoCommandService pilotoCommandServiceCommand;

    @Autowired
    PilotoQueryService pilotoQueryServiceQuery;

    public void syncPilotos(){
        pilotoQueryServiceQuery.eliminarTodosPilotos();
        List<es.f1.pilotos.command.repository.Piloto> pilotosH2 = pilotoCommandServiceCommand.recuperarTodosPilotos();
        List<es.f1.pilotos.query.repository.Piloto> pilotosMongo = mapearDeH2AMongoDB(pilotosH2);
        pilotoQueryServiceQuery.grabarPilotos(pilotosMongo);
    }

    private List<es.f1.pilotos.query.repository.Piloto> mapearDeH2AMongoDB(List<es.f1.pilotos.command.repository.Piloto> pilotosH2){
        List<es.f1.pilotos.query.repository.Piloto> pilotosMongo = new ArrayList<>();
        for(es.f1.pilotos.command.repository.Piloto pilotoH2: pilotosH2){
            if (pilotoH2.esAlta()){
                pilotosMongo.add(es.f1.pilotos.query.repository.Piloto.builder()
                                    .codigo(pilotoH2.getCodigo())
                                    .nombre(pilotoH2.getNombre())
                                    .fecha_nacimiento(pilotoH2.getFechaNacimiento())
                                    .build());
            } else if (pilotoH2.esModification()) {

            } else if (!pilotoH2.estaEnVigor()) {
                pilotosMongo = pilotosMongo.stream().filter(mongo -> !mongo.codigo.equalsIgnoreCase(pilotoH2.getCodigo())).collect(Collectors.toList());
            }
        }
        return pilotosMongo;
    }
}
