package com.medialistmaker.music.exception.badrequestexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomBadRequestException extends Exception {

    private final List<String> errorList;

    public CustomBadRequestException(String message, List<String> errorList) {
        super(message);
        this.errorList = errorList;
    }

    public List<String> getErrorList() {
        return this.errorList;
    }
}
