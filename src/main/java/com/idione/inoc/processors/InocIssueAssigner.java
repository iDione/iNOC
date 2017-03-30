package com.idione.inoc.processors;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idione.inoc.models.Issue;
import com.idione.inoc.services.IssueAssignmentService;
import com.idione.inoc.services.IssueService;

@Service
public class InocIssueAssigner {

    private IssueService issueService;
    private IssueAssignmentService issueAssignmentService;

    public InocIssueAssigner(IssueService issueService, IssueAssignmentService issueAssignmentService) {
        this.issueService = issueService;
        this.issueAssignmentService = issueAssignmentService;
    }

    public void run() {
        List<Issue> assignableIssues = issueService.getAssignableIssues();
        for (Issue issue : assignableIssues) {
            issueAssignmentService.assignIssueToPOCUser(issue);
        }
    }
}
