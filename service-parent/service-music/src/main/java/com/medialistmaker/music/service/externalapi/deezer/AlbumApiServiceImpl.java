package com.medialistmaker.music.service.externalapi.deezer;

import com.medialistmaker.music.dto.externalapi.deezerapi.AlbumElementDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.service.externalapi.AbstractExternalApiService;
import org.springframework.stereotype.Service;

@Service
public class AlbumApiServiceImpl extends AbstractExternalApiService<AlbumElementDTO> {

    private String subResource;

    @Override
    protected String getBaseUrl() {
        return "https://api.deezer.com/";
    }

    @Override
    protected String getResourceUrl() {
        return "album/" + this.getSubResource();
    }

    @Override
    protected Class<AlbumElementDTO> getItemClassType() {
        return AlbumElementDTO.class;
    }

    public AlbumElementDTO getByApiCode(String apiCode) throws CustomBadRequestException {

        this.setSubResource(apiCode);
        return this.executeRequestItem();

    }

    private String getSubResource() {
        return this.subResource;
    }

    private void setSubResource(String subResource) {
        this.subResource = subResource;
    }
}
