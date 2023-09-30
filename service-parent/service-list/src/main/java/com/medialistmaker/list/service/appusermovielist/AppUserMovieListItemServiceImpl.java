package com.medialistmaker.list.service.appusermovielist;

import com.medialistmaker.list.domain.AppUserMovieListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.repository.AppUserMovieListItemRepository;
import com.medialistmaker.list.utils.CustomEntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class AppUserMovieListItemServiceImpl implements AppUserMovieListItemService {

    @Autowired
    CustomEntityValidator<AppUserMovieListItem> movieListItemEntityValidator;

    @Autowired
    AppUserMovieListItemRepository appUserMovieListItemRepository;

    @Override
    public List<AppUserMovieListItem> getByAppUserId(Long appUserId) {
        return this.appUserMovieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(appUserId);
    }

    @Override
    public AppUserMovieListItem add(AppUserMovieListItem appUserMovieListItem)
            throws CustomBadRequestException, CustomEntityDuplicationException {

        List<String> errorList = this.movieListItemEntityValidator.validateEntity(appUserMovieListItem);

        if (Boolean.FALSE.equals(errorList.isEmpty())) {
            throw new CustomBadRequestException("Bad request", errorList);
        }

        AppUserMovieListItem isMovieAlreadyInAppUserList =
                this.appUserMovieListItemRepository.getByAppUserIdAndMovieId(
                        1L, appUserMovieListItem.getMovieId()
                );

        if (nonNull(isMovieAlreadyInAppUserList)) {
            throw new CustomEntityDuplicationException("Already exists");
        }

        return this.appUserMovieListItemRepository.save(appUserMovieListItem);
    }

    @Override
    public AppUserMovieListItem deleteById(Long movieListId) throws CustomNotFoundException {

        AppUserMovieListItem itemToDelete = this.appUserMovieListItemRepository.getReferenceById(movieListId);

        if (isNull(itemToDelete)) {
            throw new CustomNotFoundException("Not found");
        }

        this.appUserMovieListItemRepository.delete(itemToDelete);

        return itemToDelete;
    }
}
