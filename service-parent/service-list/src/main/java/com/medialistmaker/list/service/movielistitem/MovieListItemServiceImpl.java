package com.medialistmaker.list.service.movielistitem;

import com.medialistmaker.list.connector.movie.MovieConnectorProxy;
import com.medialistmaker.list.domain.MovieListItem;
import com.medialistmaker.list.dto.movie.MovieAddDTO;
import com.medialistmaker.list.dto.movie.MovieDTO;
import com.medialistmaker.list.dto.movie.MovieListItemAddDTO;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.list.repository.MovieListItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Slf4j
public class MovieListItemServiceImpl implements MovieListItemService {

    private final MovieConnectorProxy movieConnectorProxy;

    private final MovieListItemRepository movieListItemRepository;

    private final Random random = new Random();

    public MovieListItemServiceImpl(
            MovieConnectorProxy movieConnectorProxy,
            MovieListItemRepository movieListItemRepository
    ) {
        this.movieConnectorProxy = movieConnectorProxy;
        this.movieListItemRepository = movieListItemRepository;
    }

    @Override
    public List<MovieListItem> getByAppUserId(Long appUserId) {
        return this.movieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);
    }

    @Override
    public List<MovieListItem> getLatestAddedByAppUserId(Long appUserId) {
        return this.movieListItemRepository.getTop3ByAppUserIdOrderByAddedAtDesc(appUserId);
    }

    @Override
    public List<MovieListItem> editSortingOrder(Long appUserId, Long musicListItemId, Integer newSortingNumber)
            throws CustomNotFoundException {

        List<MovieListItem> movieListItems = this.movieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);

        MovieListItem movieListItemToChange = this.movieListItemRepository.getReferenceById(musicListItemId);

        if (isNull(movieListItemToChange)) {
            throw new CustomNotFoundException("Music item not found");
        }

        Integer oldSortingNumber = movieListItemToChange.getSortingOrder();

        if (oldSortingNumber < newSortingNumber) {
            for (MovieListItem item : movieListItems) {
                if (item.getSortingOrder() > oldSortingNumber && item.getSortingOrder() <= newSortingNumber) {
                    item.setSortingOrder(item.getSortingOrder() - 1);
                }
            }
        }

        if (oldSortingNumber > newSortingNumber) {
            for (MovieListItem item : movieListItems) {
                if (item.getSortingOrder() < oldSortingNumber && item.getSortingOrder() >= newSortingNumber) {
                    item.setSortingOrder(item.getSortingOrder() + 1);
                }
            }
        }

        movieListItemToChange.setSortingOrder(newSortingNumber);

        this.movieListItemRepository.save(movieListItemToChange);

        List<MovieListItem> newMovieListItem = new ArrayList<>(movieListItems);

        newMovieListItem.sort(Comparator.comparingInt(MovieListItem::getSortingOrder));

        return this.movieListItemRepository.saveAll(newMovieListItem);

    }

    @Override
    public MovieListItem add(MovieListItemAddDTO movieListItem)
            throws CustomBadRequestException, CustomEntityDuplicationException, ServiceNotAvailableException {

        try {
            MovieDTO movieToAdd = this.movieConnectorProxy.getByApiCode(movieListItem.getApiCode());

            if (Boolean.TRUE.equals(this.isMovieAlreadyInAppUserList(movieListItem.getAppUserId(), movieToAdd.getId()))) {
                throw new CustomEntityDuplicationException("This movie is already in your list");
            }

        } catch (CustomNotFoundException e) {
            log.info("Movie not exist yet");
        }

        MovieListItem movieListItemToAdd = this.createMovieListItem(movieListItem.getAppUserId());

        try {
            MovieAddDTO movieAddDTO = new MovieAddDTO();
            movieAddDTO.setApiCode(movieListItem.getApiCode());
            MovieDTO movieDTO = this.movieConnectorProxy.saveByApiCode(movieAddDTO);
            movieListItemToAdd.setMovieId(movieDTO.getId());
            return this.movieListItemRepository.save(movieListItemToAdd);
        } catch (CustomBadRequestException e) {
            throw new CustomBadRequestException(e.getMessage());
        }
    }

    @Override
    public MovieListItem deleteById(Long appUserId, Long movieListId) throws CustomNotFoundException, ServiceNotAvailableException {

        MovieListItem itemToDelete = this.movieListItemRepository.getReferenceById(movieListId);

        if (isNull(itemToDelete)) {
            throw new CustomNotFoundException("Not found");
        }

        this.movieListItemRepository.delete(itemToDelete);

        this.updateOrder(appUserId);

        Boolean isMovieUsedInAnotherList = this.isMovieIdAlreadyUsedInOtherList(itemToDelete.getMovieId());

        if(Boolean.FALSE.equals(isMovieUsedInAnotherList)) {
            this.deleteMovie(itemToDelete.getMovieId());
        }

        return itemToDelete;
    }

    public Boolean isMovieIdAlreadyUsedInOtherList(Long movieId) {
        List<MovieListItem> movieListItems = this.movieListItemRepository.getByMovieId(movieId);

        return Boolean.FALSE.equals(movieListItems.isEmpty());

    }

    @Override
    public Boolean isMovieApiCodeAlreadyInAppUserMovieList(Long appUserId, String apiCode) throws ServiceNotAvailableException {

        boolean isMovieApiCodeAlreadyInAppUserMovieList;

        try {
            MovieDTO movieDTO = this.movieConnectorProxy.getByApiCode(apiCode);
            MovieListItem movieListItem = this.movieListItemRepository.getByAppUserIdAndMovieId(appUserId, movieDTO.getId());

            isMovieApiCodeAlreadyInAppUserMovieList = nonNull(movieListItem);

        } catch (CustomNotFoundException exception) {
            isMovieApiCodeAlreadyInAppUserMovieList = Boolean.FALSE;
        } catch (ServiceNotAvailableException exception) {
            throw new ServiceNotAvailableException("Service not available");
        }

        return isMovieApiCodeAlreadyInAppUserMovieList;
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

    @Override
    public MovieListItem getRandomInAppUserList(Long appUserId) {

        List<MovieListItem> movieListItems = this.movieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);

        if(movieListItems.isEmpty()) {
            return null;
        }

        int randomIndex = this.random.nextInt(movieListItems.size());

        return movieListItems.get(randomIndex);

    }

    private Boolean isMovieAlreadyInAppUserList(Long appUserId, Long movieId) {

        MovieListItem movieListItem = this.movieListItemRepository.getByAppUserIdAndMovieId(appUserId, movieId);

        return nonNull(movieListItem);
    }

    private MovieListItem createMovieListItem(Long appUserId) {

        return MovieListItem
                .builder()
                .appUserId(appUserId)
                .sortingOrder(this.getNextSortingOrder(appUserId))
                .addedAt(new Date())
                .build();

    }
}
