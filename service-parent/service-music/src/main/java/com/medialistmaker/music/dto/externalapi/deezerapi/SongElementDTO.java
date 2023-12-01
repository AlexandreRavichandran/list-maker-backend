package com.medialistmaker.music.dto.externalapi.deezerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class SongElementDTO extends MusicElementDTO {

    private String title;

    private Integer duration;

    private Integer rank;

    private Integer trackNumber;

    private String preview;

    private ArtistElementDTO artist;

    @JsonProperty("track_position")
    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }
}
