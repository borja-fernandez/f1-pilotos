package com.f1.pilots.sync.controller;

import com.f1.pilots.PilotosApplication;
import com.f1.pilots.sync.service.PilotoSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SyncController {
    private static final String API_VERSION = "/v1";
    private static final String API_PATH = PilotosApplication.PILOT_SYNC_PATH + API_VERSION;

    @Autowired
    PilotoSyncService pilotoSyncService;

    @PatchMapping(value = API_PATH)
    public ResponseEntity syncPilotos(){

        pilotoSyncService.syncPilotos();
        return ResponseEntity.ok().build();
    }
}
