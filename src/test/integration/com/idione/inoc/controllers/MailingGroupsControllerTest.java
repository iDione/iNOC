package com.idione.inoc.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.idione.inoc.forms.MailingGroupForm;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.services.MailingGroupService;
import com.idione.inoc.services.PocUserService;
import com.idione.inoc.test.AbstractIntegrationTest;
import com.idione.inoc.validators.MailingGroupValidator;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class MailingGroupsControllerTest extends AbstractIntegrationTest {

    MailingGroupsController controller;
    final List<MailingGroup> mailingGroups = new ArrayList<MailingGroup>();
    PocUser currentPosUser;
    int mailingGroupId = 101;

    @Before
    public void setup() {
        controller = new MailingGroupsController();
    }

    @Test
    public void getMailingGroupsCallsTheMailingGroupServiceForMailingGroups(@Mocked Model model, @Mocked MailingGroupService mailingGroupService) {
        controller.setMailingGroupService(mailingGroupService);

        new Expectations() {
            {
                mailingGroupService.getMailingGroups(controller.currentClientId());
                result = mailingGroups;
            }
        };

        controller.getMailingGroups(model);

        new Verifications() {
            {
                mailingGroupService.getMailingGroups(controller.currentClientId());
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
                pocUserService.getPocUsers(controller.currentClientId());
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
    public void saveMailingGroupCallsTheMailingGroupServiceForSave(@Mocked Model model, @Mocked MailingGroupService mailingGroupService, @Mocked BindingResult bindingResult, @Mocked MailingGroupValidator mailingGroupValidator) {
        controller.setMailingGroupService(mailingGroupService);
        MailingGroupForm mailingGroupForm = new MailingGroupForm();

        controller.saveMailingGroup(mailingGroupForm, bindingResult, model);

        new Verifications() {
            {
                MailingGroupValidator.validate(mailingGroupForm, bindingResult);
                times = 1;
                mailingGroupService.saveMailingGroup(mailingGroupForm);
                times = 1;
            }
        };
    }
}
