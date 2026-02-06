package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.entity.PointBenefit;
import com.cengo.muzayedebackendv2.domain.request.PointBenefitUpdateRequest;
import com.cengo.muzayedebackendv2.domain.response.admin.PointBenefitAdminResponse;
import com.cengo.muzayedebackendv2.mapper.config.DefaultMapperConfiguration;
import org.mapstruct.Mapper;

import java.text.MessageFormat;

@Mapper(config = DefaultMapperConfiguration.class, uses = {PointBenefitActionMapper.class})
public interface PointBenefitMapper {

    PointBenefit convertToPointBenefit(PointBenefitUpdateRequest pointBenefitUpdateRequest);

    PointBenefitAdminResponse convertToPointBenefitAdminResponse(PointBenefit entity);

    default String convertPointEarningOptionDescription(PointBenefit entity){
        return MessageFormat.format(
                entity.getAction().getUserMessage(),
                entity.getAmount()
        );
    }
}
