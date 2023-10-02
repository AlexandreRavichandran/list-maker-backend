package com.medialistmaker.appuser.controller;

import com.medialistmaker.appuser.service.appuser.AppUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration
@WebMvcTest(AppUserController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AppUserServiceImpl appUserService;

    @Test
    void givenUsernameWhenGetByUsernameShouldReturnUserAndReturn200() throws Exception {

        UserDetails user = new User("Username", "Password", new ArrayList<>());

        Mockito.when(this.appUserService.loadUserByUsername(anyString())).thenReturn(user);

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get(
                                        "/api/appusers/username/{username}",
                                        user.getUsername()
                                )
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.username", equalTo(user.getUsername())),
                        jsonPath("$.password", equalTo(user.getPassword()))
                );

    }

    @Test
    void givenInvalidUsernameWhenGetByUsernameShouldReturn401() throws Exception {

        Mockito.when(this.appUserService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("Not found"));

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(
                                "/api/appusers/username/{username}",
                                "Test"
                        )
                )
                .andDo(print())
                .andExpect(
                        status().isUnauthorized()
                );
    }
}