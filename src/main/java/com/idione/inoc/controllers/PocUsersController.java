package com.idione.inoc.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.idione.inoc.enums.Role;
import com.idione.inoc.forms.PocUserForm;
import com.idione.inoc.models.Client;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.services.PocUserService;

@RequestMapping(value = "/pocUsers")
@Controller
public class PocUsersController extends ApplicationController {

    private PocUserService pocUserService;

    @Autowired
    public void setPocUserService(PocUserService pocUserService) {
        this.pocUserService = pocUserService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getPocUsers(Model model) {
        List<PocUser> pocUsers = pocUserService.getPocUsers(isSuperAdmin() ? 0 : currentClientId());
        model.addAttribute("pocUsers", pocUsers);
        return "pocUsers/index";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String newPocUser(Model model) {
        model.addAttribute("pocUserForm", new PocUserForm(currentClientId()));
        model.addAttribute("allRoles", Role.values());
        model.addAttribute("allClients", Client.findAll());
        return "pocUsers/new";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String editPocUser(@PathVariable int id, Model model) {
        PocUserForm pocUserForm = pocUserService.getPocUser(id);
        model.addAttribute("pocUserForm", pocUserForm);
        model.addAttribute("allRoles", Role.values());
        model.addAttribute("allClients", Client.findAll());
        return "pocUsers/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String editMyself(Model model) {
        PocUserForm pocUserForm = pocUserService.getPocUser(currentPocUserId());
        model.addAttribute("pocUserForm", pocUserForm);
        model.addAttribute("allRoles", Role.values());
        model.addAttribute("allClients", Client.findAll());
        return "pocUsers/edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String savePocUser(@Valid @ModelAttribute PocUserForm pocUserForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", Role.values());
            model.addAttribute("allClients", Client.findAll());
            if(pocUserForm.getId() > 0){
                return "pocUsers/edit";
            } else {
                return "pocUsers/new";
            }
        }
        pocUserService.savePocUser(pocUserForm);
        return "dashboard";
    }
}
