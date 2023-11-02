package com.medialistmaker.list.service.musiclistitem;

import com.medialistmaker.list.connector.music.MusicConnectorProxy;
import com.medialistmaker.list.domain.MusicListItem;
import com.medialistmaker.list.dto.music.MusicDTO;
import com.medialistmaker.list.dto.music.MusicListItemAddDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.list.repository.MusicListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class MusicListItemServiceImpl implements MusicListItemService {

    @Autowired
    MusicListItemRepository musicListItemRepository;

    @Autowired
    MusicConnectorProxy musicConnectorProxy;

    @Override
    public List<MusicListItem> getByAppUserId(Long appUserId) {
        return this.musicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);
    }

    @Override
    public MusicListItem add(MusicListItemAddDTO listItemAddDTO) throws
            CustomBadRequestException, CustomEntityDuplicationException, ServiceNotAvailableException {

        MusicDTO musicToAdd;

        try {
            if(listItemAddDTO.getType().equals(1)) {
                musicToAdd = this.musicConnectorProxy.getAlbumByApiCode(listItemAddDTO.getApiCode());
            } else {
                musicToAdd = this.musicConnectorProxy.getSongByApiCode(listItemAddDTO.getApiCode());
            }
        } catch (CustomNotFoundException e) {
            throw new CustomBadRequestException("Movie not exists");
        }

        MusicListItem isMusicAlreadyInAppUserList =
                this.musicListItemRepository.getByAppUserIdAndMusicId(
                        1L, musicToAdd.getId()
                );

        if (nonNull(isMusicAlreadyInAppUserList)) {
            throw new CustomEntityDuplicationException("Already exists");
        }

        MusicListItem musicListItemToAdd = MusicListItem
                .builder()
                .appUserId(1L)
                .addedAt(new Date())
                .build();
        try {
            MusicDTO musicDTO = this.musicConnectorProxy.saveByApiCode(listItemAddDTO.getType(), listItemAddDTO.getApiCode());
            musicListItemToAdd.setMusicId(musicDTO.getId());
            return this.musicListItemRepository.save(musicListItemToAdd);
        } catch (CustomBadRequestException e) {
            throw new CustomBadRequestException(e.getMessage());
        }
    }

    @Override
    public MusicListItem deleteById(Long musicListId) throws CustomNotFoundException {

        MusicListItem itemToDelete = this.musicListItemRepository.getReferenceById(musicListId);

        if (isNull(itemToDelete)) {
            throw new CustomNotFoundException("Not found");
        }

        this.musicListItemRepository.delete(itemToDelete);

        return itemToDelete;
    }
}
