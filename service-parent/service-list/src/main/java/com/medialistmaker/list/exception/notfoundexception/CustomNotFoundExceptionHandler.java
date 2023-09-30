package com.medialistmaker.list.exception.notfoundexception;

import com.medialistmaker.list.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomNotFoundExceptionHandler {

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<ErrorDTO> generateCustomNotFoundException(CustomNotFoundException e) {
        ErrorDTO errorDTO = ErrorDTO
                .builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

}
