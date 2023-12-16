package com.medialistmaker.music.service.music;

import com.medialistmaker.music.domain.Music;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;

import java.util.List;

public interface MusicService {

    List<Music> browseByIds(List<Long> musicIds);

    List<Music> browseByType(Integer type);

    Music readById(Long musicId) throws CustomNotFoundException;

    Music readByApiCodeAndType(String apiCode, Integer type) throws CustomNotFoundException;

    Music addByApiCode(Integer type, String apiCode) throws CustomBadRequestException, ServiceNotAvailableException;

    Music deleteById(Long id) throws CustomNotFoundException;
}
