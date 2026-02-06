package com.cengo.muzayedebackendv2.domain.entity;

import com.cengo.muzayedebackendv2.domain.entity.base.BaseEntity;
import com.cengo.muzayedebackendv2.domain.enums.PointBenefitAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "point_benefit")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PointBenefit extends BaseEntity {

    @Column(name = "action", unique = true)
    @Enumerated(EnumType.STRING)
    private PointBenefitAction action;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "isActive")
    private Boolean isActive;
}
