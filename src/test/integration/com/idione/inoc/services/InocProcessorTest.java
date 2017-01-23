package com.idione.inoc.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.idione.inoc.forms.EmailForm;
import com.idione.inoc.models.Client;
import com.idione.inoc.models.Email;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class InocProcessorTest extends AbstractIntegrationTest {

    InocProcessor inocProcessor;
    Client client1;
    Client client2;
    EmailReader emailReader1;
    EmailReader emailReader2;
    EmailForm emailForm1;
    EmailForm emailForm2;
    EmailForm emailForm3;

    @Before
    public void createFilter() {
        client1 = Client.createIt("name", "Mickey Mouse Club House", "host", "host1", "email", "email1", "password", "password1");
        client2 = Client.createIt("name", "Avengers Club", "host", "host2", "email", "email2", "password", "password2");
        emailForm1 = new EmailForm("abcdef", "a to f");
        emailForm2 = new EmailForm("ghijk", "g to k");
        emailForm3 = new EmailForm("lmnop", "l to p");
    }

    @Test
    public void itChecksEmailsForAllTheClients(@Mocked EmailReader emailReader, @Mocked ClientService clientService, @Mocked FilterMatchingService filterMatchingService) throws MessagingException, IOException {
        {
            InocProcessor inocProcessor = new InocProcessor(clientService, filterMatchingService);
            final List<Client> allClients = new ArrayList<Client>();
            allClients.add(client1);
            allClients.add(client2);
            final List<EmailForm> client1Emails = new ArrayList<EmailForm>();
            client1Emails.add(emailForm1);
            client1Emails.add(emailForm2);
            final List<EmailForm> client2Emails = new ArrayList<EmailForm>();
            client2Emails.add(emailForm3);

            emailReader1 = new EmailReader("host1", "email1", "password1");
            emailReader2 = new EmailReader("host2", "email2", "password2");
            new Expectations() {
                {
                    clientService.getClients("");
                    result = allClients;
                    new EmailReader("host1", "email1", "password1");
                    result = emailReader1;
                    new EmailReader("host2", "email2", "password2");
                    result = emailReader2;
                    emailReader1.processInbox();
                    result = client1Emails;
                    emailReader2.processInbox();
                    result = client2Emails;
                    filterMatchingService.matchFiltersForEmail(anyInt, anyInt, anyString);
                }
            };

            inocProcessor.run();

            new Verifications() {
                {
                    filterMatchingService.matchFiltersForEmail(anyInt, client1.getInteger("id"), "a to f");
                    times = 1;
                    filterMatchingService.matchFiltersForEmail(anyInt, client1.getInteger("id"), "g to k");
                    times = 1;
                    filterMatchingService.matchFiltersForEmail(anyInt, client2.getInteger("id"), "l to p");
                    times = 1;
                }
            };
        }
    }

    @Test
    public void itLogsTheEmail(@Mocked EmailReader emailReader, @Mocked ClientService clientService, @Mocked FilterMatchingService filterMatchingService) throws MessagingException, IOException {
        {
            InocProcessor inocProcessor = new InocProcessor(clientService, filterMatchingService);
            final List<Client> allClients = new ArrayList<Client>();
            allClients.add(client2);
            final List<EmailForm> client2Emails = new ArrayList<EmailForm>();
            client2Emails.add(emailForm3);

            emailReader2 = new EmailReader("host2", "email2", "password2");
            new Expectations() {
                {
                    clientService.getClients("");
                    result = allClients;
                    new EmailReader("host2", "email2", "password2");
                    result = emailReader2;
                    emailReader2.processInbox();
                    result = client2Emails;
                    filterMatchingService.matchFiltersForEmail(anyInt, anyInt, anyString);
                }
            };
            int noOfEmailsProcessedBefore = Email.findAll().size();
            inocProcessor.run();
            assertThat(Email.findAll().size(), is(equalTo(noOfEmailsProcessedBefore + 1)));
        }
    }
}
