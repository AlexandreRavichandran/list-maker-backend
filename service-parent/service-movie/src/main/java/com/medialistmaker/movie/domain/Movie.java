package com.medialistmaker.movie.domain;

import jakarta.persistence.*;
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

    private String title;

    private String apiCode;

    private String pictureUrl;

    private Integer releasedAt;
}
