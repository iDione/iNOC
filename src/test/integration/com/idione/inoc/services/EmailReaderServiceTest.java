package com.idione.inoc.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.models.Email;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class EmailReaderServiceTest extends AbstractIntegrationTest {

    EmailReaderService emailReaderService;

    private int clientId = 2016;
    private String emailText = "This is an email text.";
    private String emailId = "6ad7248DFG12345";

    @Test
    public void processEmailCreatesEmailRecord(@Mocked FilterMatchingService filterMatchingService) throws Exception {
        int beforeEmailCount = Email.findAll().size();
        new Expectations() {
            {
                filterMatchingService.matchFiltersForEmail(anyInt, anyInt, anyString);
            }
        };
        emailReaderService = new EmailReaderService(filterMatchingService);
        emailReaderService.processEmail(clientId, emailText, emailId);
        int afterEmailCount = Email.findAll().size();
        assertThat(afterEmailCount, is(equalTo(beforeEmailCount + 1)));
    }

    @Test
    public void processEmailCallsTheFilterMatchingService(@Mocked FilterMatchingService filterMatchingService) throws Exception {
        emailReaderService = new EmailReaderService(filterMatchingService);
        emailReaderService.processEmail(clientId, emailText, emailId);
        int lastEmailId = Email.where("external_email_id = ?", emailId).orderBy("id desc").get(0).getInteger("id");
        new Verifications() {
            {
                filterMatchingService.matchFiltersForEmail(lastEmailId, clientId, emailText);
            }
        };
    }
}
