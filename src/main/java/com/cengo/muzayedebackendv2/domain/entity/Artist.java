package com.cengo.muzayedebackendv2.domain.entity;

import com.cengo.muzayedebackendv2.domain.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "artist")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Artist extends BaseEntity {

    @NotBlank
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Lob
    @NotNull
    @Column(name = "summary", nullable = false)
    private byte[] summary;

    @Lob
    @NotNull
    @Column(name = "biography", nullable = false)
    private byte[] biography;
}
