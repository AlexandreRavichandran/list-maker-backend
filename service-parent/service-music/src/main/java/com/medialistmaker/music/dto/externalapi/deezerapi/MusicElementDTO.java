package com.medialistmaker.music.dto.externalapi.deezerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MusicElementDTO {

    protected String apiCode;

    @JsonProperty("apiCode")
    public String getApiCode() {
        return apiCode;
    }

    @JsonProperty("id")
    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

}
