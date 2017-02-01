package com.idione.inoc.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

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
    
//    private String getPrincipal(){
//        String userName = null;
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        if (principal instanceof UserDetails) {
//            userName = ((UserDetails)principal).getUsername();
//        } else {
//            userName = principal.toString();
//        }
//        return userName;
//    }
//    
//    
//    
//    @ModelAttribute("roles")
//    public List<UserProfile> initializeProfiles() {
//        return userProfileService.findAll();
//    }
}
