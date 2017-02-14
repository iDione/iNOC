package com.idione.inoc.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.integration.MandrillMailer;
import com.idione.inoc.test.AbstractIntegrationTest;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class EmailSenderTest extends AbstractIntegrationTest {

    private String from = "from@inoc.tst";
    private String [] tos = { "to1@inoc.tst", "to2@inoc.tst" };
    private String messageText = "blah blah blah";
    private String subject = "hello";
    private final String [] returnStatuses = {"success", EmailSenderService.failureEmailStatuses.get(0) };
    private final String [] successReturnStatuses = {"success", "success" };
    
    @Test
    public void itCallsTheMaindrillMailerToSendEmail(@Mocked MandrillMailer mandrillMailer) throws MandrillApiError, IOException {
        EmailSenderService emailSenderService = new EmailSenderService(mandrillMailer);
        
        new Expectations() {
            {
                mandrillMailer.sendMail(from, tos, subject, messageText);
                result = returnStatuses;
            }
        };

        emailSenderService.sendMail(from, tos, subject, messageText);

        new Verifications() {
            {
                mandrillMailer.sendMail(from, tos, subject, messageText);
                times = 1;
            }
        };
    }
    
    @Test
    public void itReturnsFalseIfEvenOneEmailFails(@Mocked MandrillMailer mandrillMailer) throws MandrillApiError, IOException {
        EmailSenderService emailSenderService = new EmailSenderService(mandrillMailer);
        
        new Expectations() {
            {
                mandrillMailer.sendMail(from, tos, subject, messageText);
                result = returnStatuses;
            }
        };

        boolean returnStatus = emailSenderService.sendMail(from, tos, subject, messageText);
        assertThat(returnStatus, is(equalTo(false)));
    }
    
    @Test
    public void itReturnsTrueIfAllEmailSucceed(@Mocked MandrillMailer mandrillMailer) throws MandrillApiError, IOException {
        EmailSenderService emailSenderService = new EmailSenderService(mandrillMailer);
        
        new Expectations() {
            {
                mandrillMailer.sendMail(from, tos, subject, messageText);
                result = successReturnStatuses;
            }
        };

        boolean returnStatus = emailSenderService.sendMail(from, tos, subject, messageText);
        assertThat(returnStatus, is(equalTo(true)));
    }

}
