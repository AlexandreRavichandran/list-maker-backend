package com.medialistmaker.list.service.movielistitem;

import com.medialistmaker.list.domain.MovieListItem;
import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
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
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class MovieListItemServiceImplTest {

    @Mock
    MovieListItemRepository movieListItemRepository;

    @Mock
    CustomEntityValidator<MovieListItem> movieListEntityValidator;

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
    void givenMovieListItemWhenAddMovieListItemShouldSaveAndReturnMovieListItem()
            throws CustomBadRequestException, CustomEntityDuplicationException {

        MovieListItem movieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.movieListEntityValidator.validateEntity(movieListItem)).thenReturn(emptyList());
        Mockito.when(this.movieListItemRepository.getByAppUserIdAndMovieId(anyLong(), anyLong())).thenReturn(null);
        Mockito.when(this.movieListItemRepository.save(movieListItem)).thenReturn(movieListItem);

        MovieListItem testAddMovieListItem = this.movieListService.add(movieListItem);

        Mockito.verify(this.movieListEntityValidator).validateEntity(movieListItem);
        Mockito.verify(this.movieListItemRepository).getByAppUserIdAndMovieId(anyLong(), anyLong());
        Mockito.verify(this.movieListItemRepository).save(movieListItem);

        assertEquals(testAddMovieListItem, movieListItem);
    }

    @Test
    void givenInvalidMovieListItemWhenAddMovieListItemShouldThrowBadRequestException() {

        MovieListItem movieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        List<String> errorList = List.of("Error 1", "Error 2");
        Mockito.when(this.movieListEntityValidator.validateEntity(movieListItem)).thenReturn(errorList);

        assertThrows(CustomBadRequestException.class, () -> this.movieListService.add(movieListItem));

        Mockito.verify(this.movieListEntityValidator).validateEntity(movieListItem);

    }

    @Test
    void givenMovieListItemWithAlreadyExistingMovieIdShouldThrowEntityDuplicationException() {

        MovieListItem movieListItem = MovieListItem
                .builder()
                .movieId(1L)
                .appUserId(1L)
                .addedAt(new Date())
                .sortingOrder(1)
                .build();

        Mockito.when(this.movieListEntityValidator.validateEntity(movieListItem)).thenReturn(emptyList());
        Mockito.when(this.movieListItemRepository
                        .getByAppUserIdAndMovieId(anyLong(), anyLong()))
                .thenReturn(movieListItem);

        assertThrows(CustomEntityDuplicationException.class, () -> this.movieListService.add(movieListItem));

        Mockito.verify(this.movieListEntityValidator).validateEntity(movieListItem);
        Mockito.verify(this.movieListItemRepository).getByAppUserIdAndMovieId(anyLong(), anyLong());
    }

    @Test
    void givenIdWhenDeleteByIdShouldDeleteAndReturnRelatedMovieListItem() throws CustomNotFoundException {

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

}