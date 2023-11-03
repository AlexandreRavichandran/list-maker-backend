package com.medialistmaker.music.exception.servicenotavailableexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
public class ServiceNotAvailableException extends Exception {

    public ServiceNotAvailableException(String message) {
        super(message);
    }
}
