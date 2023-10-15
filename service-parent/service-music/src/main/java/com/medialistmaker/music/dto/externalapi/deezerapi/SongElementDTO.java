package com.medialistmaker.music.dto.externalapi.deezerapi;

import lombok.Data;

@Data
public class SongElementDTO {

    private String id;

    private String title;

    private String duration;

    private String preview;

    private ArtistElementDTO artist;

}
