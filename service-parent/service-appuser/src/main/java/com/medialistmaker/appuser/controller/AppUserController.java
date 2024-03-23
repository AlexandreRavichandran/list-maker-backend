package com.medialistmaker.appuser.controller;

import com.medialistmaker.appuser.domain.AppUser;
import com.medialistmaker.appuser.dto.AppUserDTO;
import com.medialistmaker.appuser.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.appuser.service.appuser.AppUserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appusers")
public class AppUserController {

    private final AppUserServiceImpl appUserService;

    private final ModelMapper modelMapper;

    public AppUserController(
            AppUserServiceImpl appUserService,
            ModelMapper modelMapper
    ) {
        this.appUserService = appUserService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AppUserDTO> getByUsername(@PathVariable("username") String username) throws CustomNotFoundException {

        AppUser user = this.appUserService.getByUsername(username);

        return new ResponseEntity<>(this.modelMapper.map(user, AppUserDTO.class), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUserDTO> getById(@PathVariable("id") Long appUserId) throws CustomNotFoundException {

        AppUser user = this.appUserService.getById(appUserId);

        return new ResponseEntity<>(this.modelMapper.map(user, AppUserDTO.class), HttpStatus.OK);

    }
}
