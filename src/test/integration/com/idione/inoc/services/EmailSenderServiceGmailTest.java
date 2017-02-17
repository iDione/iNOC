package com.idione.inoc.services;

import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.integration.GmailMailer;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class EmailSenderServiceGmailTest extends AbstractIntegrationTest {

    private String from = "from@inoc.tst";
    private String [] tos = { "to1@inoc.tst", "to2@inoc.tst" };
    private String messageText = "blah blah blah";
    private String subject = "hello";
    
    @Test
    public void itCallsTheGmailMailerToSendEmail(@Mocked GmailMailer gmailMailer) throws MessagingException {
        EmailSenderService emailSenderService = new EmailSenderService();
        emailSenderService.setGmailMailer(gmailMailer);
        new Expectations() {
            {
                gmailMailer.send(tos, subject, messageText);
            }
        };

        emailSenderService.sendMailViaGmail(from, tos, subject, messageText);

        new Verifications() {
            {
                gmailMailer.send(tos, subject, messageText);
                times = 1;
            }
        };
    }
}
