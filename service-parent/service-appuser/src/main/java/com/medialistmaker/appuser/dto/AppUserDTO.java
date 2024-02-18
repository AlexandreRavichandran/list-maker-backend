package com.medialistmaker.appuser.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AppUserDTO {

    private Long id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

}
