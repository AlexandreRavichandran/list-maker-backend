package com.medialistmaker.list.exception.badrequestexception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
public class CustomBadRequestException extends Exception {

    private List<String> errorList;

    public CustomBadRequestException(String message, List<String> errorList) {
        super(message);
        this.errorList = errorList;
    }

    public CustomBadRequestException(String message) {
        super(message);
    }

}
