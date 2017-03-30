package com.idione.inoc.models;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.idione.inoc.services.IssueService;
import com.idione.inoc.test.AbstractIntegrationTest;

public class IssueTest extends AbstractIntegrationTest {

    Client client;
    Filter filter;
    Email email;
    IssueService issueService;

    @Before
    public void setup() {
        issueService = new IssueService();
        client = Client.createIt("name", "Mickey Mouse Club House");
        MailingGroup mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"));
        email = Email.createIt("client_id", client.getInteger("id"));
        filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 2, "retries", 1, "assigned_mailing_group_id", mailingGroup.getInteger("id"), "unassigned_mailing_group_id", mailingGroup.getInteger("id"), "unassigned_email_template", "un assigned email template", "assigned_email_template", "assigned email template");
    }
    
    @Test
    public void emailsReturnsAListOfIssueEmails() {
        Issue issue = issueService.createIssue(email.getInteger("id"), filter.getInteger("id"));
        assertThat(issue.emails().size(), is(equalTo(1)));
        assertThat(issue.emails().get(0).getInteger("id"), is(equalTo(email.getInteger("id"))));
    }
}
