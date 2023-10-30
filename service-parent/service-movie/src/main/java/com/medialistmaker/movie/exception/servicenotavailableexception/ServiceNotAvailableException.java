package com.medialistmaker.movie.exception.servicenotavailableexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceNotAvailableException  extends Exception {

    public ServiceNotAvailableException(String message) {
        super(message);
    }
}
