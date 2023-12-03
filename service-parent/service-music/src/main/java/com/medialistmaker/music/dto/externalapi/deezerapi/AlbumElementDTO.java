package com.medialistmaker.music.dto.externalapi.deezerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumElementDTO extends MusicElementDTO {

    private String title;

    private String pictureUrl;

    private String releaseDate;

    private ArtistElementDTO artist;

    private List<GenreDTO> genreList;

    private Boolean isAlreadyInList;

    @JsonProperty("pictureUrl")
    public String getPictureUrl() {
        return pictureUrl;
    }

    @JsonProperty("releaseDate")
    public String getReleaseDate() {
        return releaseDate;
    }

    @JsonProperty("cover_xl")
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @JsonProperty("release_date")
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @JsonProperty("genreList")
    public List<GenreDTO> getGenreList() {
        return genreList;
    }

    @JsonProperty("genres")
    public void setGenreList(Map<String, List<GenreDTO>> genres) {
        this.genreList = genres.get("data");
    }

    @Override
    @JsonProperty("apiCode")
    public String getApiCode() {
        return apiCode;
    }

    @Override
    @JsonProperty("id")
    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }
}
