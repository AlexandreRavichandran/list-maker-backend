package com.medialistmaker.music.dto.externalapi.deezerapi.search.list;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public abstract class AbstractSearchListDTO<T> {

    protected List<T> searchResults;

    protected Integer totalResults;

    @JsonProperty("searchResults")
    public List<T> getSearchResults() {
        return searchResults;
    }

    @JsonProperty("data")
    public void setSearchResults(List<T> searchResults) {
        this.searchResults = searchResults;
    }

    @JsonProperty("totalResults")
    public Integer getTotalResults() {
        return totalResults;
    }

    @JsonProperty("total")
    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }
}
