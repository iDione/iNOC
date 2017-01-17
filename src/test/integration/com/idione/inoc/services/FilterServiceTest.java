package com.idione.inoc.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.idione.inoc.forms.FilterForm;
import com.idione.inoc.models.Client;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.test.AbstractIntegrationTest;

public class FilterServiceTest extends AbstractIntegrationTest {

    FilterService filterService;
    Client client1;
    Client client2;
    Filter filter1;
    Filter filter2;
    PocUser pocUser1;
    PocUser pocUser2;
    MailingGroup mailingGroup1;
    
    @Before
    public void createFilters() {
        Filter.deleteAll();
        client1 = Client.createIt("name", "The Avengers");
        client2 = Client.createIt("name", "Superheroes");
        pocUser1 = PocUser.createIt("client_id", client1.getInteger("id"), "first_name", "Tony", "last_name", "Stark", "phone_number", "1111111111");
        pocUser2 = PocUser.createIt("client_id", client1.getInteger("id"), "first_name", "Peter", "last_name", "Parker", "phone_number", "2222222222");
        mailingGroup1 = MailingGroup.createIt("client_id", client1.getInteger("id"), "name", "Iron Man List");
        filter1 = Filter.createIt("client_id", client1.getInteger("id"), "name", "Iron Man Filter", "retries", 2, "time_interval", 10, "mailing_group_id", mailingGroup1.getInteger("id"));
        filter2 = Filter.createIt("client_id", client2.getInteger("id"), "name", "Spider Man Filter", "retries", 2, "time_interval", 10, "mailing_group_id", mailingGroup1.getInteger("id"));
        filterService = new FilterService();
    }

    @Test
    public void getFiltersReturnsAllFiltersForClient() {
        List<Filter> filters = filterService.getFilters(client1.getInteger("id"));
        assertThat(filters.size(), is(equalTo(1)));
        assertThat(filters.get(0).getString("name"), is(equalTo("Iron Man Filter")));
    }
    
    @Test
    public void getFilterReturnsAFilterForm() {
        FilterForm filterForm = filterService.getFilter(filter1.getInteger("id"));
        assertThat(filter1.getString("name"), is(equalTo(filterForm.getName())));
        assertThat(filter1.getInteger("id"), is(equalTo(filterForm.getId())));
        assertThat(filter1.getInteger("retries"), is(equalTo(filterForm.getRetries())));
        assertThat(filter1.getInteger("time_interval"), is(equalTo(filterForm.getTimeInterval())));
        assertThat(filter1.getInteger("mailing_group_id"), is(equalTo(filterForm.getMailingGroupId())));
    }
    
    @Test
    public void saveCreatesANewFilter() {
        FilterForm filterForm = new FilterForm(client1.getClientId());
        filterForm.setName("Captain America Filter");
        filterForm.setMailingGroupId(mailingGroup1.getInteger("id"));
        filterForm.setRetries(5);
        filterForm.setTimeInterval(12);
        Filter filter = filterService.saveFilter(filterForm);
        
        FilterForm savedForm = filterService.getFilter(filter.getInteger("id"));
        
        assertThat(savedForm.getName(), is(equalTo(filterForm.getName())));
        assertThat(savedForm.getRetries(), is(equalTo(filterForm.getRetries())));
        assertThat(savedForm.getTimeInterval(), is(equalTo(filterForm.getTimeInterval())));
        assertThat(savedForm.getMailingGroupId(), is(equalTo(filterForm.getMailingGroupId())));
    }
    
    @Test
    public void saveUpdatesTheAFilter() {
        FilterForm filterForm = new FilterForm(filter1);
        filterForm.setName("Captain America Filter");
        filterForm.setRetries(5);
        filterForm.setTimeInterval(12);
        Filter filter = filterService.saveFilter(filterForm);
        filter1.refresh();
        assertThat(filter1.getString("name"), is(equalTo("Captain America Filter")));
        assertThat(filter1.getInteger("id"), is(equalTo(filter.getId())));
        assertThat(filter1.getInteger("retries"), is(equalTo(filterForm.getRetries())));
        assertThat(filter1.getInteger("time_interval"), is(equalTo(filterForm.getTimeInterval())));
    }
    
    @Test
    public void saveUpdatesTheFilterUsers() {
        FilterForm filterForm = new FilterForm(filter1);
        filterForm.getPocUserIds().add(pocUser1.getInteger("id"));
        Filter filter1 = filterService.saveFilter(filterForm);
        filter1.refresh();
        assertThat(filter1.getUsers().size(), is(equalTo(1)));
        assertThat(filter1.getUsers().get(0).getInteger("id"), is(equalTo(pocUser1.getInteger("id"))));
        
        filterForm = new FilterForm(filter1);
        filterForm.getPocUserIds().clear();
        filterForm.getPocUserIds().add(pocUser2.getInteger("id"));
        filter1 = filterService.saveFilter(filterForm);
        filter1.refresh();
        assertThat(filter1.getUsers().size(), is(equalTo(1)));
        assertThat(filter1.getUsers().get(0).getInteger("id"), is(equalTo(pocUser2.getInteger("id"))));
    }
    
    @Test
    public void saveUpdatesTheFilterKeywords() {
        FilterForm filterForm = new FilterForm(filter1);
        filterForm.getKeywords().add("help");
        Filter filter1 = filterService.saveFilter(filterForm);
        filter1.refresh();
        assertThat(filter1.getKeywords().size(), is(equalTo(1)));
        assertThat(filter1.getKeywords().get(0).getString("keyword"), is(equalTo("help")));
        
        filterForm = new FilterForm(filter1);
        filterForm.getKeywords().clear();
        filterForm.getKeywords().add("Tessaract");
        filter1 = filterService.saveFilter(filterForm);
        filter1.refresh();
        assertThat(filter1.getKeywords().size(), is(equalTo(1)));
        assertThat(filter1.getKeywords().get(0).getString("keyword"), is(equalTo("Tessaract")));
    }
}
