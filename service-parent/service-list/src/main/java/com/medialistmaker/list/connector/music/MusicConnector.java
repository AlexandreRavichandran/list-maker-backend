package com.medialistmaker.list.connector.music;

import com.medialistmaker.list.dto.music.MusicAddDTO;
import com.medialistmaker.list.dto.music.MusicDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "music", path = "/api/musics")
public interface MusicConnector {

    @GetMapping("/apicodes/{apicode}")
    MusicDTO getMusicByApiCodeAndType(@PathVariable("apicode") String apiCode, @RequestParam("type") Integer type)
            throws CustomNotFoundException, ServiceNotAvailableException;

    @PostMapping
    MusicDTO saveByApiCode(@RequestBody MusicAddDTO musicAddDTO)
            throws CustomBadRequestException, ServiceNotAvailableException;

    @DeleteMapping("/{musicId}")
    MusicDTO deleteById(@PathVariable("musicId") Long id) throws CustomNotFoundException, ServiceNotAvailableException;
}
