package com.medialistmaker.list.filter;

import com.medialistmaker.list.connector.appuser.AppUserConnectorProxy;
import com.medialistmaker.list.dto.AppUserDTO;
import com.medialistmaker.list.utils.JwtTokenService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    JwtTokenService tokenService;

    @Mock
    AppUserConnectorProxy userConnectorProxy;

    @InjectMocks
    JwtAuthenticationFilter filter;

    @BeforeEach
    void beforeAllTests() {

        SecurityContextHolder.getContext().setAuthentication(null);

    }

    @Test
    void givenValidTokenWhenDoFilterShouldReturnUnAuthorized() throws Exception {

        AppUserDTO userDTO = new AppUserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("Username");
        userDTO.setPassword("Password");

        Mockito.when(this.tokenService.isTokenValid(anyString(), any())).thenReturn(Boolean.TRUE);
        Mockito.when(this.tokenService.getUsername(anyString())).thenReturn("1");
        Mockito.when(this.userConnectorProxy.getById(anyLong())).thenReturn(userDTO);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testestest");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        this.filter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_OK, response.getStatus());

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());

    }

    @Test
    void givenNoTokenWhenDoFilterShouldReturnUnAuthorized() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        this.filter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        assertNull(SecurityContextHolder.getContext().getAuthentication());

    }

    @Test
    void givenInvalidTokenWhenDoFilterShouldReturnUnAuthorized() throws Exception {

        AppUserDTO userDTO = new AppUserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("Username");
        userDTO.setPassword("Password");

        Mockito.when(this.tokenService.isTokenValid(anyString(), any())).thenReturn(Boolean.FALSE);
        Mockito.when(this.tokenService.getUsername(anyString())).thenReturn("1");
        Mockito.when(this.userConnectorProxy.getById(anyLong())).thenReturn(userDTO);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testestest");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        this.filter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void givenTokenWithInvalidUsernameWhenDoFilterShouldReturnUnAuthorized() throws Exception {

        Mockito.when(this.tokenService.getUsername(anyString())).thenReturn(null);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer testestest");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        this.filter.doFilter(request, response, filterChain);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

}