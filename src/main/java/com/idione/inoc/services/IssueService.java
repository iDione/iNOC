package com.idione.inoc.services;

import org.springframework.stereotype.Service;

import com.idione.inoc.models.Email;
import com.idione.inoc.models.Issue;
import com.idione.inoc.models.IssueEmail;
import com.idione.inoc.models.IssueStatusHistory;

@Service
public class IssueService {

    public Issue getOpenIssueWithSameFilter(int filterId) {
        return Issue.findFirst("filter_id = ? and status = ?", filterId, Issue.ISSUE_CREATED_STATUS);
    }
    
    public void updateIssueStatus(Issue issue, String status) {
        Issue.update("status", status);
        IssueStatusHistory.createIt("issue_id", issue.getInteger("id"), "status", status);
    }
    
    public Issue createIssue(int emailId, int filterId) {
        Issue issue = Issue.createIt("email_id", emailId, "filter_id", filterId, "status", Issue.ISSUE_CREATED_STATUS);
        IssueEmail.createIt("issue_id", issue.getInteger("id"), "email_id", emailId);
        IssueStatusHistory.createIt("issue_id", issue.getInteger("id"), "status", Issue.ISSUE_CREATED_STATUS);
        return issue;
    }
    
    public void addIssueEmail(Issue issue, Email email) {
        IssueEmail.createIt("issue_id", issue.getInteger("id"), "email_id", email.getInteger("id"));
    }
}
