package com.idione.inoc.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.idione.inoc.forms.PocUserForm;
import com.idione.inoc.models.Client;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.models.PocUserRole;
import com.idione.inoc.test.AbstractIntegrationTest;

public class PocUserServiceTest extends AbstractIntegrationTest {

    PocUserService pocUserService;
    Client client1;
    Client client2;
    PocUser pocUser1;
    PocUser pocUser2;

    @Before
    public void createPocUsers() {
        PocUser.deleteAll();
        client1 = Client.createIt("name", "The Avengers");
        client2 = Client.createIt("name", "Superheroes");
        pocUser1 = PocUser.createIt("client_id", client1.getInteger("id"), "first_name", "Tony", "last_name", "Stark", "email_address", "ironman@marvell.tst", "phone_number", "1231231232");
        pocUser2 = PocUser.createIt("client_id", client2.getInteger("id"), "first_name", "Peter", "last_name", "Parker", "email_address", "spiderman@marvell.tst", "phone_number", "3453453454");
        pocUserService = new PocUserService();
    }

    @Test
    public void getPocUsersReturnsAllPocUsersForClient() {
        List<PocUser> pocUsers = pocUserService.getPocUsers(client1.getInteger("id"));
        assertThat(pocUsers.size(), is(equalTo(1)));
        assertThat(pocUsers.get(0).getString("email_address"), is(equalTo("ironman@marvell.tst")));
    }

    @Test
    public void getPocUserReturnsAPocUserForm() {
        PocUserForm pocUserForm = pocUserService.getPocUser(pocUser1.getInteger("id"));
        assertThat(pocUser1.getString("first_name"), is(equalTo(pocUserForm.getFirstName())));
        assertThat(pocUser1.getString("last_name"), is(equalTo(pocUserForm.getLastName())));
        assertThat(pocUser1.getString("email_address"), is(equalTo(pocUserForm.getEmailAddress())));
        assertThat(pocUser1.getInteger("id"), is(equalTo(pocUserForm.getId())));
    }

    @Test
    public void saveCreatesANewPocUser() {
        PocUserForm pocUserForm = new PocUserForm(client1.getClientId());
        pocUserForm.setFirstName("Steven");
        pocUserForm.setLastName("Rogers");
        pocUserForm.setEmailAddress("captainamerica@marvel.tst");
        PocUser pocUser = pocUserService.savePocUser(pocUserForm);

        PocUserForm savedForm = pocUserService.getPocUser(pocUser.getInteger("id"));

        assertThat(savedForm.getFirstName(), is(equalTo(pocUserForm.getFirstName())));
        assertThat(savedForm.getLastName(), is(equalTo(pocUserForm.getLastName())));
        assertThat(savedForm.getEmailAddress(), is(equalTo(pocUserForm.getEmailAddress())));
        assertThat(savedForm.getPhoneNumber(), is(equalTo(pocUserForm.getPhoneNumber())));
        assertThat(savedForm.getClientId(), is(equalTo(pocUserForm.getClientId())));
    }

    @Test
    public void saveEncodesAndSavesPasswordIfPresent() {
        PocUserForm pocUserForm = new PocUserForm(client1.getClientId());
        pocUserForm.setFirstName("Steven");
        pocUserForm.setLastName("Rogers");
        pocUserForm.setEmailAddress("captainamerica@marvel.tst");
        pocUserForm.setPassword("12qwaszx");
        PocUser pocUser = pocUserService.savePocUser(pocUserForm);

        PocUserForm savedForm = pocUserService.getPocUser(pocUser.getInteger("id"));

        assertThat(savedForm.getPassword().length(), is(greaterThan(0)));
        assertThat(savedForm.getPassword().length(), is(not(equalTo("12qwaszx"))));

        assertThat((new BCryptPasswordEncoder()).matches("12qwaszx", savedForm.getPassword()),is(equalTo(true)));
    }

    @Test
    public void saveWithPasswordCreatesAAdminUserRole() {
        PocUserForm pocUserForm = new PocUserForm(client1.getClientId());
        pocUserForm.setFirstName("Steven");
        pocUserForm.setLastName("Rogers");
        pocUserForm.setEmailAddress("captainamerica@marvel.tst");
        pocUserForm.setPassword("12qwaszx");
        PocUser pocUser = pocUserService.savePocUser(pocUserForm);

        PocUserForm savedForm = pocUserService.getPocUser(pocUser.getInteger("id"));
        PocUserRole pocUserRole = PocUserRole.findFirst("poc_user_id = ?", pocUser.getInteger("id"));

        assertThat(pocUserRole.getString("role"), is(equalTo("ADMIN")));
    }

    @Test
    public void saveUpdatesAPocUser() {
        PocUserForm pocUserForm = new PocUserForm(pocUser1);
        pocUserForm.setEmailAddress("renegadeAvenger@marvel.tst");
        PocUser pocUser = pocUserService.savePocUser(pocUserForm);
        pocUser1.refresh();
        assertThat(pocUser1.getString("first_name"), is(equalTo("Tony")));
        assertThat(pocUser1.getString("email_address"), is(equalTo("renegadeAvenger@marvel.tst")));
        assertThat(pocUser1.getInteger("id"), is(equalTo(pocUser.getId())));
    }
}
