package com.medialistmaker.movie.service.movie;

import com.medialistmaker.movie.connector.OmdbConnectorProxy;
import com.medialistmaker.movie.domain.Movie;
import com.medialistmaker.movie.dto.externalapi.omdbapi.item.MovieElementDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.movie.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import com.medialistmaker.movie.repository.MovieRepository;
import com.medialistmaker.movie.utils.CustomEntityValidator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    OmdbConnectorProxy omdbConnectorProxy;

    @Autowired
    CustomEntityValidator<Movie> movieValidator;

    @Override
    public List<Movie> browseByIds(List<Long> movieIds) {

        return this.movieRepository.getByIds(movieIds);

    }

    @Override
    public Movie readById(Long movieId) throws CustomNotFoundException {

        Movie movie = this.movieRepository.getReferenceById(movieId);

        if (isNull(movie)) {
            log.error("Movie with id {} not found", movieId);
            throw new CustomNotFoundException("NOT FOUND");
        }

        return movie;

    }

    @Override
    public Movie readByApiCode(String apiCode) throws CustomNotFoundException {

        Movie movie = this.movieRepository.getByApiCode(apiCode);

        if (isNull(movie)) {
            log.error("Movie with api code {} not found", apiCode);
            throw new CustomNotFoundException("NOT FOUND");
        }

        return movie;
    }

    @Override
    public Movie addByApiCode(String apiCode) throws CustomBadRequestException, ServiceNotAvailableException {

        Movie isMovieAlreadyExist = this.movieRepository.getByApiCode(apiCode);

        if(nonNull(isMovieAlreadyExist)) {
            return isMovieAlreadyExist;
        }

        MovieElementDTO movieElementDTO = this.omdbConnectorProxy.getByApiCode(apiCode);

        if(isNull(movieElementDTO)) {
            throw new CustomBadRequestException("Movie not exists");
        }

        Movie movie = this.modelMapper.map(movieElementDTO, Movie.class);

        return this.add(movie);
    }

    private Movie add(Movie movie) throws CustomBadRequestException {

        List<String> movieErrors = this.movieValidator.validateEntity(movie);

        if (Boolean.FALSE.equals(movieErrors.isEmpty())) {
            log.error("Movie not valid: {}", movieErrors);
            throw new CustomBadRequestException("Bad request", movieErrors);
        }

        return this.movieRepository.save(movie);
    }

    @Override
    public Movie deleteById(Long movieId) throws CustomNotFoundException {

        Movie movie = this.movieRepository.getReferenceById(movieId);

        if (isNull(movie)) {
            log.error("Error on deleting movie with id {}: Movie not exists", movieId);
            throw new CustomNotFoundException("Not found");
        }

        this.movieRepository.delete(movie);

        return movie;

    }
}
