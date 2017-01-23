package com.idione.inoc.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.idione.inoc.forms.ClientForm;
import com.idione.inoc.models.Client;

@Service
public class ClientService {
    public List<Client> getClients(String name) {
        if (StringUtils.isEmpty(name)) {
            return Client.findAll();
        } else {
            return Client.find("name like ?", "%" + name + "%");
        }

    }

    public ClientForm getClient(int id) {
        Client client = Client.findFirst("id = ?", id);
        if (client != null) {
            return new ClientForm(client);
        } else {
            return new ClientForm();
        }

    }

    public Client saveClient(ClientForm clientForm) {
        if (clientForm.getId() > 0) {
            Client client = Client.findFirst("id = ?", clientForm.getId());
            client.set("name", clientForm.getName(), "email", clientForm.getEmail(), "host", clientForm.getHost(), "password", clientForm.getPassword()).saveIt();
            return client;
        } else {
            return Client.createIt("name", clientForm.getName(), "email", clientForm.getEmail(), "host", clientForm.getHost(), "password", clientForm.getPassword());
        }
    }
}
