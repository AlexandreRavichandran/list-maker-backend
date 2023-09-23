package com.medialistmaker.movie.exception.notfoundexception;

import com.medialistmaker.movie.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CustomNotFoundExceptionHandler {

    public ResponseEntity<ErrorDTO> generateCustomNotFoundException(CustomNotFoundException e) {
        ErrorDTO errorDTO = ErrorDTO
                .builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

}
