package com.cengo.muzayedebackendv2.domain.entity;

import com.cengo.muzayedebackendv2.domain.entity.base.BaseEntity;
import com.cengo.muzayedebackendv2.domain.entity.enums.AssetDomainType;
import com.cengo.muzayedebackendv2.domain.entity.enums.AssetMimeType;
import com.cengo.muzayedebackendv2.validation.annotation.CompatibleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "asset")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@CompatibleType
public class Asset extends BaseEntity {

    @NotNull
    @Column(name = "reference_id", nullable = false)
    private UUID referenceId;

    @NotNull
    @Column(name = "domain_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssetDomainType domainType;

    @Column(name = "mime_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AssetMimeType mimeType;

    @Column(name = "url")
    private String url;

    @Column(name = "alt")
    private String alt;

    @Column(name = "order_no")
    private Integer orderNo;
}
