package com.medialistmaker.list.service.appusermusiclist;

import com.medialistmaker.list.domain.AppUserMusicListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.repository.AppUserMusicListItemRepository;
import com.medialistmaker.list.utils.CustomEntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class AppUserMusicListItemServiceImpl implements AppUserMusicListItemService {

    @Autowired
    AppUserMusicListItemRepository appUserMusicListItemRepository;

    @Autowired
    CustomEntityValidator<AppUserMusicListItem> musicListItemEntityValidator;

    @Override
    public List<AppUserMusicListItem> getByAppUserId(Long appUserId) {
        return this.appUserMusicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);
    }

    @Override
    public AppUserMusicListItem add(AppUserMusicListItem appUserMusicListItem) throws CustomBadRequestException, CustomEntityDuplicationException {

        List<String> errorList = this.musicListItemEntityValidator.validateEntity(appUserMusicListItem);

        if (Boolean.FALSE.equals(errorList.isEmpty())) {
            throw new CustomBadRequestException("Bad request", errorList);
        }

        AppUserMusicListItem isMusicAlreadyInAppUserList =
                this.appUserMusicListItemRepository.getByAppUserIdAndMusicId(
                        1L, appUserMusicListItem.getMusicId()
                );

        if (nonNull(isMusicAlreadyInAppUserList)) {
            throw new CustomEntityDuplicationException("Already exists");
        }

        return this.appUserMusicListItemRepository.save(appUserMusicListItem);

    }

    @Override
    public List<AppUserMusicListItem> changeSortingOrder(Long musicListItemId, Integer newOrder)
            throws CustomNotFoundException, CustomBadRequestException {

        AppUserMusicListItem itemToChange = this.appUserMusicListItemRepository.getReferenceById(musicListItemId);

        if (isNull(itemToChange)) {
            throw new CustomNotFoundException("Not found");
        }

        if (newOrder <= 0) {
            throw new CustomBadRequestException("New order not valid", new ArrayList<>());
        }

        List<AppUserMusicListItem> userMusicListItems =
                this.appUserMusicListItemRepository.getByAppUserIdOrderBySortingOrderAsc(1L);

        if (userMusicListItems.size() < newOrder) {
            throw new CustomBadRequestException("New order not valid", new ArrayList<>());
        }

        AppUserMusicListItem itemWithSameOrder =
                this.appUserMusicListItemRepository.getByAppUserIdAndSortingOrder(1L, newOrder);

        itemWithSameOrder.setSortingOrder(itemToChange.getSortingOrder());
        itemToChange.setSortingOrder(newOrder);

        this.appUserMusicListItemRepository.saveAll(List.of(itemToChange, itemWithSameOrder));

        return List.of(itemToChange, itemWithSameOrder);

    }

    @Override
    public AppUserMusicListItem deleteById(Long musicListId) throws CustomNotFoundException {

        AppUserMusicListItem itemToDelete = this.appUserMusicListItemRepository.getReferenceById(musicListId);

        if (isNull(itemToDelete)) {
            throw new CustomNotFoundException("Not found");
        }

        this.appUserMusicListItemRepository.delete(itemToDelete);

        return itemToDelete;
    }
}
