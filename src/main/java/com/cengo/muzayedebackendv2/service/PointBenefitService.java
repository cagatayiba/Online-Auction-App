package com.cengo.muzayedebackendv2.service;

import com.cengo.muzayedebackendv2.domain.entity.PointBenefit;
import com.cengo.muzayedebackendv2.domain.enums.PointBenefitAction;
import com.cengo.muzayedebackendv2.domain.request.PointBenefitUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.admin.PointBenefitActionAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.admin.PointBenefitAdminResponse;
import com.cengo.muzayedebackendv2.domain.response.point.PointEarningOptionsResponse;
import com.cengo.muzayedebackendv2.mapper.PointBenefitActionMapper;
import com.cengo.muzayedebackendv2.mapper.PointBenefitMapper;
import com.cengo.muzayedebackendv2.repository.PointBenefitRepository;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PointBenefitService extends BaseEntityService<PointBenefit, PointBenefitRepository> {

    private final UserService userService;
    private final PointBenefitMapper pointBenefitMapper;
    private final PointBenefitActionMapper pointBenefitActionMapper;

    protected PointBenefitService(PointBenefitRepository repository, UserService userService, PointBenefitMapper pointBenefitMapper, PointBenefitActionMapper pointBenefitActionMapper) {
        super(repository);
        this.userService = userService;
        this.pointBenefitMapper = pointBenefitMapper;
        this.pointBenefitActionMapper = pointBenefitActionMapper;
    }

    public List<PointBenefitAdminResponse> getAllAdminResponse(){
        return getRepository().findAll().stream().map(pointBenefitMapper::convertToPointBenefitAdminResponse).toList();
    }

    public PointBenefitAdminResponse update(PointBenefitUpdateRequest pointBenefitUpdateRequest){
        var updatedEntity = save(pointBenefitMapper.convertToPointBenefit(pointBenefitUpdateRequest));
        return pointBenefitMapper.convertToPointBenefitAdminResponse(updatedEntity);
    }

    public List<PointBenefitActionAdminResponse> getActions() {
        return Arrays.stream(PointBenefitAction.values())
                .map(pointBenefitActionMapper::convertToResponse)
                .toList();
    }

    public PointBenefitAdminResponse getAdminResponseByAction(PointBenefitAction action){
        return pointBenefitMapper.convertToPointBenefitAdminResponse(getByAction(action));
    }

    public PointEarningOptionsResponse getEarningOptions(){
        return new PointEarningOptionsResponse(
              findAllActive().stream().map(pointBenefitMapper::convertPointEarningOptionDescription).toList()
        );
    }

    @Async
    public void notifyAction(UUID userId, PointBenefitAction action){
        findByAction(action).ifPresent( matchedPointBenefit ->
                        userService.loadPoint(userId, matchedPointBenefit.getAmount())
        );
    }

    private List<PointBenefit> findAllActive(){
        return getRepository().findAllByIsActiveTrue();
    }

    private PointBenefit getByAction(PointBenefitAction action){
        // TODO DÜZGÜN EXCEPTİON AT
        return findByAction(action).orElseThrow(RuntimeException::new);
    }

    private Optional<PointBenefit> findByAction(PointBenefitAction action){
        return getRepository().findByActionAndIsActiveTrue(action);
    }

}
