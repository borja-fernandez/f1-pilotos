package es.f1.pilotos.query.service;

import es.f1.pilotos.query.repository.Piloto;
import es.f1.pilotos.query.repository.PilotoQueryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PilotoQueryService {
    @Autowired
    PilotoQueryRepo pilotoQueryRepo;

    public Optional<Piloto> recuperarPiloto(String codigo){
        return pilotoQueryRepo.findByCodigo(codigo);
    }
    public void eliminarTodosPilotos(){
        pilotoQueryRepo.deleteAll();
    }

    public void eliminarPiloto(String codigo){
        pilotoQueryRepo.deleteById(codigo);
    }

    public void grabarPiloto(Piloto pilotoAGrabar){
        pilotoQueryRepo.save(pilotoAGrabar);
    }
}
