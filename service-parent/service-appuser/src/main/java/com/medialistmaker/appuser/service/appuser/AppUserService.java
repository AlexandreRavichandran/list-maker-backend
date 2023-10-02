package com.medialistmaker.appuser.service.appuser;

import com.medialistmaker.appuser.domain.AppUser;
import com.medialistmaker.appuser.exception.badrequestexception.CustomBadRequestException;
import com.medialistmaker.appuser.exception.entityduplicationexception.CustomEntityDuplicationException;
import com.medialistmaker.appuser.exception.notfoundexception.CustomNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {

    AppUser getByUsername(String username) throws CustomNotFoundException;

    AppUser add(AppUser appUser) throws CustomBadRequestException, CustomEntityDuplicationException;

}
