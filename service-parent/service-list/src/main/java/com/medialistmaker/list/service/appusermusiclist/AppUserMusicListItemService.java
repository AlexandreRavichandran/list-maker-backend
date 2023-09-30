package com.medialistmaker.list.service.appusermusiclist;

import com.medialistmaker.list.domain.AppUserMusicListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;

import java.util.List;

public interface AppUserMusicListItemService {

    List<AppUserMusicListItem> getByAppUserId(Long appUserId);

    AppUserMusicListItem add(AppUserMusicListItem appUserMusicListItem)
            throws CustomBadRequestException, CustomEntityDuplicationException;

    List<AppUserMusicListItem> changeSortingOrder(Long musicListItemId, Integer newOrder)
            throws CustomNotFoundException, CustomBadRequestException;

    AppUserMusicListItem deleteById(Long musicListId) throws CustomNotFoundException;

}
