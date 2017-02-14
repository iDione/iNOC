package com.idione.inoc.services;

import org.springframework.stereotype.Service;

import com.idione.inoc.models.Email;
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

    public void matchFiltersForEmail(int clientId, Email email) {
        Filter matchingFilter = keywordMatcher.emailMatchesFilter(email.getEmailText(), clientId);
        if (matchingFilter != null) {
            Issue issue = Issue.createIt("email_id", email.getEmailId(), "filter_id", matchingFilter.getInteger("id"));
            issueAssignmentService.assignIssueToPOCUser(issue, email);
        }
    }
}
