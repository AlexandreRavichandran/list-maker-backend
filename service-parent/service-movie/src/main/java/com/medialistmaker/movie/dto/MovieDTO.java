package com.medialistmaker.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
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
