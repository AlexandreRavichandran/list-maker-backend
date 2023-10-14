package com.medialistmaker.music.service.externalapi.deezer.search;

import com.medialistmaker.music.service.externalapi.AbstractExternalApiService;

public abstract class AbstractSearchApiService<T> extends AbstractExternalApiService<T> {

    @Override
    protected String getBaseUrl() {
        return "https://api.deezer.com";
    }

    @Override
    protected String getResourceUrl() {
        return "/search/" + this.getSubResource();
    }

    protected abstract String getSubResource();

    @Override
    protected abstract Class<T> getItemClassType();

}
