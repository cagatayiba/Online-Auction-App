package com.cengo.muzayedebackendv2.controller;

import com.cengo.muzayedebackendv2.domain.response.point.PointEarningOptionsResponse;
import com.cengo.muzayedebackendv2.security.permission.UserRole;
import com.cengo.muzayedebackendv2.service.PointBenefitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@UserRole
@RestController
@RequestMapping("/api/v1/point-benefits")
@RequiredArgsConstructor
public class PointBenefitController {

    private final PointBenefitService pointBenefitService;

    @GetMapping("/earning-options")
    public ResponseEntity<PointEarningOptionsResponse> getEarningOptions(){
        return ResponseEntity.ok(pointBenefitService.getEarningOptions());
    }
}
