package es.f1.pilotos.command.controller;

import es.f1.pilotos.PilotosApplication;
import es.f1.pilotos.command.exceptions.DuplicateObjectException;
import es.f1.pilotos.command.exceptions.ObjectNotFoundException;
import es.f1.pilotos.command.model.Piloto;
import es.f1.pilotos.command.model.PilotoHabilidad;
import es.f1.pilotos.command.service.PilotoCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommandController {
    private static final String API_VERSION = "/v1";
    private static final String API_PATH = PilotosApplication.PILOTOS_COMMAND_PATH + API_VERSION + "/piloto";

    @Autowired
    PilotoCommandService pilotoService;

    @Operation(summary = "Include a new pilot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pilot included",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Pilot already exists",
                    content = @Content) })
    @PostMapping(value = API_PATH + "/{codigoPiloto}")
    public ResponseEntity insertarPiloto(@PathVariable String codigoPiloto,
                                         @RequestBody Piloto pilotoRecibido){

        pilotoRecibido.setCodigo(codigoPiloto);
        pilotoService.insertarPiloto(pilotoRecibido);

        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Delete an exiting  pilot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pilot deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pilot not found",
                    content = @Content) })
    @DeleteMapping(value = API_PATH + "/{codigoPiloto}")
    public ResponseEntity modificarPiloto(@PathVariable String codigoPiloto){

        pilotoService.eliminarPiloto(codigoPiloto);

        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Update an existing pilot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pilot updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pilot not found",
                    content = @Content) })
    @PutMapping(value = API_PATH + "/{codigoPiloto}")
    public ResponseEntity modificarPiloto(@PathVariable String codigoPiloto,
                                          @RequestBody Piloto pilotoRecibido){

        pilotoRecibido.setCodigo(codigoPiloto);
        pilotoService.modificarPiloto(pilotoRecibido);

        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Include a new skill to an existing pilot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill included",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pilot/Skill not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Skill already exists",
                    content = @Content)})
    @PostMapping(value = API_PATH + "/{codigoPiloto}/habilidad/{codigoHabilidad}")
    public ResponseEntity insertarHabilidadEnPiloto(@PathVariable String codigoPiloto,
                                                    @PathVariable String codigoHabilidad,
                                                    @RequestBody PilotoHabilidad pilotoHabilidadRecibido){

        pilotoHabilidadRecibido.setCodigoPiloto(codigoPiloto);
        pilotoHabilidadRecibido.setCodigoHabilidad(codigoHabilidad);
        pilotoService.insertarHabilidadEnPiloto(pilotoHabilidadRecibido);

        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Update an existing skill to an existing pilot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pilot/Skill not found",
                    content = @Content) })
    @PutMapping(value = API_PATH + "/{codigoPiloto}/habilidad/{codigoHabilidad}")
    public ResponseEntity modificarHabilidadEnPiloto(@PathVariable String codigoPiloto,
                                                     @PathVariable String codigoHabilidad,
                                                     @RequestBody PilotoHabilidad pilotoHabilidadRecibido){

        pilotoHabilidadRecibido.setCodigoPiloto(codigoPiloto);
        pilotoHabilidadRecibido.setCodigoHabilidad(codigoHabilidad);
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
