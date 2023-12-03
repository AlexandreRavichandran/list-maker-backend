package com.medialistmaker.music.dto.externalapi.deezerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumElementDTO extends MusicElementDTO {

    private String title;

    private String pictureUrl;

    private String releaseDate;

    private ArtistElementDTO artist;

    private GenreListDTO genreList;

    private Boolean isAlreadyInList;

    public String getPictureUrl() {
        return pictureUrl;
    }

    @JsonProperty("cover_xl")
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @JsonProperty("release_date")
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @JsonProperty("genres")
    public void setGenreList(GenreListDTO genreList) {
        this.genreList = genreList;
    }

    @Override
    @JsonProperty("id")
    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }
}
