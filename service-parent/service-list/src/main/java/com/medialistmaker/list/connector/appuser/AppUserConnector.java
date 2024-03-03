package com.medialistmaker.list.connector.appuser;

import com.medialistmaker.list.dto.AppUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "appuser", path = "/api/appusers")
public interface AppUserConnector {

    @GetMapping("/{id}")
    AppUserDTO getUserByUsername(@PathVariable("id") Long appUserId);

}
