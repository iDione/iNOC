package com.idione.inoc.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.idione.inoc.forms.ClientForm;
import com.idione.inoc.models.Client;
import com.idione.inoc.test.AbstractIntegrationTest;

public class ClientServiceTest extends AbstractIntegrationTest {

    ClientService clientService;

    Client client1;
    Client client2;

    @Before
    public void createClients() {
        Client.deleteAll();
        client1 = Client.createIt("name", "Mickey Mouse Club House", "email", "mickey@clubhouse.tst");
        client2 = Client.createIt("name", "Tinker Bell Pixie Hollow");
        clientService = new ClientService();
    }

    @Test
    public void getClientsReturnsAllClients() {
        List<Client> clients = clientService.getClients("");
        assertThat(clients.size(), is(equalTo(2)));
    }

    @Test
    public void getClientsReturnsClientsByName() {
        List<Client> clients = clientService.getClients("Club");
        assertThat(clients.size(), is(equalTo(1)));
        assertThat(clients.get(0).getString("name"), is(equalTo("Mickey Mouse Club House")));
    }

    @Test
    public void getClientReturnsAClientForm() {
        ClientForm clientForm = clientService.getClient(client1.getInteger("id"));
        assertThat(client1.getString("name"), is(equalTo(clientForm.getName())));
        assertThat(client1.getString("email"), is(equalTo(clientForm.getEmail())));
        assertThat(client1.getInteger("id"), is(equalTo(clientForm.getId())));
    }

    @Test
    public void saveCreatesANewClient() {
        ClientForm clientForm = new ClientForm(client1);
        clientForm.setName("Avengers");
        clientForm.setEmail("avengers@marvel.tst");
        Client client = clientService.saveClient(clientForm);

        ClientForm savedForm = clientService.getClient(client.getInteger("id"));

        assertThat(savedForm.getName(), is(equalTo(clientForm.getName())));
        assertThat(savedForm.getEmail(), is(equalTo(clientForm.getEmail())));
    }

    @Test
    public void saveUpdatesAClient() {
        ClientForm clientForm = new ClientForm();
        clientForm.setName("Avengers");
        clientForm.setEmail("avengers@marvel.tst");
        clientForm.setId(client1.getInteger("id"));
        Client client = clientService.saveClient(clientForm);
        client1.refresh();
        assertThat(client1.getString("name"), is(equalTo("Avengers")));
        assertThat(client1.getString("email"), is(equalTo("avengers@marvel.tst")));
        assertThat(client1.getInteger("id"), is(equalTo(client.getId())));
    }
}
