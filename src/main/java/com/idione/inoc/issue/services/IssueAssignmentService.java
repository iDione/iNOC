package com.idione.inoc.issue.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idione.inoc.models.Issue;
import com.idione.inoc.models.IssuePocUser;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.telephone.services.TelephoneService;

@Service
public class IssueAssignmentService {

    private TelephoneService telephoneService;

    public IssueAssignmentService(TelephoneService telephoneService) {
        this.telephoneService = telephoneService;
    }

    public boolean assignIssueToPOCUser(Issue issue) {
        List<PocUser> pocUsers = issue.filter().getAssignableUsers();
        for (PocUser pocUser : pocUsers) {
            IssuePocUser issuePocUser = IssuePocUser.createIt("issue_id", issue.getId(), "poc_user_id", pocUser.getId());
            String userResponse = telephoneService.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), pocUser.getString("phone_number"), issue.filter().getInteger("retries"));
            if (userResponse.compareTo("accepted") == 0) {
                return true;
            }
        }
        // nobody accepted send email to mailing group
        MailingGroup mailingGroup = issue.filter().getMailingGroup();
        List<PocUser> mailingGroupUsers = mailingGroup.getUsers();
        sendIssueUnassignedEmail(mailingGroupUsers);
        return false;
    }

    private void sendIssueUnassignedEmail(List<PocUser> mailingGroupUsers) {
        // send email
    }
}
