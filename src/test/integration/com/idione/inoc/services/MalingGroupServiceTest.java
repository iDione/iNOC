package com.idione.inoc.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.idione.inoc.forms.MailingGroupForm;
import com.idione.inoc.models.Client;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.test.AbstractIntegrationTest;

public class MalingGroupServiceTest extends AbstractIntegrationTest {

    MailingGroupService mailingGroupService;
    Client client1;
    Client client2;
    MailingGroup mailingGroup1;
    MailingGroup mailingGroup2;
    PocUser pocUser1;
    PocUser pocUser2;

    @Before
    public void createMailingGroups() {
        MailingGroup.deleteAll();
        client1 = Client.createIt("name", "The Avengers");
        client2 = Client.createIt("name", "Superheroes");
        pocUser1 = PocUser.createIt("client_id", client1.getInteger("id"), "first_name", "Tony", "last_name", "Stark", "phone_number", "1111111111");
        pocUser2 = PocUser.createIt("client_id", client1.getInteger("id"), "first_name", "Peter", "last_name", "Parker", "phone_number", "2222222222");
        mailingGroup1 = MailingGroup.createIt("client_id", client1.getInteger("id"), "name", "Iron Man List");
        mailingGroup2 = MailingGroup.createIt("client_id", client2.getInteger("id"), "name", "Spider Man List");
        mailingGroupService = new MailingGroupService();
    }

    @Test
    public void getMailingGroupsReturnsAllMailingGroupsForClient() {
        List<MailingGroup> mailingGroups = mailingGroupService.getMailingGroups(client1.getInteger("id"));
        assertThat(mailingGroups.size(), is(equalTo(1)));
        assertThat(mailingGroups.get(0).getString("name"), is(equalTo("Iron Man List")));
    }

    @Test
    public void getMailingGroupReturnsAMailingGroupForm() {
        MailingGroupForm mailingGroupForm = mailingGroupService.getMailingGroup(mailingGroup1.getInteger("id"));
        assertThat(mailingGroup1.getString("name"), is(equalTo(mailingGroupForm.getName())));
        assertThat(mailingGroup1.getInteger("id"), is(equalTo(mailingGroupForm.getId())));
    }

    @Test
    public void saveCreatesANewMailingGroup() {
        MailingGroupForm mailingGroupForm = new MailingGroupForm(client1.getClientId());
        mailingGroupForm.setName("DC Group");
        MailingGroup mailingGroup = mailingGroupService.saveMailingGroup(mailingGroupForm);

        MailingGroupForm savedForm = mailingGroupService.getMailingGroup(mailingGroup.getInteger("id"));

        assertThat(savedForm.getName(), is(equalTo(mailingGroupForm.getName())));
        assertThat(savedForm.getName(), is(equalTo(mailingGroupForm.getName())));
    }

    @Test
    public void saveUpdatesTheAMailingGroup() {
        MailingGroupForm mailingGroupForm = new MailingGroupForm(mailingGroup1);
        mailingGroupForm.setName("Captain America Group");
        MailingGroup mailingGroup = mailingGroupService.saveMailingGroup(mailingGroupForm);
        mailingGroup1.refresh();
        assertThat(mailingGroup1.getString("name"), is(equalTo("Captain America Group")));
        assertThat(mailingGroup1.getInteger("id"), is(equalTo(mailingGroup.getId())));
    }

    @Test
    public void saveUpdatesTheMailingGroupUsers() {
        MailingGroupForm mailingGroupForm = new MailingGroupForm(mailingGroup1);
        mailingGroupForm.getPocUserIds().add(pocUser1.getInteger("id"));
        MailingGroup mailingGroup1 = mailingGroupService.saveMailingGroup(mailingGroupForm);
        mailingGroup1.refresh();
        assertThat(mailingGroup1.getUsers().size(), is(equalTo(1)));
        assertThat(mailingGroup1.getUsers().get(0).getInteger("id"), is(equalTo(pocUser1.getInteger("id"))));

        mailingGroupForm = new MailingGroupForm(mailingGroup1);
        mailingGroupForm.getPocUserIds().clear();
        mailingGroupForm.getPocUserIds().add(pocUser2.getInteger("id"));
        mailingGroup1 = mailingGroupService.saveMailingGroup(mailingGroupForm);
        mailingGroup1.refresh();
        assertThat(mailingGroup1.getUsers().size(), is(equalTo(1)));
        assertThat(mailingGroup1.getUsers().get(0).getInteger("id"), is(equalTo(pocUser2.getInteger("id"))));
    }
}
