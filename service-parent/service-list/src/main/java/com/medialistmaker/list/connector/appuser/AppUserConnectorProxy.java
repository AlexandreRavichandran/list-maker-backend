package com.medialistmaker.list.connector.appuser;

import com.medialistmaker.list.dto.AppUserDTO;
import org.springframework.stereotype.Component;

@Component
public class AppUserConnectorProxy {


    private final AppUserConnector appUserConnector;

    public AppUserConnectorProxy(
            AppUserConnector appUserConnector
    ) {
        this.appUserConnector = appUserConnector;
    }

    public AppUserDTO getById(Long appUserId) {
        return this.appUserConnector.getUserByUsername(appUserId);
    }

}
