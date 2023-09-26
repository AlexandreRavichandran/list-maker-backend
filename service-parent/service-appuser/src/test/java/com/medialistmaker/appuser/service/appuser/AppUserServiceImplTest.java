package com.medialistmaker.appuser.service.appuser;

import com.medialistmaker.appuser.domain.AppUser;
import com.medialistmaker.appuser.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.appuser.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.appuser.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.appuser.repository.AppUserRepository;
import com.medialistmaker.appuser.utils.CustomEntityValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Mock
    AppUserRepository appUserRepository;

    @Mock
    CustomEntityValidator<AppUser> appUserEntityValidator;

    @InjectMocks
    AppUserServiceImpl appUserService;

    @Test
    void givenUsernameWhenGetByUsernameShouldReturnRelatedAppUser() throws CustomNotFoundException {

        AppUser user = AppUser
                .builder()
                .username("test")
                .password("test")
                .build();

        Mockito.when(this.appUserRepository.getByUsername(anyString())).thenReturn(user);

        AppUser testGetByUsername = this.appUserService.getByUsername("test");

        Mockito.verify(this.appUserRepository).getByUsername(anyString());
        assertNotNull(testGetByUsername);
        assertEquals(testGetByUsername, user);

    }

    @Test
    void givenInvalidUsernameWhenGetByUsernameShouldThrowNotFoundException() {

        Mockito.when(this.appUserRepository.getByUsername(anyString())).thenReturn(null);

        assertThrows(CustomNotFoundException.class, () -> this.appUserService.getByUsername("test"));

    }

    @Test
    void givenAppUserWhenAddAppUserShouldSaveAndReturnAppUser()
            throws CustomBadRequestException, CustomEntityDuplicationException {

        AppUser user = AppUser
                .builder()
                .username("test")
                .password("test")
                .build();

        Mockito.when(this.appUserRepository.save(user)).thenReturn(user);
        Mockito.when(this.appUserRepository.getByUsername(anyString())).thenReturn(null);
        Mockito.when(this.appUserEntityValidator.validateEntity(user)).thenReturn(emptyList());

        AppUser testAddAppUser = this.appUserService.add(user);

        Mockito.verify(this.appUserRepository).save(user);
        Mockito.verify(this.appUserRepository).getByUsername(anyString());
        Mockito.verify(this.appUserEntityValidator).validateEntity(user);

    }

    @Test
    void givenInvalidAppUserWhenAddAppUserShouldThrowBadRequestException() {

        AppUser user = AppUser
                .builder()
                .username("test")
                .password("test")
                .build();

        List<String> errorList = List.of("Error 1", "Error 2");
        Mockito.when(this.appUserEntityValidator.validateEntity(user)).thenReturn(errorList);

        assertThrows(CustomBadRequestException.class, () -> this.appUserService.add(user));

        Mockito.verify(this.appUserEntityValidator).validateEntity(user);
    }

    @Test
    void givenAppUserWithExistingUsernameWhenAddAppUserShouldThrowEntityDuplicationException() {

        AppUser user = AppUser
                .builder()
                .username("test")
                .password("test")
                .build();

        Mockito.when(this.appUserRepository.getByUsername(anyString())).thenReturn(user);
        Mockito.when(this.appUserEntityValidator.validateEntity(user)).thenReturn(emptyList());

        assertThrows(CustomEntityDuplicationException.class, () -> this.appUserService.add(user));

        Mockito.verify(this.appUserRepository).getByUsername(anyString());
        Mockito.verify(this.appUserEntityValidator).validateEntity(user);
    }

}