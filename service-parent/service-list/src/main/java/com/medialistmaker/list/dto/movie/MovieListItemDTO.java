package com.medialistmaker.list.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class MovieListItemDTO {

    public MovieListItemDTO() {
        //Private constructor
    }

    private Long id;

    private Long movieId;

    private Long appUserId;

    private Integer sortingOrder;

    private Date addedAt;
}
