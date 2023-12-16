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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Slf4j
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
    public List<MusicListItem> getLatestAddedByAppUserId(Long appUserId) {
        return this.musicListItemRepository.getTop3ByAppUserIdOrderByAddedAtDesc(appUserId);
    }

    @Override
    public MusicListItem add(MusicListItemAddDTO listItemAddDTO) throws
            CustomBadRequestException, CustomEntityDuplicationException, ServiceNotAvailableException {

        try {

            MusicDTO musicToAdd = this.musicConnectorProxy.getMusicByApiCodeAndType(
                    listItemAddDTO.getApiCode(), listItemAddDTO.getType()
            );

            if (Boolean.TRUE.equals(this.isMusicAlreadyInAppUserList(musicToAdd.getId(), 1L))) {
                throw new CustomEntityDuplicationException("Already exists");
            }

        } catch (CustomNotFoundException e) {
            log.info("Music does not exist yet");
        }

        MusicListItem musicListItemToAdd = this.createMusicListItem(1L);

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

    private Boolean isMusicAlreadyInAppUserList(Long musicId, Long appUserId) {

        MusicListItem isMusicAlreadyInAppUserList =
                this.musicListItemRepository.getByAppUserIdAndMusicId(
                        appUserId, musicId
                );

        return nonNull(isMusicAlreadyInAppUserList);
    }

    private MusicListItem createMusicListItem(Long appUserId) {
        return MusicListItem
                .builder()
                .appUserId(appUserId)
                .sortingOrder(this.getNextSortingOrder(1L))
                .addedAt(new Date())
                .build();
    }
}
