package com.medialistmaker.music.dto.externalapi.deezerapi.search.list;

import lombok.Data;

import java.util.List;

@Data
public abstract class AbstractSearchListDTO<T> {

    protected List<T> data;

}
