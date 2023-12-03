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
            } else if(listItemAddDTO.getType().equals(2)) {
                musicToAdd = this.musicConnectorProxy.getSongByApiCode(listItemAddDTO.getApiCode());
            } else {
                throw new CustomBadRequestException("Bad type");
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
                .sortingOrder(this.getNextSortingOrder(1L))
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
    public MusicListItem deleteById(Long musicListId) throws CustomNotFoundException, ServiceNotAvailableException {

        MusicListItem itemToDelete = this.musicListItemRepository.getReferenceById(musicListId);

        if (isNull(itemToDelete)) {
            throw new CustomNotFoundException("Not found");
        }

        this.musicListItemRepository.delete(itemToDelete);

        this.updateOrder(1L);

        Boolean isMovieUsedInAnotherList = this.isMusicUsedInOtherList(itemToDelete.getMusicId());

        if(Boolean.FALSE.equals(isMovieUsedInAnotherList)) {
            this.deleteMusic(itemToDelete.getMusicId());
        }
        return itemToDelete;
    }

    private Integer getNextSortingOrder(Long appUserId) {

        MusicListItem appUserLastItem = this.musicListItemRepository.getFirstByAppUserIdOrderBySortingOrderDesc(appUserId);

        if(nonNull(appUserLastItem)) {
            return appUserLastItem.getSortingOrder() + 1;
        }

        return 1;
    }

    public Boolean isMusicUsedInOtherList(Long musicId) {

        List<MusicListItem> musicMistItems = this.musicListItemRepository.getByMusicId(musicId);

        return Boolean.FALSE.equals(musicMistItems.isEmpty());

    }

    private void deleteMusic(Long movieId) throws CustomNotFoundException, ServiceNotAvailableException{
        this.musicConnectorProxy.deleteById(movieId);
    }

    @Override
    public void updateOrder(Long appUserId) {
        List<MusicListItem> musicListItems = this.musicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);

        Integer i = 1;

        for (MusicListItem musicListItem : musicListItems) {
            musicListItem.setSortingOrder(i);
            i++;
        }

        this.musicListItemRepository.saveAll(musicListItems);

    }
}
