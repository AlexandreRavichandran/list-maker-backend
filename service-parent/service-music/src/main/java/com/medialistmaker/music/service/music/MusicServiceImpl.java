package com.medialistmaker.music.service.music;

import com.medialistmaker.music.domain.Music;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.music.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.music.repository.MusicRepository;
import com.medialistmaker.music.utils.CustomEntityValidator;
import lombok.extern.slf4j.Slf4j;
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
    public Music add(Music music) throws CustomBadRequestException, CustomEntityDuplicationException {

        List<String> musicList = this.musicEntityValidator.validateEntity(music);

        if(Boolean.FALSE.equals(musicList.isEmpty())) {
            log.error("Music not valid: {}", musicList);
            throw new CustomBadRequestException("Bad request", musicList);
        }

        Music isApiCodeAlreadyUsed = this.musicRepository.getByApiCode(music.getApiCode());

        if(nonNull(isApiCodeAlreadyUsed)) {
            log.error("Music {} already exists", music.getApiCode());
            throw new CustomEntityDuplicationException("Already exists");
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
}
