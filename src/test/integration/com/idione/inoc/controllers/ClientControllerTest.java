package com.idione.inoc.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ui.Model;

import com.idione.inoc.forms.ClientForm;
import com.idione.inoc.models.Client;
import com.idione.inoc.services.ClientService;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class ClientControllerTest extends AbstractIntegrationTest {

    final List<Client> clients = new ArrayList<Client>();
    final String name = "Avengers";
    final int id = 101;

    @Test
    public void getClientsCallsTheClientServiceForClients(@Mocked Model model, @Mocked ClientService clientService) {
        ClientsController controller = new ClientsController();
        controller.setClientService(clientService);

        new Expectations() {
            {
                clientService.getClients(name);
                result = clients;
            }
        };

        controller.getClients(name, model);

        new Verifications() {
            {
                clientService.getClients(name);
                times = 1;
                model.addAttribute("clients", clients);
                times = 1;
            }
        };
    }

    @Test
    public void editClientCallsTheClientServiceForClient(@Mocked Model model, @Mocked ClientService clientService) {
        ClientsController controller = new ClientsController();
        controller.setClientService(clientService);
        ClientForm clientForm = new ClientForm();

        new Expectations() {
            {
                clientService.getClient(id);
                result = clientForm;
            }
        };

        controller.editClient(id, model);

        new Verifications() {
            {
                clientService.getClient(id);
                times = 1;
                model.addAttribute("clientForm", clientForm);
                times = 1;
            }
        };
    }

    @Test
    public void saveClientCallsTheClientServiceForSave(@Mocked Model model, @Mocked ClientService clientService) {
        ClientsController controller = new ClientsController();
        controller.setClientService(clientService);
        ClientForm clientForm = new ClientForm();

        controller.saveClient(clientForm);

        new Verifications() {
            {
                clientService.saveClient(clientForm);
                times = 1;
            }
        };
    }
}
