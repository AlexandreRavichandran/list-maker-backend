package com.medialistmaker.list.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
public class AppUserMovieListItem {

    public AppUserMovieListItem() {
        //Private constructor
    }

    @Id
    @SequenceGenerator(name = "movie_list_id_sequence", sequenceName = "movie_list_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_list_id_sequence")
    private Long id;

    @NotNull(message = "Movie id is mandatory")
    private Long movieId;

    @NotNull(message = "User id is mandatory")
    private Long appUserId;

    @NotNull(message = "Sorting order is mandatory")
    private Integer sortingOrder;

    private Date addedAt;
}
