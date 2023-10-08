package com.medialistmaker.movie.dto.externalapi.omdbapi.collection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MovieElementListItemDTO {

    private String title;

    private String releasedAt;

    private String apiCode;

    private String pictureUrl;

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("Title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("releasedAt")
    public String getReleasedAt() {
        return releasedAt;
    }

    @JsonProperty("Year")
    public void setReleasedAt(String releasedAt) {
        this.releasedAt = releasedAt;
    }

    @JsonProperty("apiCode")
    public String getApiCode() {
        return apiCode;
    }

    @JsonProperty("imdbID")
    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

    @JsonProperty("pictureUrl")
    public String getPictureUrl() {
        return pictureUrl;
    }

    @JsonProperty("Poster")
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
