package com.f1.pilots.command.controller;

import com.f1.pilots.PilotosApplication;
import com.f1.pilots.command.exceptions.DuplicateObjectException;
import com.f1.pilots.command.exceptions.ObjectNotFoundException;
import com.f1.pilots.command.model.PilotCommandRequest;
import com.f1.pilots.command.model.SkillCommandRequest;
import com.f1.pilots.command.service.PilotoCommandService;
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
    private static final String API_PATH = PilotosApplication.PILOT_COMMAND_PATH + API_VERSION + "/pilot";

    @Autowired
    PilotoCommandService pilotoService;

    @Operation(summary = "Include a new pilot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pilot included",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Pilot already exists",
                    content = @Content) })
    @PostMapping(value = API_PATH + "/{pilot_code}")
    public ResponseEntity insertarPiloto(@PathVariable String pilot_code,
                                         @RequestBody PilotCommandRequest pilotCommandRequest){

        pilotCommandRequest.setCodeA3(pilot_code);
        pilotoService.insertarPiloto(pilotCommandRequest);

        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Delete an exiting  pilot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pilot deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pilot not found",
                    content = @Content) })
    @DeleteMapping(value = API_PATH + "/{pilot_code}")
    public ResponseEntity modificarPiloto(@PathVariable String pilot_code){

        pilotoService.eliminarPiloto(pilot_code);

        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Update an existing pilot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pilot updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pilot not found",
                    content = @Content) })
    @PutMapping(value = API_PATH + "/{pilot_code}")
    public ResponseEntity modificarPiloto(@PathVariable String pilot_code,
                                          @RequestBody PilotCommandRequest pilotCommandRequest){

        pilotCommandRequest.setCodeA3(pilot_code);
        pilotoService.modificarPiloto(pilotCommandRequest);

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
    @PostMapping(value = API_PATH + "/{pilot_code}/skill/{skill_code}")
    public ResponseEntity insertarHabilidadEnPiloto(@PathVariable String pilot_code,
                                                    @PathVariable String skill_code,
                                                    @RequestBody SkillCommandRequest skillCommandRequest){

        skillCommandRequest.setPilotCodeA3(pilot_code);
        skillCommandRequest.setSkillCode(skill_code);
        pilotoService.insertarHabilidadEnPiloto(skillCommandRequest);

        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Update an existing skill to an existing pilot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Skill updated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pilot/Skill not found",
                    content = @Content) })
    @PutMapping(value = API_PATH + "/{pilot_code}/skill/{skill_code}")
    public ResponseEntity modificarHabilidadEnPiloto(@PathVariable String pilot_code,
                                                     @PathVariable String skill_code,
                                                     @RequestBody SkillCommandRequest skillCommandRequest){

        skillCommandRequest.setPilotCodeA3(pilot_code);
        skillCommandRequest.setSkillCode(skill_code);
        pilotoService.modificarHabilidadEnPiloto(skillCommandRequest);

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
