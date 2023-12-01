package com.medialistmaker.music.dto.externalapi.deezerapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class GenreListDTO {

    private List<GenreDTO> genreList;

    @JsonProperty("genreList")
    public List<GenreDTO> getGenreList() {
        return genreList;
    }

    @JsonProperty("data")
    public void setGenreList(List<GenreDTO> genreList) {
        this.genreList = genreList;
    }
}
