package com.medialistmaker.music.service.music;

import com.medialistmaker.music.connector.deezer.DeezerConnectorElement;
import com.medialistmaker.music.connector.deezer.album.DeezerAlbumConnectorProxy;
import com.medialistmaker.music.connector.deezer.song.DeezerSongConnectorProxy;
import com.medialistmaker.music.domain.Music;
import com.medialistmaker.music.dto.externalapi.deezerapi.MusicElementDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.music.exception.unsupportedtypeexception.UnsupportedTypeException;
import com.medialistmaker.music.repository.MusicRepository;
import com.medialistmaker.music.utils.CustomEntityValidator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Slf4j
public class MusicServiceImpl implements MusicService {

    private final MusicRepository musicRepository;

    private final ModelMapper modelMapper;

    private final DeezerAlbumConnectorProxy deezerAlbumConnectorProxy;

    private final DeezerSongConnectorProxy deezerSongConnectorProxy;

    private final CustomEntityValidator<Music> musicEntityValidator;

    public MusicServiceImpl(
            MusicRepository musicRepository,
            ModelMapper modelMapper,
            DeezerAlbumConnectorProxy deezerAlbumConnectorProxy,
            DeezerSongConnectorProxy deezerSongConnectorProxy,
            CustomEntityValidator<Music> musicEntityValidator
    ) {
        this.musicRepository = musicRepository;
        this.modelMapper = modelMapper;
        this.deezerAlbumConnectorProxy = deezerAlbumConnectorProxy;
        this.deezerSongConnectorProxy = deezerSongConnectorProxy;
        this.musicEntityValidator = musicEntityValidator;
    }

    @Override
    public List<Music> browseByIds(List<Long> musicIds) {

        List<Music> musicList = this.musicRepository.getByIds(musicIds);

        musicList.sort(Comparator.comparingInt(music -> musicIds.indexOf(music.getId())));

        return musicList;
    }

    @Override
    public List<Music> browseByType(Integer type) {
        return this.musicRepository.getByType(type);
    }

    @Override
    public Music readById(Long musicId) throws CustomNotFoundException {

        Optional<Music> music = this.musicRepository.findById(musicId);

        if(music.isEmpty()) {
            log.error("Music with id {} not found", musicId);
            throw new CustomNotFoundException("Not found");
        }

        return music.get();

    }

    @Override
    public Music readByApiCodeAndType(String apiCode, Integer type) throws CustomNotFoundException {

        Music music = this.musicRepository.getByApiCodeAndType(apiCode, type);

        if(isNull(music)) {
            log.error("Music with api code {} not found", apiCode);
            throw new CustomNotFoundException("Not found");
        }

        return music;

    }


    @Override
    public Music addByApiCode(Integer type, String apiCode)
            throws CustomBadRequestException, ServiceNotAvailableException {
        MusicElementDTO musicElementDTO;

        Music isMovieAlreadyExist = this.musicRepository.getByApiCodeAndType(apiCode, type);

        if(nonNull(isMovieAlreadyExist)) {
            return isMovieAlreadyExist;
        }

        try {
            musicElementDTO = this.getConnectorByType(type).getByApiCode(apiCode);
        } catch (UnsupportedTypeException e) {
            throw new CustomBadRequestException(e.getMessage());
        }

        if(isNull(musicElementDTO)) {
            throw new CustomBadRequestException("Music not exists");
        }

        Music music = this.modelMapper.map(musicElementDTO, Music.class);
        music.setApiCode(musicElementDTO.getApiCode());
        music.setId(null);
        music.setType(type);

        return this.add(music);
    }

    private Music add(Music music) throws CustomBadRequestException  {

        List<String> musicList = this.musicEntityValidator.validateEntity(music);

        if(Boolean.FALSE.equals(musicList.isEmpty())) {
            log.error("Music not valid: {}", musicList);
            throw new CustomBadRequestException("Bad request", musicList);
        }

        return this.musicRepository.save(music);
    }

    @Override
    public Music deleteById(Long id) throws CustomNotFoundException {

        Optional<Music> music = this.musicRepository.findById(id);

        if(music.isEmpty()) {
            log.error("Error on deleting music with id {}: Music not exists", id);
            throw new CustomNotFoundException("Not found");
        }

        this.musicRepository.delete(music.get());

        return music.get();
    }

    private DeezerConnectorElement getConnectorByType(Integer type) throws UnsupportedTypeException {

        DeezerConnectorElement connector;

        connector = switch (type) {
            case 1 -> this.deezerAlbumConnectorProxy;
            case 2 -> this.deezerSongConnectorProxy;
            default -> throw new UnsupportedTypeException();
        };

        return connector;
    }

}
