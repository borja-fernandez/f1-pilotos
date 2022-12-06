package com.f1.pilots.query.controller;

import com.f1.pilots.PilotsApplication;
import com.f1.pilots.command.exceptions.ObjectNotFoundException;
import com.f1.pilots.query.model.PilotQueryResponse;
import com.f1.pilots.query.service.PilotQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryController {
    private static final String API_VERSION = "/v1";
    private static final String API_PATH = PilotsApplication.PILOT_QUERY_PATH + API_VERSION + "/pilot";

    @Autowired
    PilotQueryService pilotQueryService;

    @Operation(summary = "Get a pilot by its code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the pilot",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PilotQueryResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Pilot not found",
                    content = @Content) })
    @GetMapping(value = API_PATH + "/{pilot_code}")
    public ResponseEntity<PilotQueryResponse> getPiloto(@PathVariable String pilot_code){
        return ResponseEntity.ok(pilotQueryService.recuperarPiloto(pilot_code));
    }

    @ExceptionHandler({ ObjectNotFoundException.class  })
    public ResponseEntity manejarObjectNotFoundException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
