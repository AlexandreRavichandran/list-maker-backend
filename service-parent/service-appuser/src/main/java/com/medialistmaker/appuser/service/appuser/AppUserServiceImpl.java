package com.medialistmaker.appuser.service.appuser;

import com.medialistmaker.appuser.domain.AppUser;
import com.medialistmaker.appuser.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.appuser.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.appuser.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.appuser.repository.AppUserRepository;
import com.medialistmaker.appuser.utils.CustomEntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    CustomEntityValidator<AppUser> appUserEntityValidator;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public AppUser getByUsername(String username) throws CustomNotFoundException {

        AppUser appUser = this.appUserRepository.getByUsername(username);

        if(isNull(appUser)) {
            throw new CustomNotFoundException("Not found");
        }

        return appUser;
    }

    @Override
    public AppUser add(AppUser appUser) throws CustomBadRequestException, CustomEntityDuplicationException {

        List<String> appUserErrorList = this.appUserEntityValidator.validateEntity(appUser);

        if(Boolean.FALSE.equals(appUserErrorList.isEmpty())) {
            throw new CustomBadRequestException("Bad request", appUserErrorList);
        }

        AppUser isUsernameAlreadyUsed = this.appUserRepository.getByUsername(appUser.getUsername());

        if (nonNull(isUsernameAlreadyUsed)) {
            throw new CustomEntityDuplicationException("Already used");
        }

        appUser.setPassword(this.passwordEncoder.encode(appUser.getPassword()));
        return this.appUserRepository.save(appUser);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser appUser = this.appUserRepository.getByUsername(username);

        if (isNull(appUser)) {
            throw new UsernameNotFoundException("Bad credentials");
        }

        return new User(appUser.getUsername().toLowerCase(), appUser.getPassword(), new ArrayList<>());

    }
}
