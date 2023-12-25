package com.medialistmaker.music.dto.externalapi.deezerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TrackListDTO {

    private List<SongElementDTO> songList;

    private Long totalDurationInEpochMilli;

    private Double albumPopularityRate;

    @JsonProperty("songList")
    public List<SongElementDTO> getSongList() {
        return songList;
    }

    @JsonProperty("data")
    public void setSongList(List<SongElementDTO> songList) {
        this.songList = songList;
    }
}
