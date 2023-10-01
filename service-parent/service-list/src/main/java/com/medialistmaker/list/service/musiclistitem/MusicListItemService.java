package com.medialistmaker.list.service.musiclistitem;

import com.medialistmaker.list.domain.MusicListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;

import java.util.List;

public interface MusicListItemService {

    List<MusicListItem> getByAppUserId(Long appUserId);

    MusicListItem add(MusicListItem musicListItem)
            throws CustomBadRequestException, CustomEntityDuplicationException;

    List<MusicListItem> changeSortingOrder(Long musicListItemId, Integer newOrder)
            throws CustomNotFoundException, CustomBadRequestException;

    MusicListItem deleteById(Long musicListId) throws CustomNotFoundException;

}
