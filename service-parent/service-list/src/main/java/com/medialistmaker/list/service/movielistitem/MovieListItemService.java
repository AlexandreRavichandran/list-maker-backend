package com.medialistmaker.list.service.movielistitem;

import com.medialistmaker.list.domain.MovieListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;

import java.util.List;

public interface MovieListItemService {

    List<MovieListItem> getByAppUserId(Long appUserId);

    MovieListItem add(MovieListItem movieListItem)
            throws CustomBadRequestException, CustomEntityDuplicationException;

    MovieListItem deleteById(Long movieListId) throws CustomNotFoundException;

}
