package com.medialistmaker.music.service.externalapi.deezer;

import com.medialistmaker.music.dto.externalapi.deezerapi.SongElementDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.service.externalapi.AbstractExternalApiService;
import org.springframework.stereotype.Service;

@Service
public class SongApiServiceImpl extends AbstractExternalApiService<SongElementDTO> {

    private String subResource;

    @Override
    protected String getBaseUrl() {
        return "https://api.deezer.com/";
    }

    @Override
    protected String getResourceUrl() {
        return "track/" + this.getSubResource();
    }

    @Override
    protected Class<SongElementDTO> getItemClassType() {
        return SongElementDTO.class;
    }

    public SongElementDTO getByApiCode(String apiCode) throws CustomBadRequestException {

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
