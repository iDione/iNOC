package com.idione.inoc.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.idione.inoc.test.AbstractIntegrationTest;

@Ignore
public class GmailMailerTest extends AbstractIntegrationTest {

    GmailMailer gmailMailer;

    @Before
    public void createFilter() {
        gmailMailer = new GmailMailer();
    }

    @Test
    public void sendMail() {
        String[] tos = { "adarshadarsh@gmail.com", "simpleboy007@yahoo.com" };
        try {
            gmailMailer.send(tos, "Test Mandrill Email", "Test email to check if mails are going out from our account");
            assertThat(true, is(equalTo(true)));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
