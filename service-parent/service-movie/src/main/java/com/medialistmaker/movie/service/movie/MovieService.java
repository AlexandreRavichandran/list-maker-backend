package com.medialistmaker.movie.service.movie;

import com.medialistmaker.movie.domain.Movie;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.movie.exception.notfoundexception.CustomNotFoundException;

import java.util.List;

public interface MovieService {

    List<Movie> browseByIds(List<Long> movieIds);

    Movie readById(Long movieId) throws CustomNotFoundException;

    Movie readByApiCode(String apiCode) throws CustomNotFoundException;

    Movie add(Movie movie) throws CustomBadRequestException, CustomEntityDuplicationException;

    Movie deleteById(Long movieId) throws CustomNotFoundException;
}
