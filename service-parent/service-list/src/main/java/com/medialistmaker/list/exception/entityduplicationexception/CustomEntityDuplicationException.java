package com.medialistmaker.list.exception.entityduplicationexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CustomEntityDuplicationException extends Exception {

    public CustomEntityDuplicationException(String message) {
        super(message);
    }
}
