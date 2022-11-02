package es.f1.pilotos.command.service;

import es.f1.pilotos.command.exceptions.DuplicateObjectException;
import es.f1.pilotos.command.exceptions.ObjectNotFoundException;
import es.f1.pilotos.command.model.PilotoHabilidad;
import es.f1.pilotos.command.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

@Service
public class Piloto {

    @Autowired
    PilotoRepo pilotoRepo;

    @Autowired
    HabilidadRepo habilidadRepo;

    @Autowired
    PilotoHabilidadRepo pilotoHabilidadRepo;

    public void insertarPiloto(es.f1.pilotos.command.model.Piloto pilotoRecibido){
        confirmarNoExistenciaPiloto(pilotoRecibido.getCodigo());

        pilotoRepo.save(mapearPilotoDesdeEntrada(pilotoRecibido));
    }

    public void eliminarPiloto(String codigo){
        es.f1.pilotos.command.repository.Piloto pilotoBD = confirmarExistenciaPiloto(codigo);
        Collection<es.f1.pilotos.command.repository.PilotoHabilidad> habilidadesPilotoBD = pilotoHabilidadRepo.findByIdPilotoOrderByIdHabilidad(pilotoBD.getId());
        pilotoHabilidadRepo.deleteAll(habilidadesPilotoBD);
        pilotoRepo.delete(pilotoBD);
    }

    public void modificarPiloto(es.f1.pilotos.command.model.Piloto pilotoRecibido){
        es.f1.pilotos.command.repository.Piloto pilotoBD = confirmarExistenciaPiloto(pilotoRecibido.getCodigo());

        pilotoRepo.save(mapearPilotoExistenteDesdeEntrada(pilotoBD.getId(), pilotoRecibido));
    }

    public void insertarHabilidadEnPiloto(PilotoHabilidad pilotoHabilidadRecibido){
        es.f1.pilotos.command.repository.Piloto pilotoBD = confirmarExistenciaPiloto(pilotoHabilidadRecibido.getCodigoPiloto());
        Habilidad habilidadBD = recuperarHabilidadDesdeCodigo(pilotoHabilidadRecibido.getCodigoHabilidad());

        confirmarNoExistenciaPilotoHabilidad(pilotoBD, habilidadBD);

        pilotoHabilidadRepo.save(mapearNuevaHabilidadPilotoDesdeEntrada(pilotoBD, habilidadBD, pilotoHabilidadRecibido, TipoRegistro.tipoRegistro.ALTA));
    }

    public void modificarHabilidadEnPiloto(PilotoHabilidad pilotoHabilidadRecibido){
        es.f1.pilotos.command.repository.Piloto pilotoBD = confirmarExistenciaPiloto(pilotoHabilidadRecibido.getCodigoPiloto());
        Habilidad habilidadBD = recuperarHabilidadDesdeCodigo(pilotoHabilidadRecibido.getCodigoHabilidad());

        confirmarExistenciaPilotoHabilidad(pilotoBD, habilidadBD);

        pilotoHabilidadRepo.save(mapearNuevaHabilidadPilotoDesdeEntrada(pilotoBD, habilidadBD, pilotoHabilidadRecibido, TipoRegistro.tipoRegistro.MODIFICACION));
    }

    private void confirmarNoExistenciaPiloto(String codigoPiloto){
        Optional<es.f1.pilotos.command.repository.Piloto> pilotoDB = pilotoRepo.findByCodigo(codigoPiloto);
        if(pilotoDB.isPresent()) {
            throw new DuplicateObjectException("Ya existe un piloto con el código "+codigoPiloto);
        }
    }

    private void confirmarNoExistenciaPilotoHabilidad(es.f1.pilotos.command.repository.Piloto pilotoBD , Habilidad habilidadBD){
        Optional<es.f1.pilotos.command.repository.PilotoHabilidad> pilotoHabilidadBD =
                pilotoHabilidadRepo.findFirstByIdPilotoAndAndIdHabilidadOrderByFechaCreacionDesc(
                        pilotoBD.getId(),
                        habilidadBD.getId());
        if(pilotoHabilidadBD.isPresent()) {
            throw new DuplicateObjectException("Ya está asociada la habilidad " + habilidadBD.getCodigo().trim() + " al piloto "+pilotoBD.getCodigo());
        }
    }

    private es.f1.pilotos.command.repository.Piloto confirmarExistenciaPiloto(String codigoPiloto){
        Optional<es.f1.pilotos.command.repository.Piloto> pilotoDB = pilotoRepo.findByCodigo(codigoPiloto);
        if(!pilotoDB.isPresent()) {
            throw new ObjectNotFoundException("Piloto inexistente con código "+codigoPiloto);
        }
        return pilotoDB.get();
    }

    private es.f1.pilotos.command.repository.PilotoHabilidad confirmarExistenciaPilotoHabilidad(es.f1.pilotos.command.repository.Piloto pilotoBD, Habilidad habilidadBD){
        Optional<es.f1.pilotos.command.repository.PilotoHabilidad> pilotoHabilidadBD =
                pilotoHabilidadRepo.findFirstByIdPilotoAndAndIdHabilidadOrderByFechaCreacionDesc(
                        pilotoBD.getId(),
                        habilidadBD.getId());
        if(!pilotoHabilidadBD.isPresent()) {
            throw new ObjectNotFoundException("La habilidad " + habilidadBD.getCodigo() + " no está asociada al piloto "+pilotoBD.getCodigo());
        }
        return pilotoHabilidadBD.get();
    }

    private Habilidad recuperarHabilidadDesdeCodigo(String codigoHabilidad){
        Optional<Habilidad> habilidadDB = habilidadRepo.findByCodigo(codigoHabilidad);
        if(!habilidadDB.isPresent()) {
            throw new ObjectNotFoundException("Habilidad inexistente con código "+codigoHabilidad);
        }
        return habilidadDB.get();
    }

    private es.f1.pilotos.command.repository.Piloto mapearPilotoExistenteDesdeEntrada(int idPiloto, es.f1.pilotos.command.model.Piloto pilotoRecibido){
        return es.f1.pilotos.command.repository.Piloto.builder()
                .id(idPiloto)
                .codigo(pilotoRecibido.getCodigo())
                .nombre(pilotoRecibido.getNombre())
                .build();
    }

    private es.f1.pilotos.command.repository.Piloto mapearPilotoDesdeEntrada(es.f1.pilotos.command.model.Piloto pilotoRecibido){
        return es.f1.pilotos.command.repository.Piloto.builder()
                .codigo(pilotoRecibido.getCodigo())
                .nombre(pilotoRecibido.getNombre())
                .build();
    }

    private es.f1.pilotos.command.repository.PilotoHabilidad mapearNuevaHabilidadPilotoDesdeEntrada(es.f1.pilotos.command.repository.Piloto pilotoBD, Habilidad habilidadBD, PilotoHabilidad pilotoHabilidadRecibida, TipoRegistro.tipoRegistro tipoRegistro){
        return es.f1.pilotos.command.repository.PilotoHabilidad.builder()
                .idPiloto(pilotoBD.getId())
                .idHabilidad(habilidadBD.getId())
                .valor(pilotoHabilidadRecibida.getValor())
                .fechaCreacion(new Timestamp(System.currentTimeMillis()))
                .tipoRegistro(tipoRegistro)
                .build();
    }

}
