package com.idione.inoc.controllers;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import com.idione.inoc.models.PocUser;

@Controller
public class ApplicationController {

    public PocUser getCurrentUser() {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PocUser pocUser = PocUser.findFirst("email_address = ?", user.getUsername());
        return pocUser;
    }

    public int currentClientId() {
        return getCurrentUser().getClientId();
    }

    public int currentPocUserId() {
        return getCurrentUser().getInteger("id");
    }

    public boolean isSuperAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER"));
    }
}
