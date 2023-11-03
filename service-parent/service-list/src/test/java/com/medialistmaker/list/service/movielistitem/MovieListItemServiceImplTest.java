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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;

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
    void givenMovieListItemAddWhenAddMovieListItemShouldSaveAndReturnMovieListItem()
            throws CustomBadRequestException, CustomNotFoundException,
            CustomEntityDuplicationException, ServiceNotAvailableException {

        MovieListItemAddDTO listItemAddDTO = new MovieListItemAddDTO();
        listItemAddDTO.setApiCode("XXXX");

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(1L);

        MovieListItem movieListItem = new MovieListItem();
        movieListItem.setMovieId(movieDTO.getId());
        movieListItem.setAppUserId(1L);


        Mockito.when(this.movieConnectorProxy.getByApiCode(anyString())).thenReturn(movieDTO);
        Mockito.when(this.movieListItemRepository.getByAppUserIdAndMovieId(anyLong(), anyLong())).thenReturn(null);
        Mockito.when(this.movieConnectorProxy.saveByApiCode(anyString())).thenReturn(movieDTO);
        Mockito.when(this.movieListItemRepository.save(any())).thenReturn(movieListItem);

        MovieListItem testAddMovieListItem = this.movieListService.add(listItemAddDTO);

        Mockito.verify(this.movieConnectorProxy).getByApiCode(anyString());
        Mockito.verify(this.movieListItemRepository).getByAppUserIdAndMovieId(anyLong(), anyLong());
        Mockito.verify(this.movieConnectorProxy).saveByApiCode(anyString());
        Mockito.verify(this.movieListItemRepository).save(any());

        assertEquals(movieDTO.getId(),testAddMovieListItem.getMovieId());
    }

    @Test
    void givenMovieListItemAddWithInvalidApiCodeWhenAddMovieListItemShouldThrowBadRequestException() throws Exception {

        MovieListItemAddDTO listItemAddDTO = new MovieListItemAddDTO();
        listItemAddDTO.setApiCode("XXXX");

        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(1L);

        MovieListItem movieListItem = new MovieListItem();
        movieListItem.setMovieId(movieDTO.getId());
        movieListItem.setAppUserId(1L);


        Mockito.when(this.movieConnectorProxy.getByApiCode(anyString())).thenThrow(CustomNotFoundException.class);

        assertThrows(CustomBadRequestException.class, () -> this.movieListService.add(listItemAddDTO));

        Mockito.verify(this.movieConnectorProxy).getByApiCode(anyString());

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


        Mockito.when(this.movieConnectorProxy.getByApiCode(anyString())).thenReturn(movieDTO);
        Mockito.when(this.movieListItemRepository.getByAppUserIdAndMovieId(anyLong(), anyLong())).thenReturn(null);
        Mockito.when(this.movieConnectorProxy.saveByApiCode(anyString())).thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class, () -> this.movieListService.add(listItemAddDTO));

        Mockito.verify(this.movieConnectorProxy).getByApiCode(anyString());
        Mockito.verify(this.movieListItemRepository).getByAppUserIdAndMovieId(anyLong(), anyLong());
        Mockito.verify(this.movieConnectorProxy).saveByApiCode(anyString());
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
}