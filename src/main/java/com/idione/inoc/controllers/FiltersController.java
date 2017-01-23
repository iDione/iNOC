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

import com.idione.inoc.forms.FilterForm;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.services.FilterService;
import com.idione.inoc.services.MailingGroupService;
import com.idione.inoc.services.PocUserService;

@RequestMapping(value = "/filters")
@Controller
public class FiltersController extends ApplicationController {

    private FilterService filterService;
    private PocUserService pocUserService;
    private MailingGroupService mailingGroupService;

    @Autowired
    public void setPocUserService(PocUserService pocUserService) {
        this.pocUserService = pocUserService;
    }

    @Autowired
    public void setMailingGroupService(MailingGroupService mailingGroupService) {
        this.mailingGroupService = mailingGroupService;
    }

    @Autowired
    public void setFilterService(FilterService filterService) {
        this.filterService = filterService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getFilters(Model model) {
        List<Filter> filters = filterService.getFilters(currentClientId());
        model.addAttribute("filters", filters);
        return "filters/index";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String newFilter(Model model) {
        model.addAttribute("filterForm", new FilterForm(currentClientId()));
        setupForm(model);
        return "filters/new";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String editFilter(@PathVariable int id, Model model) {
        FilterForm filterForm = filterService.getFilter(id);
        model.addAttribute("filterForm", filterForm);
        setupForm(model);
        return "filters/edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String saveFilter(@ModelAttribute FilterForm filterForm, Model model) {
        filterService.saveFilter(filterForm);
        List<Filter> filters = filterService.getFilters(currentClientId());
        model.addAttribute("filters", filters);
        return "filters/index";
    }

    private void setupForm(Model model) {
        List<PocUser> pocUsers = pocUserService.getPocUsers(currentClientId());
        model.addAttribute("allPocUsers", pocUsers);
        List<MailingGroup> mailingGroups = mailingGroupService.getMailingGroups(currentClientId());
        model.addAttribute("allMailingGroups", mailingGroups);
    }
}
