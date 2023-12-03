package com.medialistmaker.list.service.movielistitem;

import com.medialistmaker.list.connector.movie.MovieConnectorProxy;
import com.medialistmaker.list.domain.MovieListItem;
import com.medialistmaker.list.dto.movie.MovieDTO;
import com.medialistmaker.list.dto.movie.MovieListItemAddDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.list.repository.MovieListItemRepository;
import com.medialistmaker.list.utils.CustomEntityValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Slf4j
public class MovieListItemServiceImpl implements MovieListItemService {

    @Autowired
    CustomEntityValidator<MovieListItem> movieListItemEntityValidator;

    @Autowired
    MovieConnectorProxy movieConnectorProxy;

    @Autowired
    MovieListItemRepository movieListItemRepository;

    @Override
    public List<MovieListItem> getByAppUserId(Long appUserId) {
        return this.movieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);
    }

    @Override
    public MovieListItem add(MovieListItemAddDTO movieListItem)
            throws CustomBadRequestException, CustomEntityDuplicationException, ServiceNotAvailableException {

        MovieDTO movieToAdd;

        try {
            movieToAdd = this.movieConnectorProxy.getByApiCode(movieListItem.getApiCode());
        } catch (CustomNotFoundException e) {
            throw new CustomBadRequestException("Movie not exists");
        }

        MovieListItem isMovieAlreadyInAppUserList =
                this.movieListItemRepository.getByAppUserIdAndMovieId(
                        1L, movieToAdd.getId()
                );

        if (nonNull(isMovieAlreadyInAppUserList)) {
            throw new CustomEntityDuplicationException("Already exists");
        }

        MovieListItem movieListItemToAdd = MovieListItem
                .builder()
                .appUserId(1L)
                .sortingOrder(this.getNextSortingOrder(1L))
                .addedAt(new Date())
                .build();
        try {
            MovieDTO movieDTO = this.movieConnectorProxy.saveByApiCode(movieListItem.getApiCode());
            movieListItemToAdd.setMovieId(movieDTO.getId());
            return this.movieListItemRepository.save(movieListItemToAdd);
        } catch (CustomBadRequestException e) {
            throw new CustomBadRequestException(e.getMessage());
        }
    }

    @Override
    public MovieListItem deleteById(Long movieListId) throws CustomNotFoundException, ServiceNotAvailableException {

        MovieListItem itemToDelete = this.movieListItemRepository.getReferenceById(movieListId);

        if (isNull(itemToDelete)) {
            throw new CustomNotFoundException("Not found");
        }

        this.movieListItemRepository.delete(itemToDelete);

        this.updateOrder(1L);

        Boolean isMovieUsedInAnotherList = this.isMovieUsedInOtherList(itemToDelete.getMovieId());

        if(Boolean.FALSE.equals(isMovieUsedInAnotherList)) {
            this.deleteMovie(itemToDelete.getMovieId());
        }

        return itemToDelete;
    }

    public Boolean isMovieUsedInOtherList(Long movieId) {
        List<MovieListItem> movieListItems = this.movieListItemRepository.getByMovieId(movieId);

        return Boolean.FALSE.equals(movieListItems.isEmpty());

    }

    private Integer getNextSortingOrder(Long appUserId) {

        MovieListItem appUserLastItem = this.movieListItemRepository.getFirstByAppUserIdOrderBySortingOrderDesc(appUserId);

        if(nonNull(appUserLastItem)) {
            return appUserLastItem.getSortingOrder() + 1;
        }

        return 1;
    }

    private void deleteMovie(Long movieId) throws CustomNotFoundException, ServiceNotAvailableException{
        this.movieConnectorProxy.deleteById(movieId);
    }

    @Override
    public void updateOrder(Long appUserId) {

        List<MovieListItem> movieListItems = this.movieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);

        Integer i = 1;

        for (MovieListItem movieListItem : movieListItems) {
            movieListItem.setSortingOrder(i);
            i++;
        }

        this.movieListItemRepository.saveAll(movieListItems);

    }
}
