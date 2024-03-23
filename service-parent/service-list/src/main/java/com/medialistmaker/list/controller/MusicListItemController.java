package com.medialistmaker.list.controller;

import com.medialistmaker.list.domain.MusicListItem;
import com.medialistmaker.list.dto.music.MusicListItemAddDTO;
import com.medialistmaker.list.dto.music.MusicListItemDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.list.service.musiclistitem.MusicListItemServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Objects.isNull;

@RestController
@RequestMapping("api/lists/musics")
public class MusicListItemController extends AbstractController {

    private final ModelMapper modelMapper;

    private final MusicListItemServiceImpl musicListService;

    public MusicListItemController(
            ModelMapper modelMapper,
            MusicListItemServiceImpl musicListService
    ) {
        this.modelMapper = modelMapper;
        this.musicListService = musicListService;
    }

    @GetMapping
    public ResponseEntity<List<MusicListItemDTO>> getByAppUserId() {

        return new ResponseEntity<>(
                this.musicListService
                        .getByAppUserId(this.getCurrentLoggedAppUserId())
                        .stream()
                        .map(listItem -> this.modelMapper.map(listItem, MusicListItemDTO.class))
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/random")
    public ResponseEntity<MusicListItemDTO> getRandomInAppUserList() {

        MusicListItem randomMusicLisItem = this.musicListService.getRandomInAppUserList(this.getCurrentLoggedAppUserId());

        if(isNull(randomMusicLisItem)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                this.modelMapper.map(randomMusicLisItem, MusicListItemDTO.class),
                HttpStatus.OK
        );

    }

    @GetMapping("/latest")
    public ResponseEntity<List<MusicListItemDTO>> getLatestAddedByAppUserId() {

        return new ResponseEntity<>(
                this.musicListService
                        .getLatestAddedByAppUserId(this.getCurrentLoggedAppUserId())
                        .stream()
                        .map(listItem -> this.modelMapper.map(listItem, MusicListItemDTO.class))
                        .toList(),
                HttpStatus.OK
        );
    }

    @GetMapping("/apicode/{apicode}")
    public ResponseEntity<Boolean> isMusicAlreadyInAppUserList(@PathVariable("apicode") String apiCode, @RequestParam("type") Integer type)
            throws ServiceNotAvailableException {

        return new ResponseEntity<>(
                this.musicListService.isMusicApiCodeAndTypeAlreadyInAppUserMovieList(this.getCurrentLoggedAppUserId(), apiCode, type),
                HttpStatus.OK
        );

    }

    @PutMapping("/{listItemId}")
    public ResponseEntity<List<MusicListItemDTO>> editSortingOrder(
            @PathVariable("listItemId") Long listItemId,
            @RequestBody Integer nextSortingOrder) throws CustomNotFoundException {

        return new ResponseEntity<>(
                this.musicListService.editSortingOrder(this.getCurrentLoggedAppUserId(), listItemId, nextSortingOrder)
                        .stream()
                        .map(listItem -> this.modelMapper.map(listItem, MusicListItemDTO.class))
                        .toList(),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<MusicListItemDTO> add(@RequestBody MusicListItemAddDTO musicListItemDTO)
            throws CustomBadRequestException, CustomEntityDuplicationException, ServiceNotAvailableException {

        musicListItemDTO.setAppUserId(this.getCurrentLoggedAppUserId());

        return new ResponseEntity<>(
                this.modelMapper.map(this.musicListService.add(musicListItemDTO), MusicListItemDTO.class),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{listItemId}")
    public ResponseEntity<MusicListItemDTO> deleteById(@PathVariable("listItemId") Long listItemId)
            throws CustomNotFoundException, ServiceNotAvailableException {

        return new ResponseEntity<>(
                this.modelMapper.map(this.musicListService.deleteById(this.getCurrentLoggedAppUserId(), listItemId), MusicListItemDTO.class),
                HttpStatus.OK
        );
    }

}
