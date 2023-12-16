package com.medialistmaker.movie.service.movie;

import com.medialistmaker.movie.connector.omdb.OmdbConnectorProxy;
import com.medialistmaker.movie.domain.Movie;
import com.medialistmaker.movie.dto.externalapi.omdbapi.item.MovieElementDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.movie.repository.MovieRepository;
import com.medialistmaker.movie.utils.CustomEntityValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    MovieRepository movieRepository;

    @Mock
    CustomEntityValidator<Movie> movieEntityValidator;

    @Mock
    OmdbConnectorProxy connectorProxy;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    MovieServiceImpl movieService;

    @Test
    void givenIdListWhenBrowseByIdsShouldReturnRelatedMovieList() {

        Movie firstMovie = Movie.builder().id(1L).title("First movie").releasedAt(2000).pictureUrl("http://movie1.jpg")
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

        List<Movie> movieList = new ArrayList<>(List.of(firstMovie, secondMovie));

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

    @Test
    void givenApiCodeWhenAddByApiCodeShouldSaveAndReturnRelatedMovie() throws Exception {

        MovieElementDTO elementDTO = new MovieElementDTO();
        elementDTO.setTitle("Movie title");
        elementDTO.setApiCode("0001");
        elementDTO.setDuration("120m");
        elementDTO.setDirector("Realisator");
        elementDTO.setSynopsis("Synopsis");
        elementDTO.setPictureUrl("www.picture.com");

        Movie movie = Movie.builder()
                .title(elementDTO.getTitle())
                .apiCode(elementDTO.getApiCode())
                .pictureUrl(elementDTO.getPictureUrl())
                .build();

        Mockito.when(this.movieRepository.getByApiCode(anyString())).thenReturn(null);
        Mockito.when(this.connectorProxy.getByApiCode(anyString())).thenReturn(elementDTO);
        Mockito.when(this.movieRepository.save(any())).thenReturn(movie);

        Movie testAddByApiCode = this.movieService.addByApiCode("test");

        Mockito.verify(this.movieRepository).getByApiCode(anyString());
        Mockito.verify(this.connectorProxy).getByApiCode(anyString());
        Mockito.verify(this.movieRepository).save(any());
        assertNotNull(testAddByApiCode);
        assertEquals(elementDTO.getApiCode(), testAddByApiCode.getApiCode());

    }

    @Test
    void givenInvalidApiCodeWhenAddByApiCodeShouldThrowBadRequestException() throws Exception {

        Mockito.when(this.movieRepository.getByApiCode(anyString())).thenReturn(null);
        Mockito.when(this.connectorProxy.getByApiCode(anyString())).thenReturn(null);

        assertThrows(CustomBadRequestException.class, () -> this.movieService.addByApiCode("test"));
        Mockito.verify(this.movieRepository).getByApiCode(anyString());
        Mockito.verify(this.connectorProxy).getByApiCode(anyString());

    }

    @Test
    void givenApiCodeWhenAddByApiCodeAndApiNotAvailableShouldThrowServiceNotAvailableException() throws Exception {

        Mockito.when(this.movieRepository.getByApiCode(anyString())).thenReturn(null);
        Mockito.when(this.connectorProxy.getByApiCode(anyString())).thenThrow(ServiceNotAvailableException.class);

        assertThrows(ServiceNotAvailableException.class, () -> this.movieService.addByApiCode("test"));
        Mockito.verify(this.movieRepository).getByApiCode(anyString());
        Mockito.verify(this.connectorProxy).getByApiCode(anyString());

    }

    @Test
    void givenExistingApiCodeWhenAddByApiCodeShouldGetMovieFromDatabase() throws Exception {

        Movie movie = Movie.builder().id(1L).apiCode("test").pictureUrl("test.com").releasedAt(1993).build();
        Mockito.when(this.movieRepository.getByApiCode(anyString())).thenReturn(movie);

        Movie testAddByApiCode = this.movieService.addByApiCode("test");

        Mockito.verify(this.movieRepository).getByApiCode(anyString());

        assertNotNull(testAddByApiCode);
        assertEquals(movie.getApiCode(), testAddByApiCode.getApiCode());

    }
}