package com.idione.inoc.services;

import org.springframework.stereotype.Service;

import com.idione.inoc.models.Email;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.Issue;

@Service
public class IssueStatusMonitoringService {

    private KeywordMatcher keywordMatcher;
    private IssueAssignmentService issueAssignmentService;

    public IssueStatusMonitoringService(KeywordMatcher keywordMatcher, IssueAssignmentService issueAssignmentService) {
        this.keywordMatcher = keywordMatcher;
        this.issueAssignmentService = issueAssignmentService;
    }


}
