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

    List<MovieListItem> getLatestAddedByAppUserId(Long appUserId);

    List<MovieListItem> editSortingOrder(Long appUserId, Long musicListItemId, Integer newSortingNumber) throws CustomNotFoundException;

    MovieListItem add(MovieListItemAddDTO movieListItemAdd)
            throws CustomBadRequestException, CustomEntityDuplicationException, ServiceNotAvailableException;

    MovieListItem deleteById(Long movieListId) throws CustomNotFoundException, ServiceNotAvailableException;

    Boolean isMovieIdAlreadyUsedInOtherList(Long movieId);

    Boolean isMovieApiCodeAlreadyInAppUserMovieList(Long appUserId, String apiCode) throws ServiceNotAvailableException;

    void updateOrder(Long appUserId);
}
