package com.medialistmaker.list.connector.appuser;

import com.medialistmaker.list.dto.AppUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppUserConnectorProxy {

    @Autowired
    private AppUserConnector appUserConnector;

    public AppUserDTO getById(Long appUserId) {
        return this.appUserConnector.getUserByUsername(appUserId);
    }

}
