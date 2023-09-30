package com.medialistmaker.list.service.appusermovielist;

import com.medialistmaker.list.domain.AppUserMovieListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;

import java.util.List;

public interface AppUserMovieListItemService {

    List<AppUserMovieListItem> getByAppUserId(Long appUserId);

    AppUserMovieListItem add(AppUserMovieListItem appUserMovieListItem)
            throws CustomBadRequestException, CustomEntityDuplicationException;

    AppUserMovieListItem deleteById(Long movieListId) throws CustomNotFoundException;

}
