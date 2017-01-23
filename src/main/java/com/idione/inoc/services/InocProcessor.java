package com.idione.inoc.services;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.idione.inoc.forms.EmailForm;
import com.idione.inoc.models.Client;
import com.idione.inoc.models.Email;

@Service
public class InocProcessor {

    private ClientService clientService;
    private FilterMatchingService filterMatchingService;

    public InocProcessor(ClientService clientService, FilterMatchingService filterMatchingService) {
        this.clientService = clientService;
        this.filterMatchingService = filterMatchingService;
    }

    public void run() {
        List<Client> clients = clientService.getClients("");
        for (Client client : clients) {
            try {
                EmailReader emailReader = new EmailReader(client.getString("host"), client.getString("email"), client.getString("password"));
                List<EmailForm> newEmailsForClient = emailReader.processInbox();
                for (EmailForm emailForm : newEmailsForClient) {
                    Email email = Email.createIt("client_id", client.getClientId(), "external_email_id", emailForm.getEmailId());
                    filterMatchingService.matchFiltersForEmail(email.getInteger("id"), client.getClientId(), emailForm.getEmailText());
                }
            } catch (MessagingException | IOException e) {
                // log error that email unable to be read for client oh! for a
                // logging system
                e.printStackTrace();
            }
        }
    }
}
