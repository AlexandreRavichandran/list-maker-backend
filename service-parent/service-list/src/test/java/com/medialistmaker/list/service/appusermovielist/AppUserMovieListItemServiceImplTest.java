package com.medialistmaker.list.service.appusermovielist;

import com.medialistmaker.list.domain.AppUserMovieListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.repository.AppUserMovieListItemRepository;
import com.medialistmaker.list.utils.CustomEntityValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class AppUserMovieListItemServiceImplTest {

    @Mock
    AppUserMovieListItemRepository appUserMovieListItemRepository;

    @Mock
    CustomEntityValidator<AppUserMovieListItem> movieListEntityValidator;

    @InjectMocks
    AppUserMovieListItemServiceImpl appUserMovieListService;

    @Test
    void givenAppUserIdWhenGetByAppUserIdShouldReturnRelatedMovieListItemList() {

        AppUserMovieListItem firstMovieListItem = AppUserMovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        AppUserMovieListItem secondMovieListItem = AppUserMovieListItem
                .builder()
                .movieId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        AppUserMovieListItem thirdMovieListItem = AppUserMovieListItem
                .builder()
                .movieId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<AppUserMovieListItem> movieListItemList = List.of(firstMovieListItem, secondMovieListItem, thirdMovieListItem);

        Mockito.when(this.appUserMovieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(anyLong())).thenReturn(movieListItemList);

        List<AppUserMovieListItem> testGetByAppUserId = this.appUserMovieListService.getByAppUserId(1L);

        Mockito.verify(this.appUserMovieListItemRepository).getByAppUserIdOrderBySortingOrderAsc(anyLong());
        assertEquals(3, testGetByAppUserId.size());
        assertTrue(testGetByAppUserId.containsAll(movieListItemList));
    }

    @Test
    void givenMovieListItemWhenAddMovieListItemShouldSaveAndReturnMovieListItem()
            throws CustomBadRequestException, CustomEntityDuplicationException {

        AppUserMovieListItem movieListItem = AppUserMovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.movieListEntityValidator.validateEntity(movieListItem)).thenReturn(emptyList());
        Mockito.when(this.appUserMovieListItemRepository.getByAppUserIdAndMovieId(anyLong(), anyLong())).thenReturn(null);
        Mockito.when(this.appUserMovieListItemRepository.save(movieListItem)).thenReturn(movieListItem);

        AppUserMovieListItem testAddMovieListItem = this.appUserMovieListService.add(movieListItem);

        Mockito.verify(this.movieListEntityValidator).validateEntity(movieListItem);
        Mockito.verify(this.appUserMovieListItemRepository).getByAppUserIdAndMovieId(anyLong(), anyLong());
        Mockito.verify(this.appUserMovieListItemRepository).save(movieListItem);

        assertEquals(testAddMovieListItem, movieListItem);
    }

    @Test
    void givenInvalidMovieListItemWhenAddMovieListItemShouldThrowBadRequestException() {

        AppUserMovieListItem movieListItem = AppUserMovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        List<String> errorList = List.of("Error 1", "Error 2");
        Mockito.when(this.movieListEntityValidator.validateEntity(movieListItem)).thenReturn(errorList);

        assertThrows(CustomBadRequestException.class, () -> this.appUserMovieListService.add(movieListItem));

        Mockito.verify(this.movieListEntityValidator).validateEntity(movieListItem);

    }

    @Test
    void givenMovieListItemWithAlreadyExistingMovieIdShouldThrowEntityDuplicationException() {

        AppUserMovieListItem movieListItem = AppUserMovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.movieListEntityValidator.validateEntity(movieListItem)).thenReturn(emptyList());
        Mockito.when(this.appUserMovieListItemRepository
                        .getByAppUserIdAndMovieId(anyLong(), anyLong()))
                .thenReturn(movieListItem);

        assertThrows(CustomEntityDuplicationException.class, () -> this.appUserMovieListService.add(movieListItem));

        Mockito.verify(this.movieListEntityValidator).validateEntity(movieListItem);
        Mockito.verify(this.appUserMovieListItemRepository).getByAppUserIdAndMovieId(anyLong(), anyLong());
    }

    @Test
    void givenIdWhenDeleteByIdShouldDeleteAndReturnRelatedMovieListItem() throws CustomNotFoundException {

        AppUserMovieListItem movieListItem = AppUserMovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.appUserMovieListItemRepository.getReferenceById(anyLong())).thenReturn(movieListItem);

        AppUserMovieListItem testDeleteById = this.appUserMovieListService.deleteById(1L);

        Mockito.verify(this.appUserMovieListItemRepository).getReferenceById(anyLong());
        assertEquals(movieListItem, testDeleteById);
    }

    @Test
    void givenInvalidIdWhenDeleteByIdShouldThrowNotFoundException() {

        Mockito.when(this.appUserMovieListItemRepository.getReferenceById(anyLong())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.appUserMovieListService.deleteById(1L));

        Mockito.verify(this.appUserMovieListItemRepository).getReferenceById(anyLong());
    }
}