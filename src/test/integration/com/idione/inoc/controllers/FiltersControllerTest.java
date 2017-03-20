package com.idione.inoc.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.idione.inoc.forms.FilterForm;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.services.FilterService;
import com.idione.inoc.services.MailingGroupService;
import com.idione.inoc.services.PocUserService;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class FiltersControllerTest extends AbstractIntegrationTest {

    FiltersController controller;
    final List<Filter> filters = new ArrayList<Filter>();
    static int filterId = 101;

    @Before
    public void setup() {
        controller = new FiltersController();
    }

    @Test
    public void getFiltersCallsTheFilterServiceForFilters(@Mocked Model model, @Mocked FilterService filterService) {
        controller.setFilterService(filterService);

        new Expectations() {
            {
                filterService.getFilters(controller.currentClientId());
                result = filters;
            }
        };

        controller.getFilters(model);

        new Verifications() {
            {
                filterService.getFilters(controller.currentClientId());
                times = 1;
                model.addAttribute("filters", filters);
                times = 1;
            }
        };
    }

    @Test
    public void editFilterCallsTheFilterServiceForFilter(@Mocked Model model, @Mocked FilterService filterService, @Mocked PocUserService pocUserService, @Mocked MailingGroupService mailingGroupService) {
        controller.setFilterService(filterService);
        controller.setPocUserService(pocUserService);
        controller.setMailingGroupService(mailingGroupService);
        FilterForm filterForm = new FilterForm();

        new Expectations() {
            {
                filterService.getFilter(filterId);
                result = filterForm;
                pocUserService.getPocUsers(clientId);
                result = new ArrayList<PocUser>();
                mailingGroupService.getMailingGroups(clientId);
                result = new ArrayList<MailingGroup>();
            }
        };

        controller.editFilter(filterId, model);

        new Verifications() {
            {
                filterService.getFilter(filterId);
                times = 1;
                model.addAttribute("filterForm", filterForm);
                times = 1;
            }
        };
    }

    @Test
    public void saveFilterCallsTheFilterServiceForSave(@Mocked Model model, @Mocked FilterService filterService, @Mocked BindingResult bindingResult) {
        controller.setFilterService(filterService);
        FilterForm filterForm = new FilterForm();

        controller.saveFilter(filterForm, bindingResult, model);

        new Verifications() {
            {
                filterService.saveFilter(filterForm);
                times = 1;
            }
        };
    }
}
