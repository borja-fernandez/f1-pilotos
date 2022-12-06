package com.f1.pilots.query.service;

import com.f1.pilots.command.exceptions.ObjectNotFoundException;
import com.f1.pilots.query.model.PilotoResponse;
import com.f1.pilots.query.repository.Piloto;
import com.f1.pilots.query.repository.PilotoQueryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PilotoQueryService {
    @Autowired
    PilotoQueryRepo pilotoQueryRepo;

    public PilotoResponse recuperarPiloto(String codigo){
        return mapearPilotoResponseDesdePilotoQuery(recuperarPilotoQuery(codigo));
    }

    private Piloto recuperarPilotoQuery(String codigo){
        Optional<Piloto> piloto = pilotoQueryRepo.findByCodigo(codigo);

        if(!piloto.isPresent()) {
            throw new ObjectNotFoundException("Piloto " + codigo + " no encontrado");
        }
        return piloto.get();
    }

    private PilotoResponse mapearPilotoResponseDesdePilotoQuery(Piloto pilotoQuery){
        return PilotoResponse.builder()
                .codigo(pilotoQuery.codigo)
                .fecha_nacimiento(pilotoQuery.fecha_nacimiento)
                .nombre(pilotoQuery.nombre)
                .habilidades(pilotoQuery.habilidades)
                .build();
    }

    public void eliminarPiloto(String codigo){
        pilotoQueryRepo.deleteById(codigo);
    }

    public void grabarPiloto(Piloto pilotoAGrabar){
        pilotoQueryRepo.save(pilotoAGrabar);
    }
}
