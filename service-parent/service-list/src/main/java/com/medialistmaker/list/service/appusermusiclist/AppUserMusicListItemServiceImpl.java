package com.medialistmaker.list.service.appusermusiclist;

import com.medialistmaker.list.domain.AppUserMusicListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.repository.AppUserMusicListItemRepository;
import com.medialistmaker.list.utils.CustomEntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public AppUserMusicListItem deleteById(Long musicListId) throws CustomNotFoundException {

        AppUserMusicListItem itemToDelete = this.appUserMusicListItemRepository.getReferenceById(musicListId);

        if (isNull(itemToDelete)) {
            throw new CustomNotFoundException("Not found");
        }

        this.appUserMusicListItemRepository.delete(itemToDelete);

        return itemToDelete;
    }
}
