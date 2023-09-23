package com.medialistmaker.movie.exception.badrequestexception;

import com.medialistmaker.movie.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomBadRequestExceptionHandler {

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ErrorDTO> generateCustomBadRequestException(CustomBadRequestException e) {
        ErrorDTO errorDTO = ErrorDTO
                .builder()
                .message(e.getMessage())
                .errorList(e.getErrorList())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
