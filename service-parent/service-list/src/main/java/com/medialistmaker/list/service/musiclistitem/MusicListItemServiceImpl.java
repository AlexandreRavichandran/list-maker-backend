package com.medialistmaker.list.service.musiclistitem;

import com.medialistmaker.list.domain.MusicListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.repository.MusicListItemRepository;
import com.medialistmaker.list.utils.CustomEntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class MusicListItemServiceImpl implements MusicListItemService {

    @Autowired
    MusicListItemRepository musicListItemRepository;

    @Autowired
    CustomEntityValidator<MusicListItem> musicListItemEntityValidator;

    @Override
    public List<MusicListItem> getByAppUserId(Long appUserId) {
        return this.musicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);
    }

    @Override
    public MusicListItem add(MusicListItem musicListItem) throws CustomBadRequestException, CustomEntityDuplicationException {

        List<String> errorList = this.musicListItemEntityValidator.validateEntity(musicListItem);

        if (Boolean.FALSE.equals(errorList.isEmpty())) {
            throw new CustomBadRequestException("Bad request", errorList);
        }

        MusicListItem isMusicAlreadyInAppUserList =
                this.musicListItemRepository.getByAppUserIdAndMusicId(
                        1L, musicListItem.getMusicId()
                );

        if (nonNull(isMusicAlreadyInAppUserList)) {
            throw new CustomEntityDuplicationException("Already exists");
        }

        return this.musicListItemRepository.save(musicListItem);

    }

    @Override
    public List<MusicListItem> changeSortingOrder(Long musicListItemId, Integer newOrder)
            throws CustomNotFoundException, CustomBadRequestException {

        MusicListItem itemToChange = this.musicListItemRepository.getReferenceById(musicListItemId);

        if (isNull(itemToChange)) {
            throw new CustomNotFoundException("Not found");
        }

        if (newOrder <= 0) {
            throw new CustomBadRequestException("New order not valid", new ArrayList<>());
        }

        List<MusicListItem> userMusicListItems =
                this.musicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(1L);

        if (userMusicListItems.size() < newOrder) {
            throw new CustomBadRequestException("New order not valid", new ArrayList<>());
        }

        MusicListItem itemWithSameOrder =
                this.musicListItemRepository.getByAppUserIdAndSortingOrder(1L, newOrder);

        itemWithSameOrder.setSortingOrder(itemToChange.getSortingOrder());
        itemToChange.setSortingOrder(newOrder);

        this.musicListItemRepository.saveAll(List.of(itemToChange, itemWithSameOrder));

        return List.of(itemToChange, itemWithSameOrder);

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
