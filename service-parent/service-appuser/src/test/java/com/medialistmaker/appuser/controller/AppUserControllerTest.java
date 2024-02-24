package com.medialistmaker.appuser.controller;

import com.medialistmaker.appuser.domain.AppUser;
import com.medialistmaker.appuser.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.appuser.service.appuser.AppUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AppUserController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppUserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AppUserServiceImpl appUserService;

    @Test
    void givenUsernameWhenGetByUsernameShouldReturnUserAndReturn200() throws Exception {

        AppUser user = new AppUser();
        user.setUsername("test");
        user.setPassword("password");

        Mockito.when(this.appUserService.getByUsername(anyString())).thenReturn(user);

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
    void givenInvalidUsernameWhenGetByUsernameShouldReturn404() throws Exception {

        Mockito.when(this.appUserService.getByUsername(anyString())).thenThrow(new CustomNotFoundException("Not found"));

        this.mockMvc
                .perform(
                        MockMvcRequestBuilders.get(
                                "/api/appusers/username/{username}",
                                "Test"
                        )
                )
                .andDo(print())
                .andExpect(
                        status().isNotFound()
                );
    }

    @Test
    void givenIdWhenGetByIdShouldReturnUserAndReturn200() throws Exception {

        AppUser user = new AppUser();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");

        Mockito.when(this.appUserService.getById(anyLong())).thenReturn(user);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(
                                "/api/appusers/{id}",
                                1L
                        )
                )
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", equalTo(user.getId().intValue()))
                );

    }

    @Test
    void givenInvalidIdWhenGetByIdShouldReturn404() throws Exception {

        Mockito.when(this.appUserService.getById(anyLong())).thenThrow(CustomNotFoundException.class);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.get(
                                "/api/appusers/{id}",
                                1L
                        )
                )
                .andDo(print())
                .andExpectAll(
                        status().isNotFound()
                );

    }
}