package com.medialistmaker.music.dto.externalapi.deezerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumElementDTO extends MusicElementDTO {

    private String title;

    private String pictureUrl;

    private String releasedAt;

    private ArtistElementDTO artist;

    private List<GenreDTO> genreList;

    private Boolean isAlreadyInList;

    @JsonProperty("pictureUrl")
    public String getPictureUrl() {
        return pictureUrl;
    }

    @JsonProperty("releasedAt")
    public String getReleasedAt() {
        return releasedAt;
    }

    @JsonProperty("cover_xl")
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @JsonProperty("release_date")
    public void setReleasedAt(String releasedAt) {
        LocalDate date = LocalDate.parse(releasedAt);
        this.releasedAt = String.valueOf(date.getYear());
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
