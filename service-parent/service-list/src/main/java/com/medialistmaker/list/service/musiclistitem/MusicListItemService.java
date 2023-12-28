package com.medialistmaker.list.service.musiclistitem;

import com.medialistmaker.list.domain.MusicListItem;
import com.medialistmaker.list.dto.music.MusicListItemAddDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;

import java.util.List;

public interface MusicListItemService {

    List<MusicListItem> getByAppUserId(Long appUserId);

    List<MusicListItem> getLatestAddedByAppUserId(Long appUserId);

    MusicListItem add(MusicListItemAddDTO listItemAddDTO)
            throws CustomBadRequestException, CustomEntityDuplicationException, ServiceNotAvailableException;

    MusicListItem deleteById(Long musicListId) throws CustomNotFoundException, ServiceNotAvailableException;

    Boolean isMusicApiCodeAndTypeAlreadyInAppUserMovieList(Long appUserId, String apiCode, Integer type)
            throws ServiceNotAvailableException;

    Boolean isMusicUsedInOtherList(Long musicId);

    void updateOrder(Long appUserId);
}
