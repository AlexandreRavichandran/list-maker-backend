package com.medialistmaker.movie.service.externalapi.omdb;

import com.medialistmaker.movie.dto.externalapi.omdbapi.collection.MovieElementListDTO;
import com.medialistmaker.movie.dto.externalapi.omdbapi.item.MovieElementDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.service.externalapi.AbstractExternalApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class OmdbExternalApiServiceImpl extends AbstractExternalApiService<MovieElementDTO, MovieElementListDTO> {

    @Value("${omdb.apikey}")
    private String omdbApiKey;

    @Override
    protected String getBaseUrl() {
        return "http://www.omdbapi.com/";
    }

    @Override
    protected String getResourceUrl() {
        return "";
    }

    @Override
    protected Class<MovieElementDTO> getItemClassType() {
        return MovieElementDTO.class;
    }

    @Override
    protected Class<MovieElementListDTO> getCollectionClassType() {
        return MovieElementListDTO.class;
    }

    public MovieElementListDTO getByMovieName(String movieName) throws CustomBadRequestException {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("s", movieName);
        parameters.put("apiKey", omdbApiKey);

        this.setParameters(parameters);

        return this.executeRequestCollection();

    }

    public MovieElementDTO getByApiCode(String apiCode) throws CustomBadRequestException {

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("i", apiCode);
        parameters.put("apiKey", omdbApiKey);

        this.setParameters(parameters);

        return this.executeRequestItem();

    }
}
