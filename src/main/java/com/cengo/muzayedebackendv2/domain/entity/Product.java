package com.cengo.muzayedebackendv2.domain.entity;

import com.cengo.muzayedebackendv2.domain.entity.base.BaseEntity;
import com.cengo.muzayedebackendv2.domain.entity.enums.ProductState;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "product")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product extends BaseEntity {

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "description", nullable = false)
    private String description;

    @Positive
    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductState state = ProductState.ON_SALE;

    @NotBlank
    @Column(name = "category", nullable = false)
    private String category;

    @NotNull
    @Column(name = "artist_id", nullable = false)
    private UUID artistId;

    @Column(name = "requester_id")
    private UUID requesterId;

    @Column(name = "buyer_id")
    private UUID buyerId;
}
