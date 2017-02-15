package com.idione.inoc.integration;

import java.io.IOException;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.idione.inoc.integration.ImapEmailReader;
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
}
