package com.medialistmaker.movie.exception.entityduplicationexception;

import com.medialistmaker.movie.dto.ErrorDTO;
import com.medialistmaker.movie.exception.badrequestexception.CustomBadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomEntityDuplicationExceptionHandler {

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ErrorDTO> generateCustomEntityDuplicationException(CustomEntityDuplicationException e) {
        ErrorDTO errorDTO = ErrorDTO
                .builder()
                .message(e.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
