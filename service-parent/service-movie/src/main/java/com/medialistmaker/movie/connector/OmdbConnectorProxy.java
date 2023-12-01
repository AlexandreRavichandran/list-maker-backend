package com.medialistmaker.movie.connector;

import com.medialistmaker.movie.dto.externalapi.omdbapi.collection.MovieElementListDTO;
import com.medialistmaker.movie.dto.externalapi.omdbapi.item.MovieElementDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OmdbConnectorProxy {

    @Value("${omdb.apikey}")
    private String omdbApiKey;

    @Autowired
    OmdbConnector omdbConnector;

    public MovieElementDTO getByApiCode(String apiCode) throws CustomBadRequestException, ServiceNotAvailableException {
        return this.omdbConnector.getMovieByApiCode(apiCode, omdbApiKey, "full");
    }

    public MovieElementListDTO getByQuery(String query) throws CustomBadRequestException, ServiceNotAvailableException {
        return this.omdbConnector.getMoviesByQuery(query, omdbApiKey);
    }
}
