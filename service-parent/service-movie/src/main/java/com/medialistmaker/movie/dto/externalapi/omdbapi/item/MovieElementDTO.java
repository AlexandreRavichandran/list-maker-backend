package com.medialistmaker.movie.dto.externalapi.omdbapi.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class MovieElementDTO {

    private String apiCode;

    private String title;

    private String releasedAt;

    private String duration;

    private List<String> genreList;

    private String mainActors;

    private String synopsis;

    private String director;

    private List<RatingDTO> ratings;

    private String pictureUrl;

    @JsonProperty("apiCode")
    public String getApiCode() {
        return apiCode;
    }

    @JsonProperty("imdbID")
    public void setApiCode(String apiCode) {
        this.apiCode = apiCode;
    }

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

    @JsonProperty("duration")
    public String getDuration() {
        return duration;
    }

    @JsonProperty("Runtime")
    public void setDuration(String duration) {
        this.duration = duration;
    }

    @JsonProperty("synopsis")
    public String getSynopsis() {
        return synopsis;
    }

    @JsonProperty("Plot")
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    @JsonProperty("director")
    public String getDirector() {
        return director;
    }

    @JsonProperty("Director")
    public void setDirector(String director) {
        this.director = director;
    }

    @JsonProperty("ratings")
    public List<RatingDTO> getRatings() {
        return ratings;
    }

    @JsonProperty("Ratings")
    public void setRatings(List<RatingDTO> ratings) {
        this.ratings = ratings;
    }

    @JsonProperty("pictureUrl")
    public String getPictureUrl() {
        return pictureUrl;
    }

    @JsonProperty("Poster")
    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @JsonProperty("Genre")
    public void setGenre(String genre) {
        this.genreList =  Arrays.stream(genre.split(", ")).toList();
    }

    @JsonProperty("mainActors")
    public String getMainActors() {
        return mainActors;
    }

    @JsonProperty("Actors")
    public void setMainActors(String mainActors) {
        this.mainActors = mainActors;
    }
}
