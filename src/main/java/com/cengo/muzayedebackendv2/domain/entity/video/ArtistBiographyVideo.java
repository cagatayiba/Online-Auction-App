package com.cengo.muzayedebackendv2.domain.entity.video;

import com.cengo.muzayedebackendv2.domain.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "artist_biography_video")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArtistBiographyVideo extends BaseEntity {

    @NotNull
    @Column(name = "artist_id", nullable = false)
    private UUID artistId;

    @NotBlank
    @Column(name = "header", nullable = false)
    private String header;

    @NotBlank
    @Column(name = "url", nullable = false)
    private String url;

    @NotNull
    @Column(name = "order_no", nullable = false)
    private Integer order;
}
