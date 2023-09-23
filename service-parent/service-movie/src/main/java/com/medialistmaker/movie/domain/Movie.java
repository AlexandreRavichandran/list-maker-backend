package com.medialistmaker.movie.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class Movie {

    public Movie() {
        //Private constructor
    }

    @Id
    @SequenceGenerator(name = "movie_id_sequence", sequenceName = "movie_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_id_sequence")
    private Long id;

    @NotBlank(message = "Movie name is mandatory")
    private String title;

    @NotBlank(message = "Movie api code is mandatory")
    private String apiCode;

    @NotBlank(message = "Movie picture url is mandatory")
    private String pictureUrl;

    @NotNull(message = "Movie release date is mandatory")
    private Integer releasedAt;
}
