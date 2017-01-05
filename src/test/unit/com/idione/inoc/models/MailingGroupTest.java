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
public class MailingGroupTest extends AbstractIntegrationTest {

    Client client;
    MailingGroup mailingGroup;
    PocUser pocUser1;
    
    @Before
    public void createMailingGroup() {
        client = Client.createIt("name", "Mickey Mouse Club House");
        mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"), "name", "A Mailing Group");
        pocUser1 = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Mickey", "last_name", "Mouse", "phone_number", "1111111111");
        MailingGroupPocUser.createIt("mailing_group_id", mailingGroup.getInteger("id"), "poc_user_id", pocUser1.getInteger("id"));
    }

    @Test
    public void getAssignableUsersReturnsAllUserForAMailingGroup() {
        List<PocUser> mailingGroupPocUsers = mailingGroup.getUsers();
        
        assertThat(mailingGroupPocUsers.size(), is(equalTo(1)));
        
        PocUser pocUser2 = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Minnie", "last_name", "Mouse", "phone_number", "2222222222");
        MailingGroupPocUser.createIt("mailing_group_id", mailingGroup.getInteger("id"), "poc_user_id", pocUser2.getInteger("id"));
        mailingGroupPocUsers = mailingGroup.getUsers();
        assertThat(mailingGroupPocUsers.size(), is(equalTo(2)));
    }
    
    @Test
    public void getAssignableUsersReturnsAllUserForOnlyThatMailingGroup() {
        MailingGroup mailingGroup2 = MailingGroup.createIt("name", "Another MailingGroup", "client_id", client.getInteger("id"));
        PocUser pocUser2 = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Minnie", "last_name", "Mouse", "phone_number", "2222222222");
        MailingGroupPocUser.createIt("mailing_group_id", mailingGroup2.getInteger("id"), "poc_user_id", pocUser2.getInteger("id"));
        List<PocUser> mailingGroupPocUsers = mailingGroup.getUsers();
        assertThat(mailingGroupPocUsers.size(), is(equalTo(1)));
    }
}
