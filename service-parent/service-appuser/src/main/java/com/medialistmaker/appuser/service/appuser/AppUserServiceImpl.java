package com.medialistmaker.appuser.service.appuser;

import com.medialistmaker.appuser.domain.AppUser;
import com.medialistmaker.appuser.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.appuser.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.appuser.exception.notfoundexception.CustomNotFoundException;
import com.medialistmaker.appuser.repository.AppUserRepository;
import com.medialistmaker.appuser.utils.CustomEntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.util.List;

@Service
public class AppUserServiceImpl implements AppUserService {

    @Autowired
    AppUserRepository appUserRepository;

    @Autowired
    CustomEntityValidator<AppUser> appUserEntityValidator;

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

        if(nonNull(isUsernameAlreadyUsed)) {
            throw new CustomEntityDuplicationException("Already used");
        }

        return this.appUserRepository.save(appUser);

    }

}
