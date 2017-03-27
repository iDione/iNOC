package com.idione.inoc.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

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
    IssueService issueService;

    @Before
    public void createFilter() {
        client = Client.createIt("name", "Mickey Mouse Club House");
        MailingGroup mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"));
        email = Email.createIt("client_id", client.getInteger("id"));
        filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 2, "retries", 1, "assigned_mailing_group_id", mailingGroup.getInteger("id"), "unassigned_mailing_group_id", mailingGroup.getInteger("id"), "unassigned_email_template", "un assigned email template", "assigned_email_template", "assigned email template");
        issueService = new IssueService();
    }

    @Test
    public void itCreatesAnIssueWithEmailIdAndFilterId() throws Exception {
        issueService.createIssue(email.getInteger("id"), filter.getInteger("id"));
        Issue issueCreated = (Issue) Issue.where("filter_id = ?", filter.getInteger("id")).get(0);
        assertThat(issueCreated.getInteger("email_id"), is(equalTo(email.getInteger("id"))));
    }
    
    @Test
    public void createIssueCreatesIssueWithCreatedStatus() {
        Issue issue = issueService.createIssue(email.getInteger("id"), filter.getInteger("id"));
        assertThat(issue.getString("status"), is(equalTo(Issue.ISSUE_CREATED_STATUS)));
    }
    
    @Test
    public void createIssueCreatesStatusHistoryWithCreatedStatus() {
        Issue issue = issueService.createIssue(email.getInteger("id"), filter.getInteger("id"));
        IssueStatusHistory issueStatusHistory = (IssueStatusHistory) IssueStatusHistory.where("issue_id = ?", issue.getInteger("id")).get(0);
        assertThat(issueStatusHistory.getString("status"), is(equalTo(Issue.ISSUE_CREATED_STATUS)));
    }
    
    @Test
    public void createIssueCreatesIssueEmail() {
        Issue issue = issueService.createIssue(email.getInteger("id"), filter.getInteger("id"));
        IssueEmail issueEmail = (IssueEmail) IssueEmail.where("issue_id = ?", issue.getInteger("id")).get(0);
        assertThat(issueEmail.getInteger("email_id"), is(equalTo(issue.getInteger("email_id"))));
    }
    
    @Test
    public void addEmailAddsAnIssueEmailRecord() {
        Issue issue = issueService.createIssue(email.getInteger("id"), filter.getInteger("id"));
        Email email2 = Email.createIt("client_id", client.getInteger("id"));
        issueService.addIssueEmail(issue, email2);
        List<IssueEmail> issueEmails = IssueEmail.where("issue_id = ?", issue.getInteger("id"));
        assertThat(issueEmails.size(), is(equalTo(2)));
        assertThat(issueEmails.get(1).getInteger("email_id"), is(equalTo(email2.getInteger("id"))));
    }
    
    @Test
    public void addEmailDoesNotChangeIssueEmailField() {
        Issue issue = issueService.createIssue(email.getInteger("id"), filter.getInteger("id"));
        Email email2 = Email.createIt("client_id", client.getInteger("id"));
        issueService.addIssueEmail(issue, email2);
        assertThat(issue.getInteger("email_id"), is(equalTo(email.getInteger("id"))));
    }
}
