package com.medialistmaker.music.exception.unsupportedtypeexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedTypeException extends Exception {

    public UnsupportedTypeException() {
        super("Invalid type");
    }

}
