package es.f1.pilotos.query.service;

import es.f1.pilotos.query.repository.Piloto;
import es.f1.pilotos.query.repository.PilotoQueryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PilotoQueryService {
    @Autowired
    PilotoQueryRepo pilotoQueryRepo;

    public void eliminarTodosPilotos(){
        pilotoQueryRepo.deleteAll();
    }

    public void grabarPilotos(List<Piloto> pilotos){
        pilotoQueryRepo.saveAll(pilotos);
    }
}
