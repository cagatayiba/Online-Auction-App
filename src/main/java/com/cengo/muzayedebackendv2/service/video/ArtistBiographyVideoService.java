package com.cengo.muzayedebackendv2.service.video;

import com.cengo.muzayedebackendv2.domain.dto.SaveVideoDTO;
import com.cengo.muzayedebackendv2.domain.entity.video.ArtistBiographyVideo;
import com.cengo.muzayedebackendv2.domain.response.artist.ArtistVideoResponse;
import com.cengo.muzayedebackendv2.mapper.ArtistBiographyVideoMapper;
import com.cengo.muzayedebackendv2.repository.ArtistBiographyVideoRepository;
import com.cengo.muzayedebackendv2.service.base.BaseEntityService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Validated
public class ArtistBiographyVideoService extends BaseEntityService<ArtistBiographyVideo, ArtistBiographyVideoRepository> {

    private final ArtistBiographyVideoMapper artistBiographyVideoMapper;

    protected ArtistBiographyVideoService(ArtistBiographyVideoRepository repository, ArtistBiographyVideoMapper artistBiographyVideoMapper) {
        super(repository);
        this.artistBiographyVideoMapper = artistBiographyVideoMapper;
    }

    @Transactional
    public List<ArtistVideoResponse> saveVideos(@Valid List<SaveVideoDTO> saveRequests) {
         List<ArtistBiographyVideo> videos = saveAll(artistBiographyVideoMapper.convertToVideos(saveRequests));
         return artistBiographyVideoMapper.convertToVideoResponses(saveAll(videos));
    }


    public List<ArtistVideoResponse> getVideosByArtistId(UUID artistId) {
        return artistBiographyVideoMapper.convertToVideoResponses(getRepository().findAllByArtistIdOrderByOrder(artistId));
    }

    public void deleteVideosByArtistId(UUID artistId) {
        getRepository().findAllByArtistIdOrderByOrder(artistId).forEach(this::delete);
    }
}
