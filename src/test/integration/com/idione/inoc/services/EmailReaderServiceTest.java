package com.idione.inoc.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.forms.EmailForm;
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
    private String emailSubject = "Subject for the email";
    private String emailId = "6ad7248DFG12345";
    private EmailForm emailForm;

    @Before
    public void setupEmail(){
        emailForm = new EmailForm();
        emailForm.setEmailText(emailText);
        emailForm.setEmailSubject(emailSubject);
        emailForm.setEmailId(emailId);
        emailReaderService = new EmailReaderService();
    }

    @Test
    public void processIssueCreationEmailCreatesEmailRecord(@Mocked FilterMatchingService filterMatchingService) throws Exception {
        int beforeEmailCount = Email.findAll().size();
        new Expectations() {
            {
                filterMatchingService.matchFiltersForEmail(anyInt, (Email)any);
            }
        };
        emailReaderService.setFilterMatchingService(filterMatchingService);
        emailReaderService.processIssueCreationEmail(clientId, emailForm);
        int afterEmailCount = Email.findAll().size();
        assertThat(afterEmailCount, is(equalTo(beforeEmailCount + 1)));
    }

    @Test
    public void processIssueCreationEmailCallsTheFilterMatchingService(@Mocked FilterMatchingService filterMatchingService) throws Exception {
        new Expectations() {
            {
                filterMatchingService.matchFiltersForEmail(anyInt, (Email)any);
            }
        };
        emailReaderService.setFilterMatchingService(filterMatchingService);
        emailReaderService.processIssueCreationEmail(clientId, emailForm);
        Email lastEmail = (Email) Email.where("external_email_id = ?", emailId).orderBy("id desc").get(0);
        lastEmail.setEmailText(emailText);
        lastEmail.setEmailSubject(emailSubject);

        new Verifications() {
            {
                Email receivedEmail;
                filterMatchingService.matchFiltersForEmail(clientId, receivedEmail = withCapture());
                assertThat(lastEmail.getEmailId(), is(equalTo(receivedEmail.getEmailId())));
                assertThat(lastEmail.getEmailSubject(), is(equalTo(receivedEmail.getEmailSubject())));
                assertThat(lastEmail.getEmailText(), is(equalTo(receivedEmail.getEmailText())));
            }
        };
    }

    @Test
    public void processIssueStatusEmailCreatesEmailRecord(@Mocked IssueResponseService issueResponseService) throws Exception {
        int beforeEmailCount = Email.findAll().size();
        new Expectations() {
            {
                issueResponseService.updateIssueWithEmailResponse(anyInt, (Email)any);
            }
        };
        emailReaderService.setIssueResponseService(issueResponseService);
        emailReaderService.processIssueStatusEmail(clientId, emailForm);
        int afterEmailCount = Email.findAll().size();
        assertThat(afterEmailCount, is(equalTo(beforeEmailCount + 1)));
    }

    @Test
    public void processIssueStatusEmailCallsTheIssueResponseService(@Mocked IssueResponseService issueResponseService) throws Exception {
        new Expectations() {
            {
                issueResponseService.updateIssueWithEmailResponse(anyInt, (Email)any);
            }
        };
        emailReaderService.setIssueResponseService(issueResponseService);
        emailReaderService.processIssueStatusEmail(clientId, emailForm);
        Email lastEmail = (Email) Email.where("external_email_id = ?", emailId).orderBy("id desc").get(0);
        lastEmail.setEmailText(emailText);
        lastEmail.setEmailSubject(emailSubject);

        new Verifications() {
            {
                Email receivedEmail;
                issueResponseService.updateIssueWithEmailResponse(clientId, receivedEmail = withCapture());
                assertThat(lastEmail.getEmailId(), is(equalTo(receivedEmail.getEmailId())));
                assertThat(lastEmail.getEmailSubject(), is(equalTo(receivedEmail.getEmailSubject())));
                assertThat(lastEmail.getEmailText(), is(equalTo(receivedEmail.getEmailText())));
            }
        };
    }
}
