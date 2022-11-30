package es.f1.pilotos.command.service;

import es.f1.pilotos.command.exceptions.DuplicateObjectException;
import es.f1.pilotos.command.exceptions.ObjectNotFoundException;
import es.f1.pilotos.command.model.PilotoHabilidad;
import es.f1.pilotos.command.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class PilotoCommandService {

    @Autowired
    PilotoCommandRepo pilotoCommandRepo;

    @Autowired
    HabilidadRepo habilidadRepo;

    @Autowired
    PilotoHabilidadRepo pilotoHabilidadRepo;

    public void insertarPiloto(es.f1.pilotos.command.model.Piloto pilotoRecibido){
        confirmarNoExistenciaPiloto(pilotoRecibido.getCodigo());

        pilotoCommandRepo.save(mapearPilotoDesdeEntrada(pilotoRecibido, TipoRegistro.tipoRegistro.ALTA_PILOTO));
    }

    public void eliminarPiloto(String codigo){
        es.f1.pilotos.command.repository.Piloto ultimoRegistroDePilotoBD = confirmarExistenciaPiloto(codigo);

        pilotoCommandRepo.save(mapearPilotoDesdeBD(ultimoRegistroDePilotoBD, TipoRegistro.tipoRegistro.BORRADO_PILOTO));
    }

    public void modificarPiloto(es.f1.pilotos.command.model.Piloto pilotoRecibido){
        confirmarExistenciaPiloto(pilotoRecibido.getCodigo());

        pilotoCommandRepo.save(mapearPilotoDesdeEntrada(pilotoRecibido, TipoRegistro.tipoRegistro.MODIFICACION_PILOTO));
    }

    public void insertarHabilidadEnPiloto(PilotoHabilidad pilotoHabilidadRecibido){
        es.f1.pilotos.command.repository.Piloto pilotoBD = confirmarExistenciaPiloto(pilotoHabilidadRecibido.getCodigoPiloto());
        Habilidad habilidadBD = recuperarHabilidadDesdeCodigo(pilotoHabilidadRecibido.getCodigoHabilidad());

        confirmarNoExistenciaPilotoHabilidad(pilotoBD, habilidadBD);

        pilotoHabilidadRepo.save(mapearNuevaHabilidadPilotoDesdeEntrada(pilotoBD, habilidadBD, pilotoHabilidadRecibido, TipoRegistro.tipoRegistro.ALTA_HABILIDAD));
        pilotoCommandRepo.save(mapearPilotoDesdeBD(pilotoBD, TipoRegistro.tipoRegistro.MODIFICACION_HABILIDAD));
    }

    public void modificarHabilidadEnPiloto(PilotoHabilidad pilotoHabilidadRecibido){
        es.f1.pilotos.command.repository.Piloto pilotoBD = confirmarExistenciaPiloto(pilotoHabilidadRecibido.getCodigoPiloto());
        Habilidad habilidadBD = recuperarHabilidadDesdeCodigo(pilotoHabilidadRecibido.getCodigoHabilidad());

        confirmarExistenciaPilotoHabilidad(pilotoBD, habilidadBD);

        pilotoHabilidadRepo.save(mapearNuevaHabilidadPilotoDesdeEntrada(pilotoBD, habilidadBD, pilotoHabilidadRecibido, TipoRegistro.tipoRegistro.MODIFICACION_HABILIDAD));
        pilotoCommandRepo.save(mapearPilotoDesdeBD(pilotoBD, TipoRegistro.tipoRegistro.MODIFICACION_HABILIDAD));
    }

    private void confirmarNoExistenciaPiloto(String codigoPiloto){
        Optional<es.f1.pilotos.command.repository.Piloto> ultimoRegistroDePilotoDB = pilotoCommandRepo.findFirstByCodigoOrderByFechaCreacionDesc(codigoPiloto);
        if(ultimoRegistroDePilotoDB.isPresent() && ultimoRegistroDePilotoDB.get().estaEnVigor()) {
            throw new DuplicateObjectException("Ya existe un piloto con el código "+codigoPiloto);
        }
    }

    private void confirmarNoExistenciaPilotoHabilidad(es.f1.pilotos.command.repository.Piloto pilotoBD , Habilidad habilidadBD){
        Optional<es.f1.pilotos.command.repository.PilotoHabilidad> pilotoHabilidadBD =
                pilotoHabilidadRepo.findFirstByCodigoPilotoAndAndIdHabilidadOrderByFechaCreacionDesc(
                        pilotoBD.getCodigo(),
                        habilidadBD.getId());
        if(pilotoHabilidadBD.isPresent()) {
            throw new DuplicateObjectException("Ya está asociada la habilidad " + habilidadBD.getCodigo().trim() + " al piloto "+pilotoBD.getCodigo());
        }
    }

    private es.f1.pilotos.command.repository.Piloto confirmarExistenciaPiloto(String codigoPiloto){
        Optional<es.f1.pilotos.command.repository.Piloto> ultimoRegistroDePilotoDB = pilotoCommandRepo.findFirstByCodigoOrderByFechaCreacionDesc(codigoPiloto);
        if(!ultimoRegistroDePilotoDB.isPresent() || !ultimoRegistroDePilotoDB.get().estaEnVigor()) {
            throw new ObjectNotFoundException("Piloto inexistente con código "+codigoPiloto);
        }
        return ultimoRegistroDePilotoDB.get();
    }

    private es.f1.pilotos.command.repository.PilotoHabilidad confirmarExistenciaPilotoHabilidad(es.f1.pilotos.command.repository.Piloto pilotoBD, Habilidad habilidadBD){
        Optional<es.f1.pilotos.command.repository.PilotoHabilidad> pilotoHabilidadBD =
                pilotoHabilidadRepo.findFirstByCodigoPilotoAndAndIdHabilidadOrderByFechaCreacionDesc(
                        pilotoBD.getCodigo(),
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

    public List<es.f1.pilotos.command.repository.PilotoHabilidad> recuperarHabilidadesPilotoDesdeCodigoPiloto(String codigoPiloto){
        return pilotoHabilidadRepo.findByCodigoPilotoOrderByIdHabilidad(codigoPiloto);
    }

    private es.f1.pilotos.command.repository.Piloto mapearPilotoDesdeEntrada(es.f1.pilotos.command.model.Piloto pilotoRecibido, TipoRegistro.tipoRegistro tipoRegistro){
        return es.f1.pilotos.command.repository.Piloto.builder()
                .codigo(pilotoRecibido.getCodigo())
                .nombre(pilotoRecibido.getNombre())
                .fechaCreacion(new Timestamp(System.currentTimeMillis()))
                .tipoRegistro(tipoRegistro)
                .build();
    }

    private es.f1.pilotos.command.repository.Piloto mapearPilotoDesdeBD(es.f1.pilotos.command.repository.Piloto pilotoBD, TipoRegistro.tipoRegistro tipoRegistro){
        return es.f1.pilotos.command.repository.Piloto.builder()
                .codigo(pilotoBD.getCodigo())
                .nombre(pilotoBD.getNombre())
                .fechaCreacion(new Timestamp(System.currentTimeMillis()))
                .tipoRegistro(tipoRegistro)
                .build();
    }

    private es.f1.pilotos.command.repository.PilotoHabilidad mapearNuevaHabilidadPilotoDesdeEntrada(es.f1.pilotos.command.repository.Piloto pilotoBD, Habilidad habilidadBD, PilotoHabilidad pilotoHabilidadRecibida, TipoRegistro.tipoRegistro tipoRegistro){
        return es.f1.pilotos.command.repository.PilotoHabilidad.builder()
                .codigoPiloto(pilotoBD.getCodigo())
                .idHabilidad(habilidadBD.getId())
                .valor(pilotoHabilidadRecibida.getValor())
                .fechaCreacion(new Timestamp(System.currentTimeMillis()))
                .tipoRegistro(tipoRegistro)
                .build();
    }

    public List<Piloto> recuperarPilotosASincronizar(Timestamp ultimaSincronizacion){
        return pilotoCommandRepo.findAllByFechaCreacionAfterOrderById(ultimaSincronizacion);
    }
}
