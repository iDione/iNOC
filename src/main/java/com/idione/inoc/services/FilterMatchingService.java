package com.idione.inoc.services;

import org.springframework.stereotype.Service;

import com.idione.inoc.models.Filter;
import com.idione.inoc.models.Issue;

@Service
public class FilterMatchingService {

    private KeywordMatcher keywordMatcher;
    private IssueAssignmentService issueAssignmentService;

    public FilterMatchingService(KeywordMatcher keywordMatcher, IssueAssignmentService issueAssignmentService) {
        this.keywordMatcher = keywordMatcher;
        this.issueAssignmentService = issueAssignmentService;
    }

    public void matchFiltersForEmail(int emailId, int clientId, String emailText) {
        Filter matchingFilter = keywordMatcher.emailMatchesFilter(emailText, clientId);
        if (matchingFilter != null) {
            Issue issue = Issue.createIt("email_id", emailId, "filter_id", matchingFilter.getInteger("id"));
            issueAssignmentService.assignIssueToPOCUser(issue);
        }
    }
}
