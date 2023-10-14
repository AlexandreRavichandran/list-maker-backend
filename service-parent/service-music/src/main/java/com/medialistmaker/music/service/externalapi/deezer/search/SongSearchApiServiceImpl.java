package com.medialistmaker.music.service.externalapi.deezer.search;

import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.SongSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SongSearchApiServiceImpl extends AbstractSearchApiService<SongSearchListDTO> {

    @Override
    protected String getSubResource() {
        return "track";
    }

    @Override
    protected Class<SongSearchListDTO> getItemClassType() {
        return SongSearchListDTO.class;
    }

    public SongSearchListDTO getBySongName(String songName) throws CustomBadRequestException {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("q", songName);

        this.setParameters(parameters);

        return this.executeRequestItem();
    }
}
