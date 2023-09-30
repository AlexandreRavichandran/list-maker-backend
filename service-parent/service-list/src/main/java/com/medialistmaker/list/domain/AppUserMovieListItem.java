package com.medialistmaker.list.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class AppUserMovieListItem extends AppUserListItem {

    public AppUserMovieListItem() {
        //Private constructor
    }

    @Builder
    public AppUserMovieListItem(Long appUserId, Integer sortingOrder, Date addedAt, Long id, Long movieId) {
        super(appUserId, sortingOrder, addedAt);
        this.id = id;
        this.movieId = movieId;
    }

    @Id
    @SequenceGenerator(name = "movie_list_id_sequence", sequenceName = "movie_list_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_list_id_sequence")
    private Long id;

    @NotNull(message = "Movie id is mandatory")
    private Long movieId;

}
