package com.medialistmaker.appuser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
public class ErrorDTO {

    public ErrorDTO() {
        //private constructor
    }

    private String message;
    private List<String> errorList;
}
