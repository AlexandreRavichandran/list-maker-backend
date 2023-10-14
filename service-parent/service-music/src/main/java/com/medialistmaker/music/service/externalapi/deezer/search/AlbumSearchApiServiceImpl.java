package com.medialistmaker.music.service.externalapi.deezer.search;

import com.medialistmaker.music.dto.externalapi.deezerapi.search.list.AlbumSearchListDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AlbumSearchApiServiceImpl extends AbstractSearchApiService<AlbumSearchListDTO> {

    @Override
    public String getSubResource() {
        return "album";
    }

    @Override
    protected Class<AlbumSearchListDTO> getItemClassType() {
        return AlbumSearchListDTO.class;
    }

    public AlbumSearchListDTO getByAlbumName(String albumName) throws CustomBadRequestException {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("q", albumName);

        this.setParameters(parameters);

        return this.executeRequestItem();

    }
}
