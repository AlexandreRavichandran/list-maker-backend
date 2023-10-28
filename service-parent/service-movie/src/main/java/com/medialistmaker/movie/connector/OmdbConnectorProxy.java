package com.medialistmaker.movie.connector;

import com.medialistmaker.movie.dto.externalapi.omdbapi.collection.MovieElementListDTO;
import com.medialistmaker.movie.dto.externalapi.omdbapi.item.MovieElementDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OmdbConnectorProxy {

    @Value("${omdb.apikey}")
    private static final String OMDB_API_KEY = "";

    @Autowired
    OmdbConnector omdbConnector;

    public MovieElementDTO getByApiCode(String apiCode) throws CustomBadRequestException {
        return this.omdbConnector.getMovieByApiCode(apiCode, OMDB_API_KEY);
    }

    public MovieElementListDTO getByQuery(String query) throws CustomBadRequestException {
        return this.omdbConnector.getMoviesByQuery(query, OMDB_API_KEY);
    }
}
