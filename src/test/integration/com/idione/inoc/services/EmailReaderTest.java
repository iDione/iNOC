package com.idione.inoc.services;

import java.io.IOException;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.idione.inoc.services.EmailReader;
import com.idione.inoc.test.AbstractIntegrationTest;

@Ignore
public class EmailReaderTest extends AbstractIntegrationTest {

    EmailReader emailReader;
    private String HOST = "imap.gmail.com";
    private String USERNAME = "inocdemo@gmail.com";
    private String PASSWORD = "canyouhearmenow";

    @Before
    public void createFilter() {
        try {
            emailReader = new EmailReader(HOST, USERNAME, PASSWORD);
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
