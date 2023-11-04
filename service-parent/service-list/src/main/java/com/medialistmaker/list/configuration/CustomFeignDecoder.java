package com.medialistmaker.list.configuration;

import com.medialistmaker.list.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.list.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.list.exception.servicenotavailableexception.ServiceNotAvailableException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomFeignDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {

        return switch (response.status()) {
            case 404 -> new CustomNotFoundException(response.reason());
            case 400 -> new CustomBadRequestException(response.reason());
            default -> new ServiceNotAvailableException(response.reason());
        };

    }
}
