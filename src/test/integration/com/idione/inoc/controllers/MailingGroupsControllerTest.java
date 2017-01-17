package com.idione.inoc.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.Model;

import com.idione.inoc.forms.MailingGroupForm;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.services.MailingGroupService;
import com.idione.inoc.services.PocUserService;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class MailingGroupsControllerTest extends AbstractIntegrationTest {

    MailingGroupsController controller;
    final List<MailingGroup> mailingGroups = new ArrayList<MailingGroup>();
    PocUser currentPosUser;
    int pocUserId = 1;
    int mailingGroupId = 101;
    
    @Before
    public void setup() {
        currentPosUser = new PocUser();
        currentPosUser.set("id", pocUserId);
        currentPosUser.set("client_id", 1);
        
        controller = new MailingGroupsController();
        controller.setCurrentUser(currentPosUser);
    }

    @Test
    public void getMailingGroupsCallsTheMailingGroupServiceForMailingGroups(@Mocked Model model, @Mocked MailingGroupService mailingGroupService) {
        controller.setMailingGroupService(mailingGroupService);
        
        new Expectations() {
            {
                mailingGroupService.getMailingGroups(currentPosUser.getClientId());
                result = mailingGroups;
            }
        };
        
        controller.getMailingGroups(model);
        
        new Verifications() {
            {
                mailingGroupService.getMailingGroups(currentPosUser.getClientId());
                times = 1;
                model.addAttribute("mailingGroups", mailingGroups);
                times = 1;
            }
        };
    }
    
    @Test
    public void editMailingGroupCallsTheMailingGroupServiceForMailingGroup(@Mocked Model model, @Mocked MailingGroupService mailingGroupService, @Mocked PocUserService pocUserService) {
        controller.setMailingGroupService(mailingGroupService);
        controller.setPocUserService(pocUserService);
        MailingGroupForm mailingGroupForm = new MailingGroupForm();
        
        new Expectations() {
            {
                mailingGroupService.getMailingGroup(mailingGroupId);
                result = mailingGroupForm;
                pocUserService.getPocUsers(currentPosUser.getClientId());
                result = new ArrayList<PocUser>();
            }
        };
        
        controller.editMailingGroup(mailingGroupId, model);
        
        new Verifications() {
            {
                mailingGroupService.getMailingGroup(mailingGroupId);
                times = 1;
                model.addAttribute("mailingGroupForm", mailingGroupForm);
                times = 1;
            }
        };
    }
    
    @Test
    public void saveMailingGroupCallsTheMailingGroupServiceForSave(@Mocked Model model, @Mocked MailingGroupService mailingGroupService) {
        controller.setMailingGroupService(mailingGroupService);
        MailingGroupForm mailingGroupForm = new MailingGroupForm();
        
        controller.saveMailingGroup(mailingGroupForm, model);
        
        new Verifications() {
            {
                mailingGroupService.saveMailingGroup(mailingGroupForm);
                times = 1;
            }
        };
    }
}
