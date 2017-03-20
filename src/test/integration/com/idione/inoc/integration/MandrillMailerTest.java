package com.idione.inoc.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.idione.inoc.services.EmailSenderService;
import com.idione.inoc.test.AbstractIntegrationTest;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;

@Ignore
public class MandrillMailerTest extends AbstractIntegrationTest {

    MandrillMailer mandrillMailer;

    @Before
    public void createFilter() {
        mandrillMailer = new MandrillMailer();
    }

    @Test
    public void sendMail() {
        String[] emailStatuses;
        String[] tos = {"adarshadarsh@gmail.com", "simpleboy007@yahoo.com"};
        try {
            emailStatuses = mandrillMailer.sendMail("inocissues@gmail.com", tos, "Test Mandrill Email", "Test email to check if mails are going out from our account");
            for(String emailStatus : emailStatuses){
                if(EmailSenderService.failureEmailStatuses.contains(emailStatus)) {
                    assertThat(true, is(equalTo(false)));
                }
            }
            assertThat(true, is(equalTo(true)));
        } catch (MandrillApiError | IOException e) {
            e.printStackTrace();
        }
    }
}
