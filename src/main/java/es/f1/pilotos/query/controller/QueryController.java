package es.f1.pilotos.query.controller;

import es.f1.pilotos.PilotosApplication;
import es.f1.pilotos.command.exceptions.ObjectNotFoundException;
import es.f1.pilotos.query.model.PilotoResponse;
import es.f1.pilotos.query.service.PilotoQueryService;
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
    private static final String API_PATH = PilotosApplication.PILOTOS_QUERY_PATH + API_VERSION + "/piloto";

    @Autowired
    PilotoQueryService pilotoQueryService;

    @GetMapping(value = API_PATH + "/{id}")
    public ResponseEntity<PilotoResponse> getPiloto(@PathVariable String id){
        return ResponseEntity.ok(pilotoQueryService.recuperarPiloto(id));
    }

    @ExceptionHandler({ ObjectNotFoundException.class  })
    public ResponseEntity manejarObjectNotFoundException(RuntimeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
