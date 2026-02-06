package com.cengo.muzayedebackendv2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping
    public ResponseEntity<Object> healthCheck() {
        return ResponseEntity.ok().build();
    }
}
