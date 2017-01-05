package com.idione.inoc.models;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class FilterTest extends AbstractIntegrationTest {

    Client client;
    Filter filter;
    MailingGroup mailingGroup;
    PocUser pocUser1;
    
    @Before
    public void createFilter() {
        client = Client.createIt("name", "Mickey Mouse Club House");
        mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"));
        filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", 3, "mailing_group_id", mailingGroup.getInteger("id"));
        pocUser1 = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Mickey", "last_name", "Mouse", "phone_number", "1111111111");
        FilterPocUser.createIt("filter_id", filter.getInteger("id"), "poc_user_id", pocUser1.getInteger("id"));
    }

    @Test
    public void getAssignableUsersReturnsAllUserForAFilter() {
        List<PocUser> filterPocUsers = filter.getAssignableUsers();
        
        assertThat(filterPocUsers.size(), is(equalTo(1)));
        
        PocUser pocUser2 = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Minnie", "last_name", "Mouse", "phone_number", "2222222222");
        FilterPocUser.createIt("filter_id", filter.getInteger("id"), "poc_user_id", pocUser2.getInteger("id"));
        filterPocUsers = filter.getAssignableUsers();
        assertThat(filterPocUsers.size(), is(equalTo(2)));
    }
    
    @Test
    public void getAssignableUsersReturnsAllUserForOnlyThatFilter() {
        Filter filter2 = Filter.createIt("name", "Another Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", 3, "mailing_group_id", mailingGroup.getInteger("id"));
        PocUser pocUser2 = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Minnie", "last_name", "Mouse", "phone_number", "2222222222");
        FilterPocUser.createIt("filter_id", filter2.getInteger("id"), "poc_user_id", pocUser2.getInteger("id"));
        List<PocUser> filterPocUsers = filter.getAssignableUsers();
        assertThat(filterPocUsers.size(), is(equalTo(1)));
    }
}
