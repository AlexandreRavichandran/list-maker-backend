package com.medialistmaker.music.dto.externalapi.deezerapi;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AlbumListDTO {

    private List<AlbumElementDTO> albumList;

    @JsonProperty("albumList")
    public List<AlbumElementDTO> getAlbumList() {
        return albumList;
    }

    @JsonProperty("data")
    public void setAlbumList(List<AlbumElementDTO> albumList) {
        this.albumList = albumList;
    }
}
