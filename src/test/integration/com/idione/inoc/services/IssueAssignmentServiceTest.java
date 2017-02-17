package com.idione.inoc.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.models.Client;
import com.idione.inoc.models.Email;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.FilterPocUser;
import com.idione.inoc.models.Issue;
import com.idione.inoc.models.IssuePocUser;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.MailingGroupPocUser;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class IssueAssignmentServiceTest extends AbstractIntegrationTest {

    IssueAssignmentService issueAssignmentService;
    Client client;
    Filter filter;
    Issue issue;
    Email email;
    PocUser pocUser1;
    PocUser pocUser2;
    MailingGroup mailingGroup;
    String telephoneNumber1 = "1111111111";
    String telephoneNumber2 = "2222222222";
    int retries = 2;
    String emailSubject = "Fun Times";
    String emailText = "Lets do the hot dog dance";
    @Before
    public void createFilter() {
        client = Client.createIt("name", "Mickey Mouse Club House");
        pocUser1 = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Mickey", "last_name", "Mouse", "phone_number", telephoneNumber1, "email_address", "mickey@inoc.tst");
        email = Email.createIt("client_id", client.getInteger("id"));
        email.setEmailSubject(emailSubject);
        email.setEmailText(emailText);
        mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id")); 
        filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", retries, "assigned_mailing_group_id", mailingGroup.getInteger("id"), "unassigned_mailing_group_id", mailingGroup.getInteger("id"), "unassigned_email_template", "un assigned email template", "assigned_email_template", "assigned email template");
        issue = Issue.createIt("email_id", email.getInteger("id"), "filter_id", filter.getInteger("id"));
        FilterPocUser.createIt("filter_id", filter.getInteger("id"), "poc_user_id", pocUser1.getInteger("id"));
    }

    @Test
    public void itCallsAnUserForTheFilter(@Mocked TelephoneService telephoneService, @Mocked EmailSenderService emailSenderService) {
        new Expectations() {
            {
                telephoneService.makeIssueAcceptanceCall(anyInt, telephoneNumber1, retries);
                result = "accepted";
            }
        };

        issueAssignmentService = new IssueAssignmentService(telephoneService, emailSenderService);
        issueAssignmentService.assignIssueToPOCUser(issue, email);
        IssuePocUser issuePocUser = IssuePocUser.findFirst("issue_id = ? and poc_user_id = ?", issue.getInteger("id"), pocUser1.getInteger("id"));
        new Verifications() {
            {
                telephoneService.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), telephoneNumber1, retries);
                times = 1;
            }
        };
    }

    @Test
    public void itCreatesAnIssuePocUserWithTheRightInformation(@Mocked TelephoneService telephoneService, @Mocked EmailSenderService emailSenderService) {
        new Expectations() {
            {
                telephoneService.makeIssueAcceptanceCall(anyInt, telephoneNumber1, retries);
                result = "accepted";
            }
        };

        issueAssignmentService = new IssueAssignmentService(telephoneService, emailSenderService);
        issueAssignmentService.assignIssueToPOCUser(issue, email);

        IssuePocUser issuePocUser = (IssuePocUser) IssuePocUser.where("issue_id = ?", issue.getInteger("id")).get(0);
        assertThat(issuePocUser.getInteger("poc_user_id"), is(equalTo(pocUser1.getInteger("id"))));
    }

    @Test
    public void itCallsSecondUserForTheFilterIfFirstUserDeclines(@Mocked TelephoneService telephoneService, @Mocked EmailSenderService emailSenderService) {
        pocUser2 = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Donald", "last_name", "Duck", "phone_number", "2222222222");
        FilterPocUser.createIt("filter_id", filter.getInteger("id"), "poc_user_id", pocUser2.getInteger("id"));

        new Expectations() {
            {
                telephoneService.makeIssueAcceptanceCall(anyInt, telephoneNumber1, retries);
                result = "declined";
                telephoneService.makeIssueAcceptanceCall(anyInt, telephoneNumber2, retries);
                result = "accepted";
            }
        };

        issueAssignmentService = new IssueAssignmentService(telephoneService, emailSenderService);
        issueAssignmentService.assignIssueToPOCUser(issue, email);
        IssuePocUser issuePocUser1 = IssuePocUser.findFirst("issue_id = ? and poc_user_id = ?", issue.getInteger("id"), pocUser1.getInteger("id"));
        IssuePocUser issuePocUser2 = IssuePocUser.findFirst("issue_id = ? and poc_user_id = ?", issue.getInteger("id"), pocUser2.getInteger("id"));
        new Verifications() {
            {
                telephoneService.makeIssueAcceptanceCall(issuePocUser1.getInteger("id"), telephoneNumber1, retries);
                times = 1;
                telephoneService.makeIssueAcceptanceCall(issuePocUser2.getInteger("id"), telephoneNumber2, retries);
                times = 1;
            }
        };
    }

    @Test
    public void itSendsAUnassignedEmailIfAllUsersDecline(@Mocked TelephoneService telephoneService, @Mocked EmailSenderService emailSenderService) {
        MailingGroupPocUser.createIt("mailing_group_id", mailingGroup.getInteger("id"), "poc_user_id", pocUser1.getInteger("id"));
        final String[] expectedTo = { pocUser1.getString("email_address") };
        new Expectations() {
            {
                telephoneService.makeIssueAcceptanceCall(anyInt, anyString, anyInt);
                result = "declined";
            }
        };

        issueAssignmentService = new IssueAssignmentService(telephoneService, emailSenderService);
        issueAssignmentService.assignIssueToPOCUser(issue, email);

        new Verifications() {
            {
                emailSenderService.sendMailViaGmail(EmailSenderService.INOC_EMAIL_ADDRESS, expectedTo, "Issue Not Assigned To Anyone", "un assigned email template");
                times = 1;
            }
        };
    }

    @Test
    public void itSendsAAssignedEmailIfAnUsersAccepts(@Mocked TelephoneService telephoneService, @Mocked EmailSenderService emailSenderService) {
        MailingGroupPocUser.createIt("mailing_group_id", mailingGroup.getInteger("id"), "poc_user_id", pocUser1.getInteger("id"));
        final String[] expectedTo = { pocUser1.getString("email_address") };
        new Expectations() {
            {
                telephoneService.makeIssueAcceptanceCall(anyInt, telephoneNumber1, retries);
                result = "accepted";
            }
        };

        issueAssignmentService = new IssueAssignmentService(telephoneService, emailSenderService);
        issueAssignmentService.assignIssueToPOCUser(issue, email);

        new Verifications() {
            {
                emailSenderService.sendMailViaGmail(EmailSenderService.INOC_EMAIL_ADDRESS, expectedTo, "Issue Assigned", "assigned email template");
                times = 1;
            }
        };
    }
}
