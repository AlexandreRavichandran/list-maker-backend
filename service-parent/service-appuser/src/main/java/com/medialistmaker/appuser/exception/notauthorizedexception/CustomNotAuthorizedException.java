package com.medialistmaker.appuser.exception.notauthorizedexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CustomNotAuthorizedException extends Exception {

    public CustomNotAuthorizedException(String message) {
        super(message);
    }
}
