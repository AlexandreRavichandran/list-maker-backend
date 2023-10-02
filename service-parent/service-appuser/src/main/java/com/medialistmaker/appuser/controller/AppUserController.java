package com.medialistmaker.appuser.controller;

import com.medialistmaker.appuser.service.appuser.AppUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appusers")
public class AppUserController {

    @Autowired
    AppUserServiceImpl appUserService;

    @GetMapping("/username/{username}")
    public ResponseEntity<UserDetails> getByUsername(@PathVariable("username") String username) {

        try {
            return new ResponseEntity<>(
                    this.appUserService.loadUserByUsername(username),
                    HttpStatus.OK
            );
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
