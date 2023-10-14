package com.medialistmaker.music.dto.externalapi.deezerapi.search.item;

import com.medialistmaker.music.dto.externalapi.deezerapi.ArtistElementDTO;
import lombok.Data;

@Data
public class SongSearchElementDTO {

    private String id;

    private String title;

    private String duration;

    private String preview;

    private ArtistElementDTO artist;

}
