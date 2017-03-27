package com.idione.inoc.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.forms.EmailForm;
import com.idione.inoc.integration.ImapEmailReader;
import com.idione.inoc.models.Client;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class InocIssueCreatorTest extends AbstractIntegrationTest {

    InocIssueCreator inocProcessor;
    Client client1;
    Client client2;
    ImapEmailReader emailReader1;
    ImapEmailReader emailReader2;
    EmailForm emailForm1;
    EmailForm emailForm2;
    EmailForm emailForm3;

    @Before
    public void createFilter() {
        client1 = Client.createIt("name", "Mickey Mouse Club House", "host", "host1", "email", "email1", "password", "password1");
        client2 = Client.createIt("name", "Avengers Club", "host", "host2", "email", "email2", "password", "password2");
        emailForm1 = new EmailForm("abcdef", "a to f", "a2f");
        emailForm2 = new EmailForm("ghijk", "g to k", "g2k");
        emailForm3 = new EmailForm("lmnop", "l to p", "l2p");
    }

    @Test
    public void itProcessesAllEmailsForAllTheClients(@Mocked ImapEmailReader emailReader, @Mocked ClientService clientService, @Mocked EmailReaderService emailReaderService) throws MessagingException, IOException {
        {
            InocIssueCreator inocProcessor = new InocIssueCreator(clientService, emailReaderService);
            final List<Client> allClients = new ArrayList<Client>();
            allClients.add(client1);
            allClients.add(client2);
            final List<EmailForm> client1Emails = new ArrayList<EmailForm>();
            client1Emails.add(emailForm1);
            client1Emails.add(emailForm2);
            final List<EmailForm> client2Emails = new ArrayList<EmailForm>();
            client2Emails.add(emailForm3);

            emailReader1 = new ImapEmailReader("host1", "email1", "password1");
            emailReader2 = new ImapEmailReader("host2", "email2", "password2");
            new Expectations() {
                {
                    clientService.getClients("");
                    result = allClients;
                    new ImapEmailReader("host1", "email1", "password1");
                    result = emailReader1;
                    new ImapEmailReader("host2", "email2", "password2");
                    result = emailReader2;
                    emailReader1.processInbox();
                    result = client1Emails;
                    emailReader2.processInbox();
                    result = client2Emails;
                    emailReaderService.processIssueCreationEmail(anyInt, (EmailForm) any);
                }
            };

            inocProcessor.run();

            new Verifications() {
                {
                    emailReaderService.processIssueCreationEmail(client1.getInteger("id"), emailForm1);
                    times = 1;
                    emailReaderService.processIssueCreationEmail(client1.getInteger("id"), emailForm2);
                    times = 1;
                    emailReaderService.processIssueCreationEmail(client2.getInteger("id"), emailForm3);
                    times = 1;
                }
            };
        }
    }
}
