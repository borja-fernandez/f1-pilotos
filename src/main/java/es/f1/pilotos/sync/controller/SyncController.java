package es.f1.pilotos.sync.controller;

import es.f1.pilotos.PilotosApplication;
import es.f1.pilotos.sync.service.PilotoSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SyncController {
    private static final String API_VERSION = "/v1";
    private static final String API_PATH = PilotosApplication.PILOTOS_SYNC_PATH + API_VERSION + "/pilotos";

    @Autowired
    PilotoSyncService pilotoSyncService;

    @PatchMapping(value = API_PATH)
    public ResponseEntity syncPilotos(){

        pilotoSyncService.syncPilotos();
        return ResponseEntity.ok().build();
    }
}
