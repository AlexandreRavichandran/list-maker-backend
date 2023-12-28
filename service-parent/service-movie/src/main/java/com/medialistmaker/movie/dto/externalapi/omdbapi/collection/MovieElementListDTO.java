package com.medialistmaker.movie.dto.externalapi.omdbapi.collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class MovieElementListDTO {

    private List<MovieElementListItemDTO> searchResults;

    private Integer totalResults;

    @JsonProperty("searchResults")
    public List<MovieElementListItemDTO> getSearchResults() {
        return searchResults;
    }

    @JsonProperty("Search")
    public void setSearchResults(List<MovieElementListItemDTO> searchResults) {
        this.searchResults = searchResults;
    }

}
