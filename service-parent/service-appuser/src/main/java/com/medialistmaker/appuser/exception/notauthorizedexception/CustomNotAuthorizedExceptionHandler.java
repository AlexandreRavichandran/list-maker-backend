package com.medialistmaker.appuser.exception.notauthorizedexception;

import com.medialistmaker.appuser.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomNotAuthorizedExceptionHandler {

    @ExceptionHandler(CustomNotAuthorizedException.class)
    public ResponseEntity<ErrorDTO> generateCustomNotAuthorizedException(CustomNotAuthorizedException e) {

        ErrorDTO errorDTO = ErrorDTO
                .builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.UNAUTHORIZED);

    }
}
