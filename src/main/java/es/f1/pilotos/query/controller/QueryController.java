package es.f1.pilotos.query.controller;

import es.f1.pilotos.PilotosApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueryController {
    private static final String API_VERSION = "/v1";
    private static final String API_PATH = PilotosApplication.PILOTOS_QUERY_PATH + API_VERSION + "/piloto";

    @GetMapping(value = API_PATH + "/{id}")
    public String getPiloto(@PathVariable String id){
        return "Piloto: " +  id;
    }
}
