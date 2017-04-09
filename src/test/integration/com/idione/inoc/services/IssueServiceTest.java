package com.idione.inoc.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.javalite.activejdbc.Base;
import org.junit.Before;
import org.junit.Test;

import com.idione.inoc.models.Client;
import com.idione.inoc.models.Email;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.Issue;
import com.idione.inoc.models.IssueEmail;
import com.idione.inoc.models.IssueStatusHistory;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.test.AbstractIntegrationTest;

public class IssueServiceTest extends AbstractIntegrationTest {

    Client client;
    Filter filter;
    Email email;
    Issue issue;
    IssueService issueService;

    @Before
    public void createFilter() {
        issueService = new IssueService();
        client = Client.createIt("name", "Mickey Mouse Club House");
        MailingGroup mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"));
        email = Email.createIt("client_id", client.getInteger("id"));
        filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 2, "retries", 1, "assigned_mailing_group_id", mailingGroup.getInteger("id"), "unassigned_mailing_group_id", mailingGroup.getInteger("id"), "unassigned_email_template", "un assigned email template", "assigned_email_template", "assigned email template", "hold_issue_creations_for", 0);
        issue = issueService.createIssue(email, filter);
    }

    @Test
    public void itCreatesAnIssueWithEmailIdAndFilterId() throws Exception {
        Issue issueCreated = (Issue) Issue.where("filter_id = ?", filter.getInteger("id")).get(0);
        assertThat(issueCreated.getInteger("email_id"), is(equalTo(email.getInteger("id"))));
    }

    @Test
    public void createIssueCreatesIssueWithCreatedStatus() {
        assertThat(issue.getString("status"), is(equalTo(Issue.ISSUE_CREATED_STATUS)));
    }

    @Test
    public void createIssueCreatesStatusHistoryWithCreatedStatus() {
        IssueStatusHistory issueStatusHistory = (IssueStatusHistory) IssueStatusHistory.where("issue_id = ?", issue.getInteger("id")).get(0);
        assertThat(issueStatusHistory.getString("status"), is(equalTo(Issue.ISSUE_CREATED_STATUS)));
    }

    @Test
    public void createIssueCreatesIssueEmail() {
        IssueEmail issueEmail = (IssueEmail) IssueEmail.where("issue_id = ?", issue.getInteger("id")).get(0);
        assertThat(issueEmail.getInteger("email_id"), is(equalTo(issue.getInteger("email_id"))));
    }

    @Test
    public void addEmailAddsAnIssueEmailRecord() {
        Email email2 = Email.createIt("client_id", client.getInteger("id"));
        issueService.addIssueEmail(issue, email2);
        List<IssueEmail> issueEmails = IssueEmail.where("issue_id = ?", issue.getInteger("id"));
        assertThat(issueEmails.size(), is(equalTo(2)));
        assertThat(issueEmails.get(1).getInteger("email_id"), is(equalTo(email2.getInteger("id"))));
    }

    @Test
    public void addEmailDoesNotChangeIssueEmailField() {
        Email email2 = Email.createIt("client_id", client.getInteger("id"));
        issueService.addIssueEmail(issue, email2);
        assertThat(issue.getInteger("email_id"), is(equalTo(email.getInteger("id"))));
    }

    @Test
    public void getAssignableIssuesOnlyGetsOpenIssues() throws InterruptedException {
        Base.exec("update issues set created_at = current_timestamp + INTERVAL'-1 seconds' where id = ?", issue.getInteger("id"));
        assertThat(issueService.getAssignableIssues().size(), is(equalTo(1)));
        issueService.updateIssueStatus(issue, Issue.ISSUE_ASSIGNED_STATUS);
        assertThat(issueService.getAssignableIssues().size(), is(equalTo(0)));
    }

    @Test
    public void getAssignableIssuesOnlyGetsIssuesAfterFilterTimeInterval() throws InterruptedException {
        Base.exec("update issues set created_at = current_timestamp + INTERVAL '-1 seconds' where id = ?", issue.getInteger("id"));
        assertThat(issueService.getAssignableIssues().size(), is(equalTo(1)));
        Base.exec("update issues set created_at = current_timestamp + INTERVAL '1 second' where id = ?", issue.getInteger("id"));
        assertThat(issueService.getAssignableIssues().size(), is(equalTo(0)));
    }
}
