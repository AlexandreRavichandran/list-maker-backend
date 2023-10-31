package com.medialistmaker.music.dto.externalapi.deezerapi;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class SongElementDTO extends MusicElementDTO {

    private String title;

    private String duration;

    private String preview;

    private ArtistElementDTO artist;

}
