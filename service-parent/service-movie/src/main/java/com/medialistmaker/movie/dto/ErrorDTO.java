package com.medialistmaker.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ErrorDTO {

    private String message;

    private List<String> errorList;
}
