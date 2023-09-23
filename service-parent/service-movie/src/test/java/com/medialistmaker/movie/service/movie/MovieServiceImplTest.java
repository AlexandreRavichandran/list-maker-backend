package com.medialistmaker.movie.service.movie;

import com.medialistmaker.movie.domain.Movie;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.movie.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.movie.repository.MovieRepository;
import com.medialistmaker.movie.utils.CustomEntityValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    MovieRepository movieRepository;

    @InjectMocks
    MovieServiceImpl movieService;

    @Mock
    CustomEntityValidator<Movie> movieEntityValidator;

    @Test
    void givenIdListWhenBrowseByIdsShouldReturnRelatedMovieList() {

        Movie firstMovie = Movie
                .builder()
                .id(1L)
                .title("First movie")
                .releasedAt(2000)
                .pictureUrl("http://movie1.jpg")
                .apiCode("MOVIE1")
                .build();

        Movie secondMovie = Movie
                .builder()
                .id(2L)
                .title("Second movie")
                .releasedAt(2002)
                .pictureUrl("http://movie2.jpg")
                .apiCode("MOVIE2")
                .build();

        List<Movie> movieList = List.of(firstMovie, secondMovie);

        Mockito.when(this.movieRepository.getByIds(anyList())).thenReturn(movieList);

        List<Movie> testGetByIds = this.movieService.browseByIds(List.of(1L, 2L));

        Mockito.verify(this.movieRepository).getByIds(anyList());
        assertEquals(2, movieList.size());
        assertTrue(testGetByIds.containsAll(movieList));
    }

    @Test
    void givenIdWhenReadByIdShouldReturnRelatedMovie() throws CustomNotFoundException {

        Movie movie = Movie
                .builder()
                .id(1L)
                .title("movie")
                .releasedAt(2000)
                .pictureUrl("http://movie.jpg")
                .apiCode("MOVIE")
                .build();

        Mockito.when(this.movieRepository.getReferenceById(anyLong())).thenReturn(movie);

        Movie testGetById = this.movieService.readById(movie.getId());

        Mockito.verify(this.movieRepository).getReferenceById(anyLong());
        assertNotNull(testGetById);
        assertEquals(movie, testGetById);
    }

    @Test
    void givenInvalidIdWhenReadByIdShouldThrowNotFoundException() {

        Mockito.when(this.movieRepository.getReferenceById(anyLong())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.movieService.readById(1L));
        Mockito.verify(this.movieRepository).getReferenceById(anyLong());

    }

    @Test
    void givenApiCodeWhenReadByApiCodeShouldReturnRelatedMovie() throws CustomNotFoundException {

        Movie movie = Movie
                .builder()
                .id(1L)
                .title("movie")
                .releasedAt(2000)
                .pictureUrl("http://movie.jpg")
                .apiCode("MOVIE")
                .build();

        Mockito.when(this.movieRepository.getByApiCode(anyString())).thenReturn(movie);

        Movie testGetByApiCode = this.movieService.readByApiCode(movie.getApiCode());

        Mockito.verify(this.movieRepository).getByApiCode(anyString());
        assertNotNull(testGetByApiCode);
        assertEquals(movie, testGetByApiCode);
    }

    @Test
    void givenInvalidApiCodeWhenReadByApiCodeShouldThrowNotFoundException() {

        Mockito.when(this.movieRepository.getByApiCode(anyString())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.movieService.readByApiCode("TEST"));

        Mockito.verify(this.movieRepository).getByApiCode(anyString());
    }

    @Test
    void givenMovieWhenAddMovieShouldSaveAndReturnMovie()
            throws CustomBadRequestException, CustomEntityDuplicationException {

        Movie movie = Movie
                .builder()
                .title("movie")
                .releasedAt(2000)
                .pictureUrl("http://movie.jpg")
                .apiCode("MOVIE")
                .build();

        Mockito.when(this.movieRepository.save(movie)).thenReturn(movie);
        Mockito.when(this.movieEntityValidator.validateEntity(movie)).thenReturn(emptyList());
        Mockito.when(this.movieRepository.getByApiCode(anyString())).thenReturn(null);
        this.movieService.add(movie);

        Mockito.verify(this.movieRepository).save(movie);
        Mockito.verify(this.movieEntityValidator).validateEntity(movie);
        Mockito.verify(this.movieRepository).getByApiCode(anyString());
    }

    @Test
    void givenInvalidMovieWhenAddMovieShouldThrowBadRequestException() {

        Movie movie = Movie
                .builder()
                .title("movie")
                .releasedAt(2000)
                .pictureUrl("http://movie.jpg")
                .apiCode("MOVIE")
                .build();

        List<String> errorList = List.of("Error 1", "Error 2");
        Mockito.when(this.movieEntityValidator.validateEntity(movie)).thenReturn(errorList);

        assertThrows(CustomBadRequestException.class, () -> this.movieService.add(movie));

        Mockito.verify(this.movieEntityValidator).validateEntity(movie);
    }

    @Test
    void givenMovieWithExistingApiCodeWhenAddMovieShouldThrowEntityDuplicationException() {

        Movie movie = Movie
                .builder()
                .title("movie")
                .releasedAt(2000)
                .pictureUrl("http://movie.jpg")
                .apiCode("MOVIE")
                .build();

        Mockito.when(this.movieEntityValidator.validateEntity(movie)).thenReturn(emptyList());
        Mockito.when(this.movieRepository.getByApiCode(anyString())).thenReturn(movie);

        assertThrows(CustomEntityDuplicationException.class, () -> this.movieService.add(movie));

        Mockito.verify(this.movieRepository).getByApiCode(anyString());
        Mockito.verify(this.movieEntityValidator).validateEntity(movie);
    }

    @Test
    void givenIdWhenDeleteByIdShouldDeleteAndReturnRelatedMovie() throws CustomNotFoundException {

        Movie movie = Movie
                .builder()
                .title("movie")
                .releasedAt(2000)
                .pictureUrl("http://movie.jpg")
                .apiCode("MOVIE")
                .build();

        Mockito.when(this.movieRepository.getReferenceById(anyLong())).thenReturn(movie);

        Movie testDeleteById = this.movieService.deleteById(1L);

        Mockito.verify(this.movieRepository).getReferenceById(anyLong());
        Mockito.verify(this.movieRepository).delete(movie);

        assertNotNull(testDeleteById);
        assertEquals(movie, testDeleteById);

    }

    @Test
    void givenInvalidIdWhenDeleteByIdShouldThrowNotFoundException() {

        Mockito.when(this.movieRepository.getReferenceById(anyLong())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.movieService.deleteById(1L));

        Mockito.verify(this.movieRepository).getReferenceById(anyLong());

    }
}