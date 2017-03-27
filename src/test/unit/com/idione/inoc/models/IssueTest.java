package com.idione.inoc.models;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.idione.inoc.test.AbstractIntegrationTest;

public class IssueTest extends AbstractIntegrationTest {

    Client client;
    Filter filter;
    Email email;

    @Before
    public void createFilter() {
        client = Client.createIt("name", "Mickey Mouse Club House");
        MailingGroup mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"));
        email = Email.createIt("client_id", client.getInteger("id"));
        filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 2, "retries", 1, "assigned_mailing_group_id", mailingGroup.getInteger("id"), "unassigned_mailing_group_id", mailingGroup.getInteger("id"), "unassigned_email_template", "un assigned email template", "assigned_email_template", "assigned email template");
    }
    
    @Test
    public void createIssueCreatesIssueWithCreatedStatus() {
        Issue issue = Issue.createIssue(email.getInteger("id"), filter.getInteger("id"));
        assertThat(issue.getString("status"), is(equalTo(Issue.ISSUE_CREATED_STATUS)));
    }
    
    @Test
    public void createIssueCreatesStatusHistoryWithCreatedStatus() {
        Issue issue = Issue.createIssue(email.getInteger("id"), filter.getInteger("id"));
        IssueStatusHistory issueStatusHistory = (IssueStatusHistory) IssueStatusHistory.where("issue_id = ?", issue.getInteger("id")).get(0);
        assertThat(issueStatusHistory.getString("status"), is(equalTo(Issue.ISSUE_CREATED_STATUS)));
    }
    
    @Test
    public void createIssueCreatesIssueEmail() {
        Issue issue = Issue.createIssue(email.getInteger("id"), filter.getInteger("id"));
        IssueEmail issueEmail = (IssueEmail) IssueEmail.where("issue_id = ?", issue.getInteger("id")).get(0);
        assertThat(issueEmail.getInteger("email_id"), is(equalTo(issue.getInteger("email_id"))));
    }
    
    @Test
    public void addEmailAddsAnIssueEmailRecord() {
        Issue issue = Issue.createIssue(email.getInteger("id"), filter.getInteger("id"));
        Email email2 = Email.createIt("client_id", client.getInteger("id"));
        issue.addEmail(email2);
        List<IssueEmail> issueEmails = IssueEmail.where("issue_id = ?", issue.getInteger("id"));
        assertThat(issueEmails.size(), is(equalTo(2)));
        assertThat(issueEmails.get(1).getInteger("email_id"), is(equalTo(email2.getInteger("id"))));
    }
    
    @Test
    public void addEmailDoesNotChangeIssueEmailField() {
        Issue issue = Issue.createIssue(email.getInteger("id"), filter.getInteger("id"));
        Email email2 = Email.createIt("client_id", client.getInteger("id"));
        issue.addEmail(email2);
        assertThat(issue.getInteger("email_id"), is(equalTo(email.getInteger("id"))));
    }
}
