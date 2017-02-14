package com.idione.inoc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idione.inoc.models.Email;
import com.idione.inoc.models.Issue;
import com.idione.inoc.models.IssuePocUser;
import com.idione.inoc.models.PocUser;

@Service
public class IssueAssignmentService {

    private TelephoneService telephoneService;
    private EmailSenderService emailSenderService;

    public IssueAssignmentService(TelephoneService telephoneService, EmailSenderService emailSenderService) {
        this.telephoneService = telephoneService;
        this.emailSenderService = emailSenderService;
    }

    public boolean assignIssueToPOCUser(Issue issue, Email email) {
        List<PocUser> pocUsers = issue.filter().getUsers();
        for (PocUser pocUser : pocUsers) {
            IssuePocUser issuePocUser = IssuePocUser.createIt("issue_id", issue.getId(), "poc_user_id", pocUser.getId());
            String userResponse = telephoneService.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), pocUser.getString("phone_number"), issue.filter().getInteger("retries"));
            if (userResponse.compareTo("accepted") == 0) {
                sendIssueAssignedEmail(issue, email);
                return true;
            }
        }
        sendIssueUnassignedEmail(issue, email);
        return false;
    }

    private void sendIssueUnassignedEmail(Issue issue, Email email) {
        emailSenderService.sendMail(EmailSenderService.INOC_EMAIL_ADDRESS,
                                    issue.filter().getUnassignedMailingGroup().emails(),
                                    "Issue Not Assigned To Anyone",
                                    issue.filter().getString("unassigned_email_template"));
    }

    private void sendIssueAssignedEmail(Issue issue, Email email) {
        emailSenderService.sendMail(EmailSenderService.INOC_EMAIL_ADDRESS,
                                    issue.filter().getAssignedMailingGroup().emails(),
                                    "Issue Assigned",
                                    issue.filter().getString("assigned_email_template"));
    }
}
