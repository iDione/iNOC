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
import com.idione.inoc.models.TelephoneCall;
import com.idione.inoc.services.IssueAssignmentService;
import com.idione.inoc.services.TelephoneService;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class IssueResponseServiceTest extends AbstractIntegrationTest {

    IssueResponseService issueResponseService;
    Client client;
    Filter filter;
    Issue issue;
    PocUser pocUser;
    TelephoneCall telephoneCall;
    String telephoneNumber1 = "1111111111";
    IssuePocUser issuePocUser;
    int retries = 2;
    String externalCallId = "w9g3j3f7hd85m";

    @Before
    public void createFilter() {
        client = Client.createIt("name", "Mickey Mouse Club House");
        Email email = Email.createIt("client_id", client.getInteger("id"));
        MailingGroup mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"));
        filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", retries, "mailing_group_id", mailingGroup.getInteger("id"));
        issue = Issue.createIt("email_id", email.getInteger("id"), "filter_id", filter.getInteger("id"));
        pocUser = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Mickey", "last_name", "Mouse", "phone_number", telephoneNumber1);
        issuePocUser = IssuePocUser.createIt("issue_id", issue.getInteger("id"), "poc_user_id", pocUser.getInteger("id"));
        telephoneCall = TelephoneCall.createIt("issue_poc_user_id", issuePocUser.getInteger("id"), "external_call_id", externalCallId, "call_Status", TelephoneService.RINGING_STATUS.toString());
    }

    @Test
    public void updateIssueWithUserResponseUpdatesTelephoneCall() {
        issueResponseService = new IssueResponseService();
        issueResponseService.updateIssueWithUserResponse(IssueResponseService.ISSUE_DECLINED_RESPONSE, externalCallId);
        telephoneCall.refresh();
        assertThat(telephoneCall.getInteger("user_response"), is(equalTo(IssueResponseService.ISSUE_DECLINED_RESPONSE)));
    }
    
    @Test
    public void updateIssueWithUserResponseUpdatesIssuePocUser() {
        issueResponseService = new IssueResponseService();
        issueResponseService.updateIssueWithUserResponse(IssueResponseService.ISSUE_DECLINED_RESPONSE, externalCallId);
        issuePocUser.refresh();
        assertThat(issuePocUser.getString("user_response"), is(equalTo(IssueResponseService.ISSUE_DECLINED_STATUS)));
    }
}
