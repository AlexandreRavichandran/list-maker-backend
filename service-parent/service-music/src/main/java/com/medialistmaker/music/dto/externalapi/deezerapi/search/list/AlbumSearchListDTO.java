package com.medialistmaker.music.dto.externalapi.deezerapi.search.list;

import com.medialistmaker.music.dto.externalapi.deezerapi.search.item.AlbumSearchElementDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumSearchListDTO extends AbstractSearchListDTO<AlbumSearchElementDTO> {
}
