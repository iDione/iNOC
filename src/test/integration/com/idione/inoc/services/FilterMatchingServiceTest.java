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
import com.idione.inoc.models.Issue;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.services.FilterMatchingService;
import com.idione.inoc.services.IssueAssignmentService;
import com.idione.inoc.services.KeywordMatcher;
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
        mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"), "name", "A Mailing Group");
        filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", 1, "mailing_group_id", mailingGroup.getInteger("id"));
    }

    @Test
    public void itDoesNotAssignIssueIfNoMatchingFilterFound(@Mocked KeywordMatcher keywordMatcher, @Mocked IssueAssignmentService issueAssignmentService) throws Exception {
        new Expectations() {
            {
                keywordMatcher.emailMatchesFilter(emailText, client.getInteger("id"));
                result = null;
            }
        };
        filterMatchingService = new FilterMatchingService(keywordMatcher, issueAssignmentService);
        filterMatchingService.matchFiltersForEmail(email.getInteger("id"), client.getInteger("id"), emailText);
        new Verifications() {
            {
                issueAssignmentService.assignIssueToPOCUser((Issue) any);
                times = 0;
            }
        };
    }

    @Test
    public void itCreatesAnIssueIfMatchingFilterFound(@Mocked KeywordMatcher keywordMatcher, @Mocked IssueAssignmentService issueAssignmentService) throws Exception {
        int beforeIssueCount = Issue.findAll().size();
        new Expectations() {
            {
                keywordMatcher.emailMatchesFilter(emailText, client.getInteger("id"));
                result = filter;
            }
        };
        filterMatchingService = new FilterMatchingService(keywordMatcher, issueAssignmentService);
        filterMatchingService.matchFiltersForEmail(email.getInteger("id"), client.getInteger("id"), emailText);
        int afterIssueCount = Issue.findAll().size();
        assertThat(afterIssueCount, is(equalTo(beforeIssueCount + 1)));
    }

    @Test
    public void itCreatesAnIssueWithEmailIdAndFilterId(@Mocked KeywordMatcher keywordMatcher, @Mocked IssueAssignmentService issueAssignmentService) throws Exception {
        new Expectations() {
            {
                keywordMatcher.emailMatchesFilter(emailText, client.getInteger("id"));
                result = filter;
            }
        };
        filterMatchingService = new FilterMatchingService(keywordMatcher, issueAssignmentService);
        filterMatchingService.matchFiltersForEmail(email.getInteger("id"), client.getInteger("id"), emailText);
        Issue issueCreated = (Issue) Issue.where("filter_id = ?", filter.getInteger("id")).get(0);
        assertThat(issueCreated.getInteger("email_id"), is(equalTo(email.getInteger("id"))));
    }

    @Test
    public void itCallsTheIssueAssignmentServiceForIssueAssignment(@Mocked KeywordMatcher keywordMatcher, @Mocked IssueAssignmentService issueAssignmentService) throws Exception {
        new Expectations() {
            {
                keywordMatcher.emailMatchesFilter(emailText, client.getInteger("id"));
                result = filter;
            }
        };
        filterMatchingService = new FilterMatchingService(keywordMatcher, issueAssignmentService);
        filterMatchingService.matchFiltersForEmail(email.getInteger("id"), client.getInteger("id"), emailText);
        Issue issueCreated = (Issue) Issue.where("filter_id = ? and email_id = ?", filter.getInteger("id"), email.getInteger("id")).get(0);
        // TODO do an exact match to issueCreated
        new Verifications() {
            {
                issueAssignmentService.assignIssueToPOCUser((Issue) any);
                times = 1;
            }
        };
    }
}
