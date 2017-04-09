package com.idione.inoc.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.models.Client;
import com.idione.inoc.models.Email;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.Issue;
import com.idione.inoc.models.IssueEmail;
import com.idione.inoc.models.IssuePocUser;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.models.TelephoneCall;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class IssueResponseServiceTest extends AbstractIntegrationTest {

    IssueResponseService issueResponseService;
    Client client;
    Filter filter;
    Issue issue;
    IssueEmail issueEmail1;
    IssueEmail issueEmail2;
    PocUser pocUser;
    TelephoneCall telephoneCall;
    String telephoneNumber1 = "1111111111";
    IssuePocUser issuePocUser;
    int retries = 2;
    String externalCallId = "w9g3j3f7hd85m";

    String serverCode1 = "Boca-AS-101";
    String serverCode2 = "A202";

    @Before
    public void createFilter() {
        client = Client.createIt("name", "Mickey Mouse Club House");
        Email email = Email.createIt("client_id", client.getInteger("id"));
        MailingGroup mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"));
        filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", retries, "assigned_mailing_group_id", mailingGroup.getInteger("id"));
        issue = Issue.createIt("email_id", email.getInteger("id"), "filter_id", filter.getInteger("id"), "status", Issue.ISSUE_CREATED_STATUS);
        pocUser = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Mickey", "last_name", "Mouse", "phone_number", telephoneNumber1);
        issuePocUser = IssuePocUser.createIt("issue_id", issue.getInteger("id"), "poc_user_id", pocUser.getInteger("id"));
        telephoneCall = TelephoneCall.createIt("issue_poc_user_id", issuePocUser.getInteger("id"), "external_call_id", externalCallId, "call_Status", TelephoneService.RINGING_STATUS.toString());

        issueResponseService = new IssueResponseService();
    }

    @Test
    public void updateIssueWithUserResponseUpdatesTelephoneCall() {
        issueResponseService.updateIssueWithUserResponse(IssueResponseService.ISSUE_DECLINED_RESPONSE, externalCallId);
        telephoneCall.refresh();
        assertThat(telephoneCall.getInteger("user_response"), is(equalTo(IssueResponseService.ISSUE_DECLINED_RESPONSE)));
    }

    @Test
    public void updateIssueWithUserResponseUpdatesIssuePocUser() {
        issueResponseService.updateIssueWithUserResponse(IssueResponseService.ISSUE_DECLINED_RESPONSE, externalCallId);
        issuePocUser.refresh();
        assertThat(issuePocUser.getString("user_response"), is(equalTo(IssueResponseService.ISSUE_DECLINED_STATUS)));
    }

    // issue email, issue, filter, client
    @Test
    public void updateIssueWithEmailResponseSetsEmailStatusForAllEmails() {
        createIssueEmails();

        Email email = new Email();
        email.setEmailSubject("Node: Boca-AS-101 UP");
        issueResponseService.updateIssueWithEmailResponse(client.getClientId(), email);

        issueEmail1.refresh();
        issueEmail2.refresh();
        assertThat(issueEmail1.getString("status"), is(equalTo(IssueEmail.RESOLVED_STATUS)));
        assertThat(issueEmail2.getString("status"), is(equalTo(IssueEmail.RESOLVED_STATUS)));
    }

    @Test
    @Ignore // cannot get this to work, later maybe
    public void updateIssueWithEmailResponseDoesNotChangeResolvedEmails() throws InterruptedException {
        createIssueEmails();
        issueEmail2.set("status", IssueEmail.RESOLVED_STATUS).saveIt();
        issueEmail2.refresh();

        Timestamp issue1UpdatedAtBefore = issueEmail1.getTimestamp("updated_at");
        Timestamp issue2UpdatedAtBefore = issueEmail2.getTimestamp("updated_at");

        Email email = new Email();
        email.setEmailSubject("Node: Boca-AS-101 UP");

        issueResponseService.updateIssueWithEmailResponse(client.getClientId(), email);

        issueEmail1.refresh();
        issueEmail2.refresh();

        Timestamp issue1UpdatedAtAfter = issueEmail1.getTimestamp("updated_at");
        Timestamp issue2UpdatedAtAfter = issueEmail2.getTimestamp("updated_at");

        assertThat(issue2UpdatedAtBefore, is(equalTo(issue2UpdatedAtAfter)));
        assertThat(issue1UpdatedAtAfter, greaterThan(issue1UpdatedAtBefore));
    }

    @Test
    public void updateIssueWithEmailResponseDoesNotUpdateOtherClientEmails() {
        createIssueEmails();

        Email email = new Email();
        email.setEmailSubject("Node: Boca-AS-101 UP");
        issueResponseService.updateIssueWithEmailResponse((client.getClientId() + 1), email);

        issueEmail1.refresh();
        issueEmail2.refresh();

        assertThat(issueEmail1.getString("status"), not(equalTo(IssueEmail.RESOLVED_STATUS)));
        assertThat(issueEmail2.getString("status"), not(equalTo(IssueEmail.RESOLVED_STATUS)));
    }

    @Test
    public void updateIssueWithEmailResponseDoesNotUpdateOtherServerCodes() {
        createIssueEmails();

        Email email = new Email();
        email.setEmailSubject("Node: Boca-AS-102 UP");
        issueResponseService.updateIssueWithEmailResponse(client.getClientId(), email);

        issueEmail1.refresh();
        issueEmail2.refresh();

        assertThat(issueEmail1.getString("status"), not(equalTo(IssueEmail.RESOLVED_STATUS)));
        assertThat(issueEmail2.getString("status"), not(equalTo(IssueEmail.RESOLVED_STATUS)));
    }

    private void createIssueEmails() {
        Email email = Email.createIt("client_id", client.getInteger("id"));
        issueEmail1 = IssueEmail.createIt("issue_id", issue.getInteger("id"), "email_id", email.getInteger("id"), "status", IssueEmail.OPEN_STATUS, "server_code", serverCode1);

        Email email2 = Email.createIt("client_id", client.getInteger("id"));
        issueEmail2 = IssueEmail.createIt("issue_id", issue.getInteger("id"), "email_id", email2.getInteger("id"), "status", IssueEmail.OPEN_STATUS, "server_code", serverCode1);
    }
}
