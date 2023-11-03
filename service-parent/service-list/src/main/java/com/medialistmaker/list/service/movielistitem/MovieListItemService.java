package com.medialistmaker.list.service.movielistitem;

import com.medialistmaker.list.domain.MovieListItem;
import com.medialistmaker.list.dto.movie.MovieListItemAddDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;

import java.util.List;

public interface MovieListItemService {

    List<MovieListItem> getByAppUserId(Long appUserId);

    MovieListItem add(MovieListItemAddDTO movieListItemAdd)
            throws CustomBadRequestException, CustomEntityDuplicationException, ServiceNotAvailableException;

    MovieListItem deleteById(Long movieListId) throws CustomNotFoundException, ServiceNotAvailableException;

    void updateOrder(Long appUserId);
}
