package com.medialistmaker.music.dto.externalapi.deezerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ArtistElementDTO {

    private String apiCode;

    private String name;

    private String pictureUrl;

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

    @JsonProperty("picture_xl")
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
