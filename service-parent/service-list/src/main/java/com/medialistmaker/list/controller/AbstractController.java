package com.medialistmaker.list.controller;

import org.springframework.security.core.context.SecurityContextHolder;

public abstract class AbstractController {

    protected Long getCurrentLoggedAppUserId() {

        return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());

    }

}
