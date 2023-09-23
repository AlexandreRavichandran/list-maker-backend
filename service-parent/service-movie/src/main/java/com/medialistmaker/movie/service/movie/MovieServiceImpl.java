package com.medialistmaker.movie.service.movie;

import com.medialistmaker.movie.domain.Movie;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.movie.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.movie.repository.MovieRepository;
import com.medialistmaker.movie.utils.CustomEntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    MovieRepository movieRepository;

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
            throw new CustomNotFoundException("NOT FOUND");
        }

        return movie;

    }

    @Override
    public Movie readByApiCode(String apiCode) throws CustomNotFoundException {

        Movie movie = this.movieRepository.getByApiCode(apiCode);

        if (isNull(movie)) {
            throw new CustomNotFoundException("NOT FOUND");
        }

        return movie;
    }

    @Override
    public Movie add(Movie movie) throws CustomBadRequestException, CustomEntityDuplicationException {

        List<String> movieErrors = this.movieValidator.validateEntity(movie);

        if (Boolean.FALSE.equals(movieErrors.isEmpty())) {
            throw new CustomBadRequestException("Bad request", movieErrors);
        }

        Movie isApiCodeAlreadyUsed = this.movieRepository.getByApiCode(movie.getApiCode());

        if (nonNull(isApiCodeAlreadyUsed)) {
            throw new CustomEntityDuplicationException("Api code already exists");
        }

        return this.movieRepository.save(movie);
    }

    @Override
    public Movie deleteById(Long movieId) throws CustomNotFoundException {

        Movie movie = this.movieRepository.getReferenceById(movieId);

        if (isNull(movie)) {
            throw new CustomNotFoundException("Not found");
        }

        this.movieRepository.delete(movie);

        return movie;

    }
}
