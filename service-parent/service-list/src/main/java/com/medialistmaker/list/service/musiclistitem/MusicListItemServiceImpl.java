package com.medialistmaker.list.service.musiclistitem;

import com.medialistmaker.list.connector.music.MusicConnectorProxy;
import com.medialistmaker.list.domain.MusicListItem;
import com.medialistmaker.list.dto.music.MusicAddDTO;
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

import java.util.ArrayList;
import java.util.Comparator;
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
    public List<MusicListItem> editSortingOrder(Long appUserId, Long musicListItemId, Integer newSortingNumber)
            throws CustomNotFoundException {

        List<MusicListItem> musicListItems = this.musicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);

        MusicListItem musicListItemToChange = this.musicListItemRepository.getReferenceById(musicListItemId);

        if(isNull(musicListItemToChange)) {
            throw new CustomNotFoundException("Music item not found");
        }

        Integer oldSortingNumber = musicListItemToChange.getSortingOrder();

        if(oldSortingNumber < newSortingNumber) {
            for (MusicListItem item : musicListItems) {
                if(item.getSortingOrder() > oldSortingNumber && item.getSortingOrder() <= newSortingNumber) {
                    item.setSortingOrder(item.getSortingOrder() -1);
                }
            }
        }

        if(oldSortingNumber > newSortingNumber ) {
            for (MusicListItem item : musicListItems) {
                if(item.getSortingOrder() < oldSortingNumber && item.getSortingOrder() >= newSortingNumber) {
                    item.setSortingOrder(item.getSortingOrder() +1);
                }
            }
        }

        musicListItemToChange.setSortingOrder(newSortingNumber);

        this.musicListItemRepository.save(musicListItemToChange);

        List<MusicListItem> newMusicListItem = new ArrayList<>(musicListItems);

        newMusicListItem.sort(Comparator.comparingInt(MusicListItem::getSortingOrder));
        return this.musicListItemRepository.saveAll(musicListItems);

    }

    @Override
    public MusicListItem add(MusicListItemAddDTO listItemAddDTO) throws
            CustomBadRequestException, CustomEntityDuplicationException, ServiceNotAvailableException {

        try {

            MusicDTO musicToAdd = this.musicConnectorProxy.getMusicByApiCodeAndType(
                    listItemAddDTO.getApiCode(), listItemAddDTO.getType()
            );

            if (Boolean.TRUE.equals(this.isMusicAlreadyInAppUserList(musicToAdd.getId(), 1L))) {
                throw new CustomEntityDuplicationException("This music is already in your list");
            }

        } catch (CustomNotFoundException e) {
            log.info("Music does not exist yet");
        }

        MusicListItem musicListItemToAdd = this.createMusicListItem(1L);

        try {
            MusicAddDTO musicAddDTO = new MusicAddDTO();
            musicAddDTO.setApiCode(listItemAddDTO.getApiCode());
            musicAddDTO.setType(listItemAddDTO.getType());
            MusicDTO musicDTO = this.musicConnectorProxy.saveByApiCode(musicAddDTO);
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

        this.updateAllMusicListItemSortingOrder(1L);

        Boolean isMovieUsedInAnotherList = this.isMusicUsedInOtherList(itemToDelete.getMusicId());

        if(Boolean.FALSE.equals(isMovieUsedInAnotherList)) {
            this.deleteMusic(itemToDelete.getMusicId());
        }
        return itemToDelete;
    }

    @Override
    public Boolean isMusicApiCodeAndTypeAlreadyInAppUserMovieList(Long appUserId, String apiCode, Integer type)
            throws ServiceNotAvailableException {

        Boolean isMusicApiCodeAlreadyInAppUserMovieList = null;

        try {
            MusicDTO musicDTO = this.musicConnectorProxy.getMusicByApiCodeAndType(apiCode, type);
            MusicListItem musicListItem = this.musicListItemRepository.getByAppUserIdAndMusicId(appUserId, musicDTO.getId());

            isMusicApiCodeAlreadyInAppUserMovieList = nonNull(musicListItem);

        } catch (CustomNotFoundException exception) {
            isMusicApiCodeAlreadyInAppUserMovieList = Boolean.FALSE;
        } catch (ServiceNotAvailableException exception) {
            throw new ServiceNotAvailableException("Service not available");
        }

        return isMusicApiCodeAlreadyInAppUserMovieList;

    }

    public Boolean isMusicUsedInOtherList(Long musicId) {

        List<MusicListItem> musicMistItems = this.musicListItemRepository.getByMusicId(musicId);

        return Boolean.FALSE.equals(musicMistItems.isEmpty());

    }

    @Override
    public void updateAllMusicListItemSortingOrder(Long appUserId) {
        List<MusicListItem> musicListItems = this.musicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);

        Integer i = 1;

        for (MusicListItem musicListItem : musicListItems) {
            musicListItem.setSortingOrder(i);
            i++;
        }

        this.musicListItemRepository.saveAll(musicListItems);

    }

    private Integer getNextSortingOrder(Long appUserId) {

        MusicListItem appUserLastItem = this.musicListItemRepository.getFirstByAppUserIdOrderBySortingOrderDesc(appUserId);

        if(nonNull(appUserLastItem)) {
            return appUserLastItem.getSortingOrder() + 1;
        }

        return 1;
    }

    private void deleteMusic(Long movieId) throws CustomNotFoundException, ServiceNotAvailableException{
        this.musicConnectorProxy.deleteById(movieId);
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
