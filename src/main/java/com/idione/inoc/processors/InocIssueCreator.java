package com.idione.inoc.processors;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.stereotype.Service;

import com.idione.inoc.forms.EmailForm;
import com.idione.inoc.integration.ImapEmailReader;
import com.idione.inoc.models.Client;
import com.idione.inoc.services.ClientService;
import com.idione.inoc.services.EmailReaderService;

@Service
public class InocIssueCreator {

    private ClientService clientService;
    private EmailReaderService emailReaderService;

    public InocIssueCreator(ClientService clientService, EmailReaderService emailReaderService) {
        this.clientService = clientService;
        this.emailReaderService = emailReaderService;
    }

    public void run() {
        List<Client> clients = clientService.getClients("");
        for (Client client : clients) {
            try {
                ImapEmailReader emailReader = new ImapEmailReader(client.getString("host"), client.getString("email"), client.getString("password"));
                List<EmailForm> newEmailsForClient = emailReader.processInbox();
                for (EmailForm emailForm : newEmailsForClient) {
                    emailReaderService.processIssueCreationEmail(client.getClientId(), emailForm);
                }
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
