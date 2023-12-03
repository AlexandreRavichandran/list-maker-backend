package com.medialistmaker.music.connector.list;

import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "music", path = "/api/lists/musics")
public interface ListConnector {

    @GetMapping("/{musicId}")
    Boolean isMusicIdAlreadyInList(@PathVariable("musicId") Long musicId) throws ServiceNotAvailableException;

}
