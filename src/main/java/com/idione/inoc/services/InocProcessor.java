package com.idione.inoc.services;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.idione.inoc.forms.EmailForm;
import com.idione.inoc.models.Client;

@Service
public class InocProcessor {

    private ClientService clientService;
    private EmailReaderService emailReaderService;

    public InocProcessor(ClientService clientService, EmailReaderService emailReaderService) {
        this.clientService = clientService;
        this.emailReaderService = emailReaderService;
    }

    public void run() {
        List<Client> clients = clientService.getClients("");
        for (Client client : clients) {
            try {
                EmailReader emailReader = new EmailReader(client.getString("host"), client.getString("email"), client.getString("password"));
                List<EmailForm> newEmailsForClient = emailReader.processInbox();
                for (EmailForm emailForm : newEmailsForClient) {
                    emailReaderService.processEmail(client.getClientId(), emailForm);
                }
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
