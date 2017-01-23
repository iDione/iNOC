package com.idione.inoc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.idione.inoc.forms.MailingGroupForm;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.services.MailingGroupService;
import com.idione.inoc.services.PocUserService;

@RequestMapping(value = "/mailingGroups")
@Controller
public class MailingGroupsController extends ApplicationController {

    private MailingGroupService mailingGroupService;
    private PocUserService pocUserService;

    @Autowired
    public void setPocUserService(PocUserService pocUserService) {
        this.pocUserService = pocUserService;
    }

    @Autowired
    public void setMailingGroupService(MailingGroupService mailingGroupService) {
        this.mailingGroupService = mailingGroupService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getMailingGroups(Model model) {
        List<MailingGroup> mailingGroups = mailingGroupService.getMailingGroups(currentClientId());
        model.addAttribute("mailingGroups", mailingGroups);
        return "mailingGroups/index";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String newMailingGroup(Model model) {
        model.addAttribute("mailingGroupForm", new MailingGroupForm(currentClientId()));
        setupForm(model);
        return "mailingGroups/new";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String editMailingGroup(@PathVariable int id, Model model) {
        MailingGroupForm mailingGroupForm = mailingGroupService.getMailingGroup(id);
        model.addAttribute("mailingGroupForm", mailingGroupForm);
        setupForm(model);
        return "mailingGroups/edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String saveMailingGroup(@ModelAttribute MailingGroupForm mailingGroupForm, Model model) {
        mailingGroupService.saveMailingGroup(mailingGroupForm);
        List<MailingGroup> mailingGroups = mailingGroupService.getMailingGroups(currentClientId());
        model.addAttribute("mailingGroups", mailingGroups);
        return "mailingGroups/index";
    }

    private void setupForm(Model model) {
        List<PocUser> pocUsers = pocUserService.getPocUsers(currentClientId());
        model.addAttribute("allPocUsers", pocUsers);
    }
}
