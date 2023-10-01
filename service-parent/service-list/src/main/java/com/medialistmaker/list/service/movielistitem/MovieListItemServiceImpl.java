package com.medialistmaker.list.service.movielistitem;

import com.medialistmaker.list.domain.MovieListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.repository.MovieListItemRepository;
import com.medialistmaker.list.utils.CustomEntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class MovieListItemServiceImpl implements MovieListItemService {

    @Autowired
    CustomEntityValidator<MovieListItem> movieListItemEntityValidator;

    @Autowired
    MovieListItemRepository movieListItemRepository;

    @Override
    public List<MovieListItem> getByAppUserId(Long appUserId) {
        return this.movieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);
    }

    @Override
    public MovieListItem add(MovieListItem movieListItem)
            throws CustomBadRequestException, CustomEntityDuplicationException {

        List<String> errorList = this.movieListItemEntityValidator.validateEntity(movieListItem);

        if (Boolean.FALSE.equals(errorList.isEmpty())) {
            throw new CustomBadRequestException("Bad request", errorList);
        }

        MovieListItem isMovieAlreadyInAppUserList =
                this.movieListItemRepository.getByAppUserIdAndMovieId(
                        1L, movieListItem.getMovieId()
                );

        if (nonNull(isMovieAlreadyInAppUserList)) {
            throw new CustomEntityDuplicationException("Already exists");
        }

        return this.movieListItemRepository.save(movieListItem);
    }


    @Override
    public MovieListItem deleteById(Long movieListId) throws CustomNotFoundException {

        MovieListItem itemToDelete = this.movieListItemRepository.getReferenceById(movieListId);

        if (isNull(itemToDelete)) {
            throw new CustomNotFoundException("Not found");
        }

        this.movieListItemRepository.delete(itemToDelete);

        return itemToDelete;
    }
}
