package com.idione.inoc.services;

import org.springframework.stereotype.Service;

import com.idione.inoc.models.Email;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.Issue;

@Service
public class FilterMatchingService {

    private KeywordMatcher keywordMatcher;
    private IssueService issueService;

    public FilterMatchingService(KeywordMatcher keywordMatcher, IssueService issueService) {
        this.keywordMatcher = keywordMatcher;
        this.issueService = issueService;
    }

    public void matchFiltersForEmail(int clientId, Email email) {
        Filter matchingFilter = keywordMatcher.emailMatchesFilter(email.getEmailText(), clientId);
        if (matchingFilter != null) {
            Issue openIssue = issueService.getOpenIssueWithSameFilter(matchingFilter.getInteger("id"));
            if (openIssue == null) {
                issueService.createIssue(email, matchingFilter);
            } else {
                issueService.addIssueEmail(openIssue, email);
            }
        }
    }
}
