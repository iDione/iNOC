package com.idione.inoc.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.models.Client;
import com.idione.inoc.models.Email;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.Issue;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class FilterMatchingServiceTest extends AbstractIntegrationTest {

    FilterMatchingService filterMatchingService;
    Client client;
    Email email;
    Filter filter;
    MailingGroup mailingGroup;
    private String emailText = "This is an email text.";

    @Before
    public void createFilter() {
        client = Client.createIt("name", "Mickey Mouse Club House");
        email = Email.createIt("client_id", client.getInteger("id"));
        email.setEmailText(emailText);
        mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"), "name", "A Mailing Group");
        filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", 1, "assigned_mailing_group_id", mailingGroup.getInteger("id"));
    }

    @Test
    public void itDoesNotAssignIssueIfNoMatchingFilterFound(@Mocked KeywordMatcher keywordMatcher, @Mocked IssueService issueService) throws Exception {
        new Expectations() {
            {
                keywordMatcher.emailMatchesFilter(emailText, client.getInteger("id"));
                result = null;
            }
        };
        filterMatchingService = new FilterMatchingService(keywordMatcher, issueService);
        filterMatchingService.matchFiltersForEmail(client.getInteger("id"), email);
        new Verifications() {
            {
                issueService.createIssue((Email) any, (Filter) any);
                times = 0;
            }
        };
    }

    @Test
    public void itCreatesAnIssueIfMatchingFilterFound(@Mocked KeywordMatcher keywordMatcher, @Mocked IssueService issueService) throws Exception {
        new Expectations() {
            {
                keywordMatcher.emailMatchesFilter(emailText, client.getInteger("id"));
                result = filter;
                issueService.getOpenIssueWithSameFilter(filter.getInteger("id"));
                result = null;
            }
        };
        filterMatchingService = new FilterMatchingService(keywordMatcher, issueService);
        filterMatchingService.matchFiltersForEmail(client.getInteger("id"), email);
        new Verifications() {
            {
                issueService.createIssue((Email) any, (Filter) any);
                times = 1;
            }
        };
    }

    @Test
    public void itDoesNotCreateAnIssueIfOpenIssueExists(@Mocked KeywordMatcher keywordMatcher, @Mocked IssueService issueService) throws Exception {
        Issue openIssue = new Issue();
        openIssue.setString("status", Issue.ISSUE_CREATED_STATUS);
        new Expectations() {
            {
                keywordMatcher.emailMatchesFilter(emailText, client.getInteger("id"));
                result = filter;
                issueService.getOpenIssueWithSameFilter(filter.getInteger("id"));
                result = openIssue;
            }
        };
        filterMatchingService = new FilterMatchingService(keywordMatcher, issueService);
        filterMatchingService.matchFiltersForEmail(client.getInteger("id"), email);
        new Verifications() {
            {
                issueService.createIssue((Email) any, (Filter) any);
                times = 0;
            }
        };
    }

    @Test
    public void itAddsTheEmailToTheIssueIfOpenIssueExists(@Mocked KeywordMatcher keywordMatcher, @Mocked IssueService issueService) throws Exception {
        Issue openIssue = new Issue();
        openIssue.setString("status", Issue.ISSUE_CREATED_STATUS);
        new Expectations() {
            {
                keywordMatcher.emailMatchesFilter(emailText, client.getInteger("id"));
                result = filter;
                issueService.getOpenIssueWithSameFilter(filter.getInteger("id"));
                result = openIssue;
            }
        };
        filterMatchingService = new FilterMatchingService(keywordMatcher, issueService);
        filterMatchingService.matchFiltersForEmail(client.getInteger("id"), email);
        new Verifications() {
            {
                issueService.addIssueEmail(openIssue, email);
                times = 1;
            }
        };
    }
}
