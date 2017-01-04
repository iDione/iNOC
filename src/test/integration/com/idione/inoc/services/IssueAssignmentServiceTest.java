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
import com.idione.inoc.models.PocUser;
import com.idione.inoc.services.IssueAssignmentService;
import com.idione.inoc.services.TelephoneService;
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
    PocUser pocUser1;
    PocUser pocUser2;
    String telephoneNumber1 = "1111111111";
    String telephoneNumber2 = "2222222222";
    int retries = 2;

    @Before
    public void createFilter() {
        client = Client.createIt("name", "Mickey Mouse Club House");
        Email email = Email.createIt("client_id", client.getInteger("id"));
        MailingGroup mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"));
        filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", retries, "mailing_group_id", mailingGroup.getInteger("id"));
        issue = Issue.createIt("email_id", email.getInteger("id"), "filter_id", filter.getInteger("id"));
        pocUser1 = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Mickey", "last_name", "Mouse", "phone_number", telephoneNumber1);
        FilterPocUser.createIt("filter_id", filter.getInteger("id"), "poc_user_id", pocUser1.getInteger("id"));
    }

    @Test
    public void itCallsAnUserForTheFilter(@Mocked TelephoneService telephoneService) {
        new Expectations() {
            {
                telephoneService.makeIssueAcceptanceCall(anyInt, telephoneNumber1, retries);
                result = "accepted";
            }
        };

        issueAssignmentService = new IssueAssignmentService(telephoneService);
        issueAssignmentService.assignIssueToPOCUser(issue);
        IssuePocUser issuePocUser = IssuePocUser.findFirst("issue_id = ? and poc_user_id = ?", issue.getInteger("id"), pocUser1.getInteger("id"));
        new Verifications() {
            {
                telephoneService.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), telephoneNumber1, retries);
                times = 1;
            }
        };
    }

    @Test
    public void itCreatesAnIssuePocUserWithTheRightInformation(@Mocked TelephoneService telephoneService) {
        new Expectations() {
            {
                telephoneService.makeIssueAcceptanceCall(anyInt, telephoneNumber1, retries);
                result = "accepted";
            }
        };

        issueAssignmentService = new IssueAssignmentService(telephoneService);
        issueAssignmentService.assignIssueToPOCUser(issue);

        IssuePocUser issuePocUser = (IssuePocUser) IssuePocUser.where("issue_id = ?", issue.getInteger("id")).get(0);
        assertThat(issuePocUser.getInteger("poc_user_id"), is(equalTo(pocUser1.getInteger("id"))));
    }

    @Test
    public void itCallsSecondUserForTheFilterIfFirstUserDeclines(@Mocked TelephoneService telephoneService) {
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

        issueAssignmentService = new IssueAssignmentService(telephoneService);
        issueAssignmentService.assignIssueToPOCUser(issue);
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
}
