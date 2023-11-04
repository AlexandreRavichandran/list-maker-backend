package com.medialistmaker.movie.configuration;

import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.movie.exception.servicenotavailableexception.ServiceNotAvailableException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomFeignDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {

        if(response.status() >= 400 && response.status() <=499) {
            return new CustomBadRequestException(response.reason());
        }

        return new ServiceNotAvailableException(response.reason());

    }
}
