package com.idione.inoc.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.Model;

import com.idione.inoc.forms.PocUserForm;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.services.PocUserService;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class PocUsersControllerTest extends AbstractIntegrationTest {

    PocUsersController controller;
    final List<PocUser> pocUsers = new ArrayList<PocUser>();
    PocUser currentPocUser;
    int pocUserId = 1;
    
    @Before
    public void setup() {
        currentPocUser = new PocUser();
        currentPocUser.set("id", pocUserId);
        currentPocUser.set("client_id", 1);
        
        controller = new PocUsersController();
        controller.setCurrentUser(currentPocUser);
    }

    @Test
    public void getPocUsersCallsThePocUserServiceForPocUsers(@Mocked Model model, @Mocked PocUserService pocUserService) {
        controller.setPocUserService(pocUserService);
        
        new Expectations() {
            {
                pocUserService.getPocUsers(currentPocUser.getClientId());
                result = pocUsers;
            }
        };
        
        controller.getPocUsers(model);
        
        new Verifications() {
            {
                pocUserService.getPocUsers(currentPocUser.getClientId());
                times = 1;
                model.addAttribute("pocUsers", pocUsers);
                times = 1;
            }
        };
    }
    
    @Test
    public void editPocUserCallsThePocUserServiceForPocUser(@Mocked Model model, @Mocked PocUserService pocUserService) {
        controller.setPocUserService(pocUserService);
        PocUserForm pocUserForm = new PocUserForm();
        
        new Expectations() {
            {
                pocUserService.getPocUser(pocUserId);
                result = pocUserForm;
            }
        };
        
        controller.editPocUser(pocUserId, model);
        
        new Verifications() {
            {
                pocUserService.getPocUser(pocUserId);
                times = 1;
                model.addAttribute("pocUserForm", pocUserForm);
                times = 1;
            }
        };
    }
    
    @Test
    public void savePocUserCallsThePocUserServiceForSave(@Mocked Model model, @Mocked PocUserService pocUserService) {
        controller.setPocUserService(pocUserService);
        PocUserForm pocUserForm = new PocUserForm();
        
        controller.savePocUser(pocUserForm, model);
        
        new Verifications() {
            {
                pocUserService.savePocUser(pocUserForm);
                times = 1;
            }
        };
    }
}