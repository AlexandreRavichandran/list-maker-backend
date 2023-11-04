package com.medialistmaker.movie.dto;

import lombok.Data;

@Data
public class MovieDTO {

    public MovieDTO() {
        //Private constructor
    }

    private Long id;

    private String title;

    private String apiCode;

    private String pictureUrl;

    private Integer releasedAt;
}
