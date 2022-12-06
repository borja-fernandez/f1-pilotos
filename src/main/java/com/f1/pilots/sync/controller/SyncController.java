package com.f1.pilots.sync.controller;

import com.f1.pilots.PilotsApplication;
import com.f1.pilots.sync.service.PilotSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SyncController {
    private static final String API_VERSION = "/v1";
    private static final String API_PATH = PilotsApplication.PILOT_SYNC_PATH + API_VERSION;

    @Autowired
    PilotSyncService pilotSyncService;

    @PatchMapping(value = API_PATH)
    public ResponseEntity syncPilotos(){

        pilotSyncService.syncPilotos();
        return ResponseEntity.ok().build();
    }
}
