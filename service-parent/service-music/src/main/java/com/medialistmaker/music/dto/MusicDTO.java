package com.medialistmaker.music.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MusicDTO {

    public MusicDTO() {
        //Private constructor
    }

    private Long id;

    private String title;

    private String artistName;

    private Integer releasedAt;

    private String pictureUrl;

    private String apiCode;

    private Integer type;

}
