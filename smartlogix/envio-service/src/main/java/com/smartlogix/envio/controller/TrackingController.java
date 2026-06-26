package com.smartlogix.envio.controller;

import com.smartlogix.envio.dto.response.TrackingResponse;
import com.smartlogix.envio.service.TrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    private final TrackingService trackingService;

    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackingResponse> track(@PathVariable String trackingId) {
        return ResponseEntity.ok(trackingService.obtenerTracking(trackingId));
    }
}