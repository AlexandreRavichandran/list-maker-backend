package com.medialistmaker.music.domain;

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
public class Music {

    public Music() {
        //Private constructor
    }

    @Id
    @SequenceGenerator(name = "music_id_sequence", sequenceName = "music_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "music_id_sequence")
    private Long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotBlank(message = "Artist name is mandatory")
    private String artistName;

    @NotNull(message = "Release date is mandatory")
    private Integer releasedAt;

    @NotBlank(message = "Picture url is mandatory")
    private String pictureUrl;

    @NotBlank(message = "Api code is mandatory")
    private String apiCode;

    @NotNull(message = "Type is mandatory")
    private Integer type;
}
