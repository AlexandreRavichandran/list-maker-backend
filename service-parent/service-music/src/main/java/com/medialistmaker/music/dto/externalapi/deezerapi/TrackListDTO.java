package com.medialistmaker.music.dto.externalapi.deezerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TrackListDTO {

    @JsonProperty("data")
    private List<SongElementDTO> songs;

}
