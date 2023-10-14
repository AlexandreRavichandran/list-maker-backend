package com.medialistmaker.music.dto.externalapi.deezerapi.search.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import lombok.Data;

@Data
public class AlbumSearchElementDTO {

    private String id;

    private String title;

    private String pictureUrl;

    private ArtistElementDTO artist;

    public String getPictureUrl() {
        return pictureUrl;
    }

    @JsonProperty("cover_xl")
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
