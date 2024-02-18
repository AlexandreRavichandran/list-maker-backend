package com.medialistmaker.appuser.controller;

import com.medialistmaker.appuser.domain.AppUser;
import com.medialistmaker.appuser.dto.AppUserDTO;
import com.medialistmaker.appuser.dto.auth.JwtRequestDTO;
import com.medialistmaker.appuser.dto.auth.JwtResponseDTO;
import com.medialistmaker.appuser.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.appuser.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.appuser.exception.notauthorizedexception.CustomNotAuthorizedException;
import com.medialistmaker.appuser.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.appuser.service.appuser.AppUserServiceImpl;
import com.medialistmaker.appuser.utils.JwtTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AppUserServiceImpl appUserService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody JwtRequestDTO credentials)
            throws CustomNotAuthorizedException {

        try {
            Authentication authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername().toLowerCase(),
                            credentials.getPassword()
                    )
            );

            User loggedUser = (User) authentication.getPrincipal();

            AppUser user = this.appUserService.getByUsername(loggedUser.getUsername());

            return new ResponseEntity<>(
                    this.generateResponse(user),
                    HttpStatus.OK
            );

        } catch (AuthenticationException | CustomNotFoundException e) {
            throw new CustomNotAuthorizedException("Username or password is incorrect");
        }


    }


    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO> register(@RequestBody AppUserDTO appUserDTO)
            throws CustomBadRequestException, CustomEntityDuplicationException {

        AppUser appUser = this.modelMapper.map(appUserDTO, AppUser.class);

        AppUser createdAppUser = this.appUserService.add(appUser);

        return new ResponseEntity<>(
                this.generateResponse(createdAppUser),
                HttpStatus.CREATED
        );
    }

    private JwtResponseDTO generateResponse(AppUser appUser) {
        String token = this.tokenService.generateToken(new HashMap<>(), appUser.getId().toString());
        Date expiresAt = this.tokenService.getTokenExpirationDate(token);

        return new JwtResponseDTO(token, appUser.getUsername(), expiresAt);
    }
}
