package com.cengo.muzayedebackendv2.controller.admin;

import com.cengo.muzayedebackendv2.domain.enums.PointBenefitAction;
import com.cengo.muzayedebackendv2.domain.request.PointBenefitUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.admin.PointBenefitActionAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.PointBenefitAdminResponse;
import com.cengo.muzayedebackendv2.security.permission.AdminRole;
import com.cengo.muzayedebackendv2.service.PointBenefitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AdminRole
@RestController
@RequestMapping("/api/v1/admin/point-benefits")
@RequiredArgsConstructor
public class PointBenefitAdminController {

    private final PointBenefitService pointBenefitService;

    @GetMapping("/actions")
    public ResponseEntity<List<PointBenefitActionAdminResponse>> getPointBenefitActions(){
        return ResponseEntity.ok(pointBenefitService.getActions());
    }

    @GetMapping("/by-action/{action}")
    public ResponseEntity<PointBenefitAdminResponse> getByAction(@PathVariable PointBenefitAction action){
        return ResponseEntity.ok(pointBenefitService.getAdminResponseByAction(action));
    }

    @GetMapping
    public ResponseEntity<List<PointBenefitAdminResponse>> getAll(){
        return ResponseEntity.ok(pointBenefitService.getAllAdminResponse());
    }

    @PutMapping
    public ResponseEntity<PointBenefitAdminResponse> update(@RequestBody PointBenefitUpdateRequest pointBenefitUpdateRequest){
        return ResponseEntity.ok(pointBenefitService.update(pointBenefitUpdateRequest));
    }
}
