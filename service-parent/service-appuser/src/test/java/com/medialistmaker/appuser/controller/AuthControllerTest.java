package com.medialistmaker.appuser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.medialistmaker.appuser.domain.AppUser;
import com.medialistmaker.appuser.dto.AppUserDTO;
import com.medialistmaker.appuser.dto.auth.JwtRequestDTO;
import com.medialistmaker.appuser.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.appuser.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.appuser.service.appuser.AppUserServiceImpl;
import com.medialistmaker.appuser.utils.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @SpyBean
    ModelMapper modelMapper;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    AppUserServiceImpl appUserService;

    @MockBean
    JwtTokenService tokenService;

    @Test
    void givenUsernameAndPasswordWhenLoginShouldReturnJwtResponseAndReturn200() throws Exception {

        JwtRequestDTO jwtRequestDTO = new JwtRequestDTO();
        jwtRequestDTO.setUsername("test");
        jwtRequestDTO.setPassword("test");

        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setUsername("Username");
        appUser.setPassword("Password");

        User user = new User("Username", "Password", new ArrayList<>());

        Mockito
                .when(this.authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(user , "element"));

        Mockito.when(this.tokenService.generateToken(anyMap(), anyString())).thenReturn("jwttokentest");
        Mockito.when(this.appUserService.getByUsername(anyString())).thenReturn(appUser);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false
                                                )
                                                .writeValueAsString(jwtRequestDTO)
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.token", equalTo("jwttokentest"))
                );

    }

    @Test
    void givenUsernameAndInvalidPasswordWhenLoginShouldReturnBadCredentialExceptionAndReturn401() throws Exception {

        Mockito.when(this.authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        JwtRequestDTO jwtRequestDTO = new JwtRequestDTO();
        jwtRequestDTO.setUsername("test");
        jwtRequestDTO.setPassword("test");

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false
                                                )
                                                .writeValueAsString(jwtRequestDTO)
                                )
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }

    @Test
    void givenAppUserDTOWhenRegisterShouldSaveAndAndReturnAppUserAndReturn201() throws Exception {

        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername("username");
        appUserDTO.setPassword("test");

        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setUsername("username");
        appUser.setPassword("test");

        Mockito.when(this.tokenService.generateToken(anyMap(), anyString())).thenReturn("jwttokentest");
        Mockito.when(this.appUserService.add(any())).thenReturn(appUser);

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false
                                                )
                                                .writeValueAsString(appUserDTO)
                                )
                )
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.username", equalTo(appUser.getUsername()))
                );
    }

    @Test
    void givenInvalidAppUserDTOWhenRegisterShouldThrowBadRequestExceptionAndReturn400() throws Exception {

        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername("username");
        appUserDTO.setPassword("test");

        AppUser appUser = new AppUser();
        appUser.setUsername("username");
        appUser.setPassword("test");

        List<String> errorList = List.of("Error 1", "Error 2");

        Mockito.when(this.tokenService.generateToken(anyMap(), anyString())).thenReturn("jwttokentest");
        Mockito
                .when(this.appUserService.add(appUser))
                .thenThrow(new CustomBadRequestException("Bad request", errorList));

        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false
                                                )
                                                .writeValueAsString(appUserDTO)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(CustomBadRequestException.class, result.getResolvedException()))
                .andExpect(result -> assertEquals(2, ((CustomBadRequestException) result.getResolvedException()).getErrorList().size()));
    }

    @Test
    void givenAppUserDTOWithExistingUsernameWhenRegisterShouldThrowEntityDuplicationExceptionAndReturn400() throws Exception {

        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername("username");
        appUserDTO.setPassword("test");

        AppUser appUser = new AppUser();
        appUser.setUsername("username");
        appUser.setPassword("test");

        Mockito.when(this.tokenService.generateToken(anyMap(), anyString())).thenReturn("jwttokentest");
        Mockito
                .when(this.appUserService.add(appUser))
                .thenThrow(new CustomEntityDuplicationException("Bad request"));


        this.mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        new ObjectMapper()
                                                .configure(
                                                        SerializationFeature.WRAP_ROOT_VALUE,
                                                        false
                                                )
                                                .writeValueAsString(appUserDTO)
                                )
                )
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(CustomEntityDuplicationException.class, result.getResolvedException()));

    }
}