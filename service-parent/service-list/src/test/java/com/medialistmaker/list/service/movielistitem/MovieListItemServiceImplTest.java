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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class MovieListItemServiceImplTest {

    @Mock
    MovieListItemRepository movieListItemRepository;

    @Mock
    MovieConnectorProxy movieConnectorProxy;

    @InjectMocks
    MovieListItemServiceImpl movieListService;

    @Test
    void givenAppUserIdWhenGetByAppUserIdShouldReturnRelatedMovieListItemList() {

        MovieListItem firstMovieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MovieListItem secondMovieListItem = MovieListItem
                .builder()
                .movieId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        MovieListItem thirdMovieListItem = MovieListItem
                .builder()
                .movieId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<MovieListItem> movieListItemList = List.of(firstMovieListItem, secondMovieListItem, thirdMovieListItem);

        Mockito.when(this.movieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(anyLong())).thenReturn(movieListItemList);

        List<MovieListItem> testGetByAppUserId = this.movieListService.getByAppUserId(1L);

        Mockito.verify(this.movieListItemRepository).getByAppUserIdOrderBySortingOrderAsc(anyLong());
        assertEquals(3, testGetByAppUserId.size());
        assertTrue(testGetByAppUserId.containsAll(movieListItemList));
    }

    @Test
    void givenAppUserIdWhenGetLatestAddedByAppUserIdShouldReturnRelatedMovieListItemList() {

        MovieListItem firstMovieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MovieListItem secondMovieListItem = MovieListItem
                .builder()
                .movieId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        MovieListItem thirdMovieListItem = MovieListItem
                .builder()
                .movieId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<MovieListItem> movieListItemList = List.of(firstMovieListItem, secondMovieListItem, thirdMovieListItem);

        Mockito.when(this.movieListItemRepository.getTop3ByAppUserIdOrderByAddedAtDesc(anyLong())).thenReturn(movieListItemList);

        List<MovieListItem> testGetByAppUserId = this.movieListService.getLatestAddedByAppUserId(1L);

        Mockito.verify(this.movieListItemRepository).getTop3ByAppUserIdOrderByAddedAtDesc(anyLong());
        assertEquals(3, testGetByAppUserId.size());
        assertTrue(testGetByAppUserId.containsAll(movieListItemList));
    }

    @Test
    void givenMovieListItemAddWhenAddMovieListItemShouldSaveAndReturnMovieListItem()
            throws CustomBadRequestException, CustomNotFoundException,
            CustomEntityDuplicationException, ServiceNotAvailableException {

        MovieListItemAddDTO listItemAddDTO = new MovieListItemAddDTO();
        listItemAddDTO.setApiCode("XXX");

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(1L);

        MovieListItem movieListItem = new MovieListItem();
        movieListItem.setMovieId(movieDTO.getId());
        movieListItem.setAppUserId(1L);

        MovieAddDTO movieAddDTO = new MovieAddDTO();
        movieAddDTO.setApiCode("XXX");

        Mockito.when(this.movieConnectorProxy.getByApiCode(anyString())).thenReturn(movieDTO);
        Mockito.when(this.movieListItemRepository.getByAppUserIdAndMovieId(anyLong(), anyLong())).thenReturn(null);
        Mockito.when(this.movieConnectorProxy.saveByApiCode(movieAddDTO)).thenReturn(movieDTO);
        Mockito.when(this.movieListItemRepository.save(any())).thenReturn(movieListItem);

        MovieListItem testAddMovieListItem = this.movieListService.add(listItemAddDTO);

        Mockito.verify(this.movieConnectorProxy).getByApiCode(anyString());
        Mockito.verify(this.movieListItemRepository).getByAppUserIdAndMovieId(anyLong(), anyLong());
        Mockito.verify(this.movieConnectorProxy).saveByApiCode(movieAddDTO);
        Mockito.verify(this.movieListItemRepository).save(any());

        assertEquals(movieDTO.getId(),testAddMovieListItem.getMovieId());
    }

    @Test
    void givenMovieListItemAddWithAlreadyExistingMovieIdWhenAddMovieListItemShouldThrowEntityDuplicationException() throws Exception {

        MovieListItemAddDTO listItemAddDTO = new MovieListItemAddDTO();
        listItemAddDTO.setApiCode("XXXX");

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(1L);

        MovieListItem movieListItem = new MovieListItem();
        movieListItem.setId(1L);
        movieListItem.setMovieId(1L);
        movieListItem.setAppUserId(1L);
        movieListItem.setSortingOrder(2);

        Mockito.when(this.movieConnectorProxy.getByApiCode(anyString())).thenReturn(movieDTO);
        Mockito.when(this.movieListItemRepository.getByAppUserIdAndMovieId(anyLong(), anyLong())).thenReturn(movieListItem);

        assertThrows(CustomEntityDuplicationException.class, () -> this.movieListService.add(listItemAddDTO));

        Mockito.verify(this.movieConnectorProxy).getByApiCode(anyString());
        Mockito.verify(this.movieListItemRepository).getByAppUserIdAndMovieId(anyLong(), anyLong());


    }

    @Test
    void givenMovieListItemAddWhenAddMovieListItemAndServiceNotAvailableShouldThrowServiceNotAvailableException() throws Exception {

        MovieListItemAddDTO listItemAddDTO = new MovieListItemAddDTO();
        listItemAddDTO.setApiCode("XXXX");

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(1L);

        MovieListItem movieListItem = new MovieListItem();
        movieListItem.setMovieId(movieDTO.getId());
        movieListItem.setAppUserId(1L);

        MovieAddDTO movieAddDTO = new MovieAddDTO();
        movieAddDTO.setApiCode("XXXX");


        Mockito.when(this.movieConnectorProxy.getByApiCode(anyString())).thenReturn(movieDTO);
        Mockito.when(this.movieListItemRepository.getByAppUserIdAndMovieId(anyLong(), anyLong())).thenReturn(null);
        Mockito.when(this.movieConnectorProxy.saveByApiCode(movieAddDTO)).thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class, () -> this.movieListService.add(listItemAddDTO));

        Mockito.verify(this.movieConnectorProxy).getByApiCode(anyString());
        Mockito.verify(this.movieListItemRepository).getByAppUserIdAndMovieId(anyLong(), anyLong());
        Mockito.verify(this.movieConnectorProxy).saveByApiCode(movieAddDTO);
    }

    @Test
    void givenIdWhenDeleteByIdShouldDeleteAndReturnRelatedMovieListItem() throws Exception {

        MovieListItem movieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.movieListItemRepository.getReferenceById(anyLong())).thenReturn(movieListItem);

        MovieListItem testDeleteById = this.movieListService.deleteById(1L);

        Mockito.verify(this.movieListItemRepository).getReferenceById(anyLong());
        assertEquals(movieListItem, testDeleteById);
    }

    @Test
    void givenInvalidIdWhenDeleteByIdShouldThrowNotFoundException() {

        Mockito.when(this.movieListItemRepository.getReferenceById(anyLong())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.movieListService.deleteById(1L));

        Mockito.verify(this.movieListItemRepository).getReferenceById(anyLong());
    }

    @Test
    void givenAppUserIdShouldGetAllMovieItemAndResetSortingOrderAndSave() {

        MovieListItem firstMovieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        MovieListItem secondMovieListItem = MovieListItem
                .builder()
                .movieId(2L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(2)
                .build();

        MovieListItem thirdMovieListItem = MovieListItem
                .builder()
                .movieId(3L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(3)
                .build();

        List<MovieListItem> movieListItems = List.of(firstMovieListItem, secondMovieListItem, thirdMovieListItem);

        Mockito.when(this.movieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(anyLong())).thenReturn(movieListItems);

        this.movieListService.updateOrder(1L);

        Mockito.verify(this.movieListItemRepository).saveAll(movieListItems);

    }

    @Test
    void givenAppUserIdAndExistingMovieApiCodeWhenIsAlreadyInAppUserListShouldReturnTrue() throws Exception {

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(1L);

        MovieListItem movieListItem = new MovieListItem();
        movieListItem.setId(2L);

        Mockito.when(this.movieConnectorProxy.getByApiCode(anyString())).thenReturn(movieDTO);
        Mockito.when(this.movieListItemRepository.getByAppUserIdAndMovieId(anyLong(), anyLong())).thenReturn(movieListItem);

        Boolean testIsAlreadyInAppUserList = this.movieListService.isMovieApiCodeAlreadyInAppUserMovieList(1L, "tes");

        Mockito.verify(this.movieConnectorProxy).getByApiCode(anyString());
        Mockito.verify(this.movieListItemRepository).getByAppUserIdAndMovieId(anyLong(), anyLong());
        assertEquals(Boolean.TRUE, testIsAlreadyInAppUserList);
    }

    @Test
    void givenAppUserIdAndNonExistingMovieApiCodeWhenIsAlreadyInAppUserListShouldReturnTrue() throws Exception {

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(1L);

        Mockito.when(this.movieConnectorProxy.getByApiCode(anyString())).thenThrow(CustomNotFoundException.class);

        Boolean testIsAlreadyInAppUserList = this.movieListService.isMovieApiCodeAlreadyInAppUserMovieList(1L, "tes");
        assertEquals(Boolean.FALSE, testIsAlreadyInAppUserList);
    }

    @Test
    void givenAppUserIdAndNonExistingMovieApiCodeWhenIsAlreadyInAppUserListAndServiceNotAvailableShouldReturnTrue() throws Exception {

        Mockito.when(this.movieConnectorProxy.getByApiCode(anyString())).thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class,
                () -> this.movieListService.isMovieApiCodeAlreadyInAppUserMovieList(1L, "test"));

    }

    @Test
    void givenAppUserIdAndMovieItemIdAndNewSortingOrderOnTopWhenEditSortingOrderShouldChangeSortingOrderAndReturnBothEditedMovieItem()
            throws Exception {

        MovieListItem firstItem = new MovieListItem();
        firstItem.setId(1L);
        firstItem.setMovieId(1L);
        firstItem.setSortingOrder(1);
        firstItem.setAppUserId(1L);
        firstItem.setAddedAt(new Date());

        MovieListItem secondItem = new MovieListItem();
        secondItem.setId(2L);
        secondItem.setMovieId(1L);
        secondItem.setSortingOrder(2);
        secondItem.setAppUserId(1L);
        secondItem.setAddedAt(new Date());

        MovieListItem thirdItem = new MovieListItem();
        thirdItem.setId(3L);
        thirdItem.setMovieId(1L);
        thirdItem.setSortingOrder(3);
        thirdItem.setAppUserId(1L);
        thirdItem.setAddedAt(new Date());


        Mockito
                .when(this.movieListItemRepository.getReferenceById(anyLong()))
                .thenReturn(thirdItem);

        Mockito
                .when(this.movieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(anyLong()))
                .thenReturn(List.of(firstItem, secondItem, thirdItem));

        List<MovieListItem> testEditSortingOrder = this.movieListService.editSortingOrder(1L, 1L, 1);

        assertEquals(1, thirdItem.getSortingOrder());
        assertEquals(2, firstItem.getSortingOrder());
        assertEquals(3, secondItem.getSortingOrder());
    }

    @Test
    void givenAppUserIdAndMovieItemIdAndNewSortingOrderOnBottomWhenEditSortingOrderShouldChangeSortingOrderAndReturnBothEditedMovieItem()
            throws Exception {

        MovieListItem firstItem = new MovieListItem();
        firstItem.setId(1L);
        firstItem.setMovieId(1L);
        firstItem.setSortingOrder(1);
        firstItem.setAppUserId(1L);
        firstItem.setAddedAt(new Date());

        MovieListItem secondItem = new MovieListItem();
        secondItem.setId(2L);
        secondItem.setMovieId(1L);
        secondItem.setSortingOrder(2);
        secondItem.setAppUserId(1L);
        secondItem.setAddedAt(new Date());

        MovieListItem thirdItem = new MovieListItem();
        thirdItem.setId(3L);
        thirdItem.setMovieId(1L);
        thirdItem.setSortingOrder(3);
        thirdItem.setAppUserId(1L);
        thirdItem.setAddedAt(new Date());


        Mockito
                .when(this.movieListItemRepository.getReferenceById(anyLong()))
                .thenReturn(firstItem);

        Mockito
                .when(this.movieListItemRepository.getByAppUserIdOrderBySortingOrderAsc(anyLong()))
                .thenReturn(List.of(firstItem, secondItem, thirdItem));

        List<MovieListItem> testEditSortingOrder = this.movieListService.editSortingOrder(1L, 1L, 3);

        assertEquals(1, secondItem.getSortingOrder());
        assertEquals(2, thirdItem.getSortingOrder());
        assertEquals(3, firstItem.getSortingOrder());
    }

    @Test
    void givenAppUserIdAndInvalidMovieItemIdAndNewSortingOrderWhenEditSortingOrderShouldThrowNotFoundException() {

        Mockito
                .when(this.movieListItemRepository.getReferenceById(anyLong()))
                .thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.movieListService.editSortingOrder(1L, 1L, 1));
    }
}