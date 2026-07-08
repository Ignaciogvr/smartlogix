package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.response.TrackingResponse;
import com.smartlogix.bff.service.EnvioBffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    private final EnvioBffService envioService;

    public TrackingController(EnvioBffService envioService) {
        this.envioService = envioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackingResponse> tracking(@PathVariable Long id) {
        return ResponseEntity.ok(envioService.obtenerTracking(String.valueOf(id)));
    }

    @GetMapping("/codigo/{trackingCode}")
    public ResponseEntity<TrackingResponse> trackingPorCodigo(@PathVariable String trackingCode) {
        return ResponseEntity.ok(envioService.obtenerTracking(trackingCode));
    }
}
