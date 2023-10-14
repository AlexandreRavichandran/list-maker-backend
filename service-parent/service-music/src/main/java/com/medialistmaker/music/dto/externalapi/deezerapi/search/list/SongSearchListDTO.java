package com.medialistmaker.music.dto.externalapi.deezerapi.search.list;

import com.medialistmaker.music.dto.externalapi.deezerapi.search.item.SongSearchElementDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SongSearchListDTO extends AbstractSearchListDTO<SongSearchElementDTO> {
}
