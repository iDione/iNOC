package com.idione.inoc.controllers;

import org.springframework.stereotype.Controller;

import com.idione.inoc.models.PocUser;

@Controller
public class ApplicationController {

    private PocUser currentUser;
    
    public PocUser getCurrentUser() {
        PocUser pocUser = new PocUser();
        pocUser.set("id", 1);
        pocUser.set("client_id", 1);
        return pocUser;
    }
    
    public int currentClientId() {
        return getCurrentUser().getClientId();
    }

    public void setCurrentUser(PocUser currentUser) {
        this.currentUser = currentUser;
    }
}
