package com.f1.pilots.command.service;

import com.f1.pilots.command.exceptions.DuplicateObjectException;
import com.f1.pilots.command.exceptions.ObjectNotFoundException;
import com.f1.pilots.command.model.PilotCommandRequest;
import com.f1.pilots.command.model.SkillCommandRequest;
import com.f1.pilots.command.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class PilotoCommandService {

    @Autowired
    PilotCommandRepo pilotCommandRepo;

    @Autowired
    SkillCommandRepo skillCommandRepo;

    @Autowired
    PilotSkillCommandRepo pilotSkillCommandRepo;

    public void insertarPiloto(PilotCommandRequest pilotCommandRequestRecibido){
        confirmarNoExistenciaPiloto(pilotCommandRequestRecibido.getCodeA3());

        pilotCommandRepo.save(mapearPilotoDesdeEntrada(pilotCommandRequestRecibido, Movement.type.ALTA_PILOTO));
    }

    public void eliminarPiloto(String codigo){
        PilotCommandEntity ultimoRegistroDePilotCommandEntityBD = confirmarExistenciaPiloto(codigo);

        pilotCommandRepo.save(mapearPilotoDesdeBD(ultimoRegistroDePilotCommandEntityBD, Movement.type.BORRADO_PILOTO));
    }

    public void modificarPiloto(PilotCommandRequest pilotCommandRequestRecibido){
        confirmarExistenciaPiloto(pilotCommandRequestRecibido.getCodeA3());

        pilotCommandRepo.save(mapearPilotoDesdeEntrada(pilotCommandRequestRecibido, Movement.type.MODIFICACION_PILOTO));
    }

    public void insertarHabilidadEnPiloto(SkillCommandRequest skillCommandRequestRecibido){
        PilotCommandEntity pilotCommandEntityBD = confirmarExistenciaPiloto(skillCommandRequestRecibido.getPilotCodeA3());
        SkillCommandEntity skillCommandEntityBD = recuperarHabilidadDesdeCodigo(skillCommandRequestRecibido.getSkillCode());

        confirmarNoExistenciaPilotoHabilidad(pilotCommandEntityBD, skillCommandEntityBD);

        pilotSkillCommandRepo.save(mapearNuevaHabilidadPilotoDesdeEntrada(pilotCommandEntityBD, skillCommandEntityBD, skillCommandRequestRecibido, Movement.type.ALTA_HABILIDAD));
        pilotCommandRepo.save(mapearPilotoDesdeBD(pilotCommandEntityBD, Movement.type.MODIFICACION_HABILIDAD));
    }

    public void modificarHabilidadEnPiloto(SkillCommandRequest skillCommandRequestRecibido){
        PilotCommandEntity pilotCommandEntityBD = confirmarExistenciaPiloto(skillCommandRequestRecibido.getPilotCodeA3());
        SkillCommandEntity skillCommandEntityBD = recuperarHabilidadDesdeCodigo(skillCommandRequestRecibido.getSkillCode());

        confirmarExistenciaPilotoHabilidad(pilotCommandEntityBD, skillCommandEntityBD);

        pilotSkillCommandRepo.save(mapearNuevaHabilidadPilotoDesdeEntrada(pilotCommandEntityBD, skillCommandEntityBD, skillCommandRequestRecibido, Movement.type.MODIFICACION_HABILIDAD));
        pilotCommandRepo.save(mapearPilotoDesdeBD(pilotCommandEntityBD, Movement.type.MODIFICACION_HABILIDAD));
    }

    private void confirmarNoExistenciaPiloto(String codigoPiloto){
        Optional<PilotCommandEntity> ultimoRegistroDePilotoDB = pilotCommandRepo.findFirstByCodeA3OrderByCreationDateDesc(codigoPiloto);
        if(ultimoRegistroDePilotoDB.isPresent() && ultimoRegistroDePilotoDB.get().isAvailable()) {
            throw new DuplicateObjectException("Ya existe un piloto con el código "+codigoPiloto);
        }
    }

    private void confirmarNoExistenciaPilotoHabilidad(PilotCommandEntity pilotCommandEntityBD, SkillCommandEntity skillCommandEntityBD){
        Optional<PilotSkillCommandEntity> pilotoHabilidadBD =
                pilotSkillCommandRepo.findFirstByPilotCodeA3AndSkillIdOrderByCreationDateDesc(
                        pilotCommandEntityBD.getCodeA3(),
                        skillCommandEntityBD.getId());
        if(pilotoHabilidadBD.isPresent()) {
            throw new DuplicateObjectException("Ya está asociada la habilidad " + skillCommandEntityBD.getCode().trim() + " al piloto "+ pilotCommandEntityBD.getCodeA3());
        }
    }

    private PilotCommandEntity confirmarExistenciaPiloto(String codigoPiloto){
        Optional<PilotCommandEntity> ultimoRegistroDePilotoDB = pilotCommandRepo.findFirstByCodeA3OrderByCreationDateDesc(codigoPiloto);
        if(!ultimoRegistroDePilotoDB.isPresent() || !ultimoRegistroDePilotoDB.get().isAvailable()) {
            throw new ObjectNotFoundException("Piloto inexistente con código "+codigoPiloto);
        }
        return ultimoRegistroDePilotoDB.get();
    }

    private PilotSkillCommandEntity confirmarExistenciaPilotoHabilidad(PilotCommandEntity pilotCommandEntityBD, SkillCommandEntity skillCommandEntityBD){
        Optional<PilotSkillCommandEntity> pilotoHabilidadBD =
                pilotSkillCommandRepo.findFirstByPilotCodeA3AndSkillIdOrderByCreationDateDesc(
                        pilotCommandEntityBD.getCodeA3(),
                        skillCommandEntityBD.getId());
        if(!pilotoHabilidadBD.isPresent()) {
            throw new ObjectNotFoundException("La habilidad " + skillCommandEntityBD.getCode() + " no está asociada al piloto "+ pilotCommandEntityBD.getCodeA3());
        }
        return pilotoHabilidadBD.get();
    }

    private SkillCommandEntity recuperarHabilidadDesdeCodigo(String codigoHabilidad){
        Optional<SkillCommandEntity> habilidadDB = skillCommandRepo.findByCode(codigoHabilidad);
        if(!habilidadDB.isPresent()) {
            throw new ObjectNotFoundException("Habilidad inexistente con código "+codigoHabilidad);
        }
        return habilidadDB.get();
    }

    public List<PilotSkillCommandEntity> recuperarHabilidadesPilotoDesdeCodigoPiloto(String codigoPiloto){
        return pilotSkillCommandRepo.findByPilotCodeA3OrderBySkillId(codigoPiloto);
    }

    private PilotCommandEntity mapearPilotoDesdeEntrada(PilotCommandRequest pilotCommandRequestRecibido, Movement.type type){
        return PilotCommandEntity.builder()
                .codeA3(pilotCommandRequestRecibido.getCodeA3())
                .name(pilotCommandRequestRecibido.getName())
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .type(type)
                .build();
    }

    private PilotCommandEntity mapearPilotoDesdeBD(PilotCommandEntity pilotCommandEntityBD, Movement.type type){
        return PilotCommandEntity.builder()
                .codeA3(pilotCommandEntityBD.getCodeA3())
                .name(pilotCommandEntityBD.getName())
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .type(type)
                .build();
    }

    private PilotSkillCommandEntity mapearNuevaHabilidadPilotoDesdeEntrada(PilotCommandEntity pilotCommandEntityBD, SkillCommandEntity skillCommandEntityBD, SkillCommandRequest skillCommandRequestRecibida, Movement.type type){
        return PilotSkillCommandEntity.builder()
                .pilotCodeA3(pilotCommandEntityBD.getCodeA3())
                .skillId(skillCommandEntityBD.getId())
                .value(skillCommandRequestRecibida.getValue())
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .type(type)
                .build();
    }

    public List<PilotCommandEntity> recuperarPilotosASincronizar(Timestamp ultimaSincronizacion){
        return pilotCommandRepo.findAllByCreationDateAfterOrderById(ultimaSincronizacion);
    }
}
