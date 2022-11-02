package es.f1.pilotos.command.controller;

import es.f1.pilotos.PilotosApplication;
import es.f1.pilotos.command.exceptions.DuplicateObjectException;
import es.f1.pilotos.command.exceptions.ObjectNotFoundException;
import es.f1.pilotos.command.model.Piloto;
import es.f1.pilotos.command.model.PilotoHabilidad;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.HashMap;

@RestController
public class CommandController {
    private static final String API_VERSION = "/v1";
    private static final String API_PATH = PilotosApplication.PILOTOS_COMMAND_PATH + API_VERSION + "/piloto";

    @Autowired
    es.f1.pilotos.command.service.Piloto pilotoService;

    @PostMapping(value = API_PATH)
    public ResponseEntity insertarPiloto(@RequestBody Piloto pilotoRecibido){
        HashMap<String, Integer> habilidades = new HashMap<>();
        habilidades.put("Lluvia", 100);
        habilidades.put("Seco", 98);
        habilidades.put("Salida", 95);

        Piloto p1 = Piloto.builder()
                .codigo("ALO")
                .nombre("Fernando Alonso")
                .habilidades(habilidades)
                .fechaNacimiento(new Date())
                .build();

        pilotoService.insertarPiloto(pilotoRecibido);

        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(value = API_PATH + "/{codigoA3}")
    public ResponseEntity modificarPiloto(@PathVariable String codigoA3){

        pilotoService.eliminarPiloto(codigoA3);

        return ResponseEntity.accepted().build();
    }

    @PutMapping(value = API_PATH)
    public ResponseEntity modificarPiloto(@RequestBody Piloto pilotoRecibido){

        pilotoService.modificarPiloto(pilotoRecibido);

        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = API_PATH + "/habilidad")
    public ResponseEntity insertarHabilidadEnPiloto(@RequestBody PilotoHabilidad pilotoHabilidadRecibido){

        pilotoService.insertarHabilidadEnPiloto(pilotoHabilidadRecibido);

        return ResponseEntity.accepted().build();
    }

    @PutMapping(value = API_PATH + "/habilidad")
    public ResponseEntity modificarHabilidadEnPiloto(@RequestBody PilotoHabilidad pilotoHabilidadRecibido){

        pilotoService.modificarHabilidadEnPiloto(pilotoHabilidadRecibido);

        return ResponseEntity.accepted().build();
    }


    @ExceptionHandler({  DuplicateObjectException.class })
    public ResponseEntity manejarDuplicateObjectException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({ ObjectNotFoundException.class  })
    public ResponseEntity manejarObjectNotFoundException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
