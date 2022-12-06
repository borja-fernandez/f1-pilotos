package com.f1.pilots.query.service;

import com.f1.pilots.command.exceptions.ObjectNotFoundException;
import com.f1.pilots.query.model.PilotQueryResponse;
import com.f1.pilots.query.repository.PilotQueryDocument;
import com.f1.pilots.query.repository.PilotQueryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PilotQueryService {
    @Autowired
    PilotQueryRepo pilotQueryRepo;

    public PilotQueryResponse recuperarPiloto(String codigo){
        return mapearPilotoResponseDesdePilotoQuery(recuperarPilotoQuery(codigo));
    }

    private PilotQueryDocument recuperarPilotoQuery(String codigo){
        Optional<PilotQueryDocument> piloto = pilotQueryRepo.findByCode(codigo);

        if(!piloto.isPresent()) {
            throw new ObjectNotFoundException("Pilot " + codigo + " not found");
        }
        return piloto.get();
    }

    private PilotQueryResponse mapearPilotoResponseDesdePilotoQuery(PilotQueryDocument pilotQueryDocumentQuery){
        return PilotQueryResponse.builder()
                .code(pilotQueryDocumentQuery.code)
                .date_of_birth(pilotQueryDocumentQuery.date_of_birth)
                .name(pilotQueryDocumentQuery.name)
                .skills(pilotQueryDocumentQuery.skillList)
                .build();
    }

    public void eliminarPiloto(String codigo){
        pilotQueryRepo.deleteById(codigo);
    }

    public void grabarPiloto(PilotQueryDocument pilotQueryDocumentAGrabar){
        pilotQueryRepo.save(pilotQueryDocumentAGrabar);
    }
}
