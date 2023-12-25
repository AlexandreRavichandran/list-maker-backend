package com.medialistmaker.music.controller;

import com.medialistmaker.music.dto.MusicAddDTO;
import com.medialistmaker.music.dto.MusicDTO;
import com.medialistmaker.music.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.music.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.music.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.music.service.music.MusicServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/musics")
public class MusicController {

    @Autowired
    MusicServiceImpl musicService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<List<MusicDTO>> browseByIds(@RequestParam("musicIds") List<Long> musicIds) {

        return new ResponseEntity<>(
                this.musicService.browseByIds(musicIds)
                        .stream()
                        .map(music -> this.modelMapper.map(music, MusicDTO.class))
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/types/{type}")
    public ResponseEntity<List<MusicDTO>> browseByType(@PathVariable("type") Integer type) {

        return new ResponseEntity<>(
                this.musicService.browseByType(type)
                        .stream()
                        .map(music -> this.modelMapper.map(music, MusicDTO.class))
                        .toList(),
                HttpStatus.OK
        );

    }

    @GetMapping("/{musicId}")
    public ResponseEntity<MusicDTO> readById(@PathVariable("musicId") Long musicId) throws CustomNotFoundException {

        return new ResponseEntity<>(
                this.modelMapper.map(this.musicService.readById(musicId), MusicDTO.class),
                HttpStatus.OK
        );
    }

    @GetMapping("/apicode/{apicode}")
    public ResponseEntity<MusicDTO> readByApiCode(@PathVariable("apicode") String apiCode, @RequestParam("type") Integer type)
            throws CustomNotFoundException {

        return new ResponseEntity<>(
                this.modelMapper.map(this.musicService.readByApiCodeAndType(apiCode, type), MusicDTO.class),
                HttpStatus.OK
        );

    }

    @PostMapping
    public ResponseEntity<MusicDTO> addFromApiCode(@RequestBody MusicAddDTO musicAddDTO)
            throws CustomBadRequestException, ServiceNotAvailableException {

        return new ResponseEntity<>(
                this.modelMapper.map(
                        this.musicService.addByApiCode(
                                musicAddDTO.getType(),
                                musicAddDTO.getApiCode()
                        ), MusicDTO.class),
                HttpStatus.CREATED
        );

    }

    @DeleteMapping("/{musicId}")
    public ResponseEntity<MusicDTO> deleteById(@PathVariable("musicId") Long musicId) throws CustomNotFoundException {

        return new ResponseEntity<>(
                this.modelMapper.map(this.musicService.deleteById(musicId), MusicDTO.class),
                HttpStatus.OK
        );

    }
}
