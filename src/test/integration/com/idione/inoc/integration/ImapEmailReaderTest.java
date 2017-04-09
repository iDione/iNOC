package com.idione.inoc.integration;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.MessageIDTerm;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.idione.inoc.forms.EmailForm;
import com.idione.inoc.test.AbstractIntegrationTest;

@Ignore
public class ImapEmailReaderTest extends AbstractIntegrationTest {

    ImapEmailReader emailReader;
    private String HOST = "imap.gmail.com";
    private String USERNAME = "inocdemo@gmail.com";
    private String PASSWORD = "Canyouhearmenow.";

    @Before
    public void createFilter() {
        try {
            emailReader = new ImapEmailReader(HOST, USERNAME, PASSWORD);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void itGetsEmailsFromGmailDemoAccount() {
        try {
            emailReader.processInbox();
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void itGetsBackSpecificEmail() throws MessagingException, IOException {
        EmailForm emailForm = emailReader.getMessage("<a78221cc964c8c18df86e1a39021399e@webhooks.twilio.com>");
        if (emailForm != null) {
            System.out.println(emailForm.getEmailSubject());
            System.out.println(emailForm.getEmailText());
            System.out.println(emailForm.getEmailId());
        }

    }
}
