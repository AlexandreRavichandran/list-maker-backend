package com.medialistmaker.movie.exception.badrequestexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CustomBadRequestException extends Exception {

    private List<String> errorList = new ArrayList<>();

    public CustomBadRequestException(String message, List<String> errorList) {
        super(message);
        this.errorList = errorList;
    }

    public CustomBadRequestException(String message) {
        super(message);
    }

    public List<String> getErrorList() {
        return this.errorList;
    }
}
