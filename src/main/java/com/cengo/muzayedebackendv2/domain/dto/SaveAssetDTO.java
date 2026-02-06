package com.cengo.muzayedebackendv2.domain.dto;

import com.cengo.muzayedebackendv2.domain.entity.enums.AssetDomainType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Builder
public record SaveAssetDTO(
        @NotNull UUID referenceId,
        @NotNull AssetDomainType domainType,
        MultipartFile file,
        Integer orderNo
) {
}
