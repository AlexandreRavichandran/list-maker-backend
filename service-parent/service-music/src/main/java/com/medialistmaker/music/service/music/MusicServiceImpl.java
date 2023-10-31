package com.medialistmaker.music.service.music;

import com.medialistmaker.music.connector.deezer.DeezerConnector;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Slf4j
public class MusicServiceImpl implements MusicService {

    @Autowired
    MusicRepository musicRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DeezerAlbumConnectorProxy deezerAlbumConnectorProxy;

    @Autowired
    DeezerSongConnectorProxy deezerSongConnectorProxy;

    @Autowired
    CustomEntityValidator<Music> musicEntityValidator;

    @Override
    public List<Music> browseByIds(List<Long> musicIds) {
        return this.musicRepository.getByIds(musicIds);
    }

    @Override
    public List<Music> browseByType(Integer type) {
        return this.musicRepository.getByType(type);
    }

    @Override
    public Music readById(Long musicId) throws CustomNotFoundException {

        Music music = this.musicRepository.getReferenceById(musicId);

        if(isNull(music)) {
            log.error("Music with id {} not found", musicId);
            throw new CustomNotFoundException("Not found");
        }

        return music;

    }

    @Override
    public Music readByApiCode(String apiCode) throws CustomNotFoundException {

        Music music = this.musicRepository.getByApiCode(apiCode);

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

        Music isMovieAlreadyExist = this.musicRepository.getByApiCode(apiCode);

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

        //TODO Find alternative
        Music music = this.modelMapper.map(musicElementDTO, Music.class);
        music.setApiCode(musicElementDTO.getId());
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

        Music music = this.musicRepository.getReferenceById(id);

        if(isNull(music)) {
            log.error("Error on deleting music with id {}: Music not exists", id);
            throw new CustomNotFoundException("Not found");
        }

        this.musicRepository.delete(music);

        return music;
    }

    private DeezerConnector getConnectorByType(Integer type) throws UnsupportedTypeException {

        DeezerConnector connector;

        connector = switch (type) {
            case 1 -> this.deezerAlbumConnectorProxy;
            case 2 -> this.deezerSongConnectorProxy;
            default -> throw new UnsupportedTypeException();
        };

        return connector;
    }

}
