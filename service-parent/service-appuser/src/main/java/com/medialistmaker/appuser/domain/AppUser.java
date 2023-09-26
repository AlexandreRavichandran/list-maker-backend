package com.medialistmaker.appuser.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@AllArgsConstructor
public class AppUser {

    public AppUser() {
        //Private constructor
    }

    @Id
    @SequenceGenerator(name = "appuser_id_sequence", sequenceName = "appuser_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appuser_id_sequence")
    private Long id;

    @NotBlank(message = "Username is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;

}
