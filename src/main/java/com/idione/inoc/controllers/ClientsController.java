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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.idione.inoc.forms.ClientForm;
import com.idione.inoc.models.Client;
import com.idione.inoc.services.ClientService;

@RequestMapping(value = "/clients")
@Controller
public class ClientsController extends ApplicationController {

    private ClientService clientService;

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String getClients(@RequestParam(value = "name", required = false) final String name, Model model) {
        List<Client> clients = clientService.getClients(name);
        model.addAttribute("clients", clients);
        return "clients/index";
    }
    
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String newClient(Model model) {
        model.addAttribute("clientForm", new ClientForm());
        return "clients/new";
    }
    
    @RequestMapping(value = "/edit/{clientId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String editClient(@PathVariable int clientId, Model model) {
        ClientForm clientForm = clientService.getClient(clientId);
        model.addAttribute("clientForm", clientForm);
        return "clients/edit";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String saveClient(@ModelAttribute ClientForm clientForm) {
        clientService.saveClient(clientForm);
        return "clients/index";
    }
}
