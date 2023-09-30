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
import static org.mockito.ArgumentMatchers.anyInt;
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

    @Test
    void givenListItemIdAndAppUserIdAndSortingNumberWhenChangeSortingOrderShouldChangeSortingOrderAndSaveAndReturnBothEditedMovieListItem()
            throws CustomBadRequestException, CustomNotFoundException {

        AppUserMovieListItem movieListItemToEdit = AppUserMovieListItem
                .builder()
                .id(1L)
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        AppUserMovieListItem movieListItemWithNewSortingOrderEdited = AppUserMovieListItem
                .builder()
                .id(1L)
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        AppUserMovieListItem movieListItemWithSameNewSortingOrder = AppUserMovieListItem
                .builder()
                .id(2L)
                .movieId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        AppUserMovieListItem movieListItemWithSameNewSortingOrderEdited = AppUserMovieListItem
                .builder()
                .id(2L)
                .movieId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();


        Mockito
                .when(this.appUserMovieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(anyLong()))
                .thenReturn(List.of(movieListItemToEdit, movieListItemWithSameNewSortingOrder));

        Mockito.when(this.appUserMovieListItemRepository.getReferenceById(anyLong())).thenReturn(movieListItemToEdit);
        Mockito
                .when(this.appUserMovieListItemRepository.getByAppUserIdAndSortingOrder(anyLong(), anyInt()))
                .thenReturn(movieListItemWithSameNewSortingOrder);

        List<AppUserMovieListItem> testChangeSortingNumber =
                this.appUserMovieListService.changeSortingOrder(1L, 2);

        Mockito
                .verify(this.appUserMovieListItemRepository)
                .saveAll(List.of(movieListItemWithNewSortingOrderEdited, movieListItemWithSameNewSortingOrderEdited));
        Mockito.verify(this.appUserMovieListItemRepository).getReferenceById(anyLong());
        Mockito.verify(this.appUserMovieListItemRepository).getByAppUserIdAndSortingOrder(anyLong(), anyInt());


        assertFalse(testChangeSortingNumber.isEmpty());

        assertEquals(movieListItemWithSameNewSortingOrder.getSortingOrder(), testChangeSortingNumber.get(1).getSortingOrder());
        assertEquals(movieListItemToEdit.getSortingOrder(), testChangeSortingNumber.get(0).getSortingOrder());

        assertNotEquals(testChangeSortingNumber.get(0).getSortingOrder(), testChangeSortingNumber.get(1).getSortingOrder());
    }

    @Test
    void givenMovieListItemIdAndAppUserIdAndInvalidSortingNumberWhenChangeSortingOrderShouldThrowBadRequestException() {

        AppUserMovieListItem movieListItem = AppUserMovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.appUserMovieListItemRepository.getReferenceById(anyLong())).thenReturn(movieListItem);

        assertThrows(
                CustomBadRequestException.class,
                () -> this.appUserMovieListService.changeSortingOrder(1L, 0)
        );


        assertThrows(
                CustomBadRequestException.class,
                () -> this.appUserMovieListService.changeSortingOrder(1L, -1)
        );

        assertThrows(
                CustomBadRequestException.class,
                () -> this.appUserMovieListService.changeSortingOrder(1L, 6)
        );

    }

    @Test
    void givenInvalidMovieListItemAndAppUserIdAndSortingNumberWhenChangeSortingOrderShouldThrowNotFoundException() {

        Mockito.when(this.appUserMovieListItemRepository.getReferenceById(anyLong())).thenReturn(null);

        assertThrows(
                CustomNotFoundException.class,
                () -> this.appUserMovieListService.changeSortingOrder(1L, 2)
        );
    }
}