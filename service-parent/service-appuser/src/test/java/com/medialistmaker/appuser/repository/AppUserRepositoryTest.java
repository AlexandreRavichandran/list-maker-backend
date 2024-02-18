package com.medialistmaker.appuser.repository;

import com.medialistmaker.appuser.domain.AppUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AppUserRepositoryTest {

    @Autowired
    AppUserRepository appUserRepository;

    @Test
    void givenUsernameWhenGetByUsernameShouldReturnRelatedAppUser() {

        AppUser user = AppUser
                .builder()
                .username("test")
                .password("test")
                .build();

        this.appUserRepository.save(user);

        AppUser testGetByUsername = this.appUserRepository.getByUsername("test");

        assertNotNull(testGetByUsername);
        assertEquals(user.getUsername(), testGetByUsername.getUsername());

    }
}