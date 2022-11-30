package es.f1.pilotos.query.service;

import es.f1.pilotos.command.exceptions.ObjectNotFoundException;
import es.f1.pilotos.query.repository.Piloto;
import es.f1.pilotos.query.repository.PilotoQueryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PilotoQueryService {
    @Autowired
    PilotoQueryRepo pilotoQueryRepo;

    public Piloto recuperarPiloto(String codigo){
        Optional<Piloto> piloto = pilotoQueryRepo.findByCodigo(codigo);

        if(!piloto.isPresent()) {
            throw new ObjectNotFoundException("Piloto " + codigo + " no encontrado");
        }
        return piloto.get();
    }

    public void eliminarPiloto(String codigo){
        pilotoQueryRepo.deleteById(codigo);
    }

    public void grabarPiloto(Piloto pilotoAGrabar){
        pilotoQueryRepo.save(pilotoAGrabar);
    }
}
