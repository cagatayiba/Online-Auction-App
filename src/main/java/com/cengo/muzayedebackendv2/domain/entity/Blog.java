package com.cengo.muzayedebackendv2.domain.entity;

import com.cengo.muzayedebackendv2.domain.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "blog")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Blog extends BaseEntity {

    @Lob
    @NotNull
    @Column(name = "content", nullable = false)
    private byte[] content;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "keywords", nullable = false)
    private String keywords;

    @NotBlank
    @Column(name = "meta_description", nullable = false)
    private String metaDescription;

    @NotBlank
    @Column(name = "page_title", nullable = false)
    private String pageTitle;
}
