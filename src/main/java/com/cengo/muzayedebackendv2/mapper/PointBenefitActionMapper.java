package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.enums.PointBenefitAction;
import com.cengo.muzayedebackendv2.domain.response.admin.PointBenefitActionAdminResponse;
import com.cengo.muzayedebackendv2.mapper.config.DefaultMapperConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = DefaultMapperConfiguration.class)
public interface PointBenefitActionMapper {

    //@Mapping(target = "action", source = "pointBenefitAction")
    @Mapping(target = "text", source = ".", qualifiedByName = "mapText")
    @Mapping(target = "description", source = ".", qualifiedByName = "mapDescription")
    PointBenefitActionAdminResponse convertToResponse(PointBenefitAction action);

    @Named("mapText")
    default String mapText(PointBenefitAction action){
        return action.getText();
    }

    @Named("mapDescription")
    default String mapDescription(PointBenefitAction action){
        return action.getDescription();
    }
}
