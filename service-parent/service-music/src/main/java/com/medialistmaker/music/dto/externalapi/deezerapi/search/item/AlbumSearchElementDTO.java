package com.medialistmaker.music.dto.externalapi.deezerapi.search.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import lombok.Data;

@Data
public class AlbumSearchElementDTO {

    private String apiCode;

    private String title;

    private String pictureUrl;

    private ArtistElementDTO artist;

    @JsonProperty("apiCode")
    public String getApiCode() {
        return apiCode;
    }

    @JsonProperty("id")
    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    @JsonProperty("pictureUrl")
    public String getPictureUrl() {
        return pictureUrl;
    }

    @JsonProperty("cover_xl")
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
