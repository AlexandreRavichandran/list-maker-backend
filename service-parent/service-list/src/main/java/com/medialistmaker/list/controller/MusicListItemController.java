package com.medialistmaker.list.controller;

import com.medialistmaker.list.dto.music.MusicListItemAddDTO;
import com.medialistmaker.list.dto.music.MusicListItemDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.list.service.musiclistitem.MusicListItemServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/lists/musics")
public class MusicListItemController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    MusicListItemServiceImpl musicListService;

    @GetMapping
    public ResponseEntity<List<MusicListItemDTO>> getByAppUserId() {

        return new ResponseEntity<>(
                this.musicListService
                        .getByAppUserId(1L)
                        .stream()
                        .map(listItem -> this.modelMapper.map(listItem, MusicListItemDTO.class))
                        .toList(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<MusicListItemDTO> add(@RequestBody MusicListItemAddDTO musicListItemDTO)
            throws CustomBadRequestException, CustomEntityDuplicationException, ServiceNotAvailableException {


        return new ResponseEntity<>(
                this.modelMapper.map(this.musicListService.add(musicListItemDTO), MusicListItemDTO.class),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{listItemId}")
    public ResponseEntity<MusicListItemDTO> deleteById(@PathVariable("listItemId") Long listItemId)
            throws CustomNotFoundException {

        return new ResponseEntity<>(
                this.modelMapper.map(this.musicListService.deleteById(listItemId), MusicListItemDTO.class),
                HttpStatus.OK
        );
    }

}
