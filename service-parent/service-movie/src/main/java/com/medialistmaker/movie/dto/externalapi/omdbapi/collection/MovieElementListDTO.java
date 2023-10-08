package com.medialistmaker.movie.dto.externalapi.omdbapi.collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MovieElementListDTO {

    private List<MovieElementListItemDTO> movieElementList;

    private String totalResults;

    private String responseStatus;

    @JsonProperty("movieElementList")
    public List<MovieElementListItemDTO> getMovieElementList() {
        return movieElementList;
    }

    @JsonProperty("responseStatus")
    public String getResponseStatus() {
        return responseStatus;
    }

    @JsonProperty("Search")
    public void setMovieElementList(List<MovieElementListItemDTO> movieElementList) {
        this.movieElementList = movieElementList;
    }

    @JsonProperty("Response")
    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }
}
