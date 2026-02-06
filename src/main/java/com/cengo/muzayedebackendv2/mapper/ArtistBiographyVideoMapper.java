package com.cengo.muzayedebackendv2.mapper;

import com.cengo.muzayedebackendv2.domain.dto.SaveVideoDTO;
import com.cengo.muzayedebackendv2.domain.entity.video.ArtistBiographyVideo;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistVideoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ArtistBiographyVideoMapper {
    List<ArtistBiographyVideo> convertToVideos(List<SaveVideoDTO> saveRequests);

    List<ArtistVideoResponse> convertToVideoResponses(List<ArtistBiographyVideo> artistBiographyVideo);
}
