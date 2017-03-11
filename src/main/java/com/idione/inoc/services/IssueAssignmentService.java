package com.idione.inoc.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.idione.inoc.models.Email;
import com.idione.inoc.models.Issue;
import com.idione.inoc.models.IssuePocUser;
import com.idione.inoc.models.PocUser;

@Service
public class IssueAssignmentService {

    private TelephoneService telephoneService;
    private EmailSenderService emailSenderService;
    private TemplateEngine emailTemplateEngine;

    private final String UNASSIGNED_EMAIL_TEMPLATE_NAME = "unassignedIssueEmailTemplate";
    private final String ASSIGNED_EMAIL_TEMPLATE_NAME = "assignedIssueEmailTemplate";

    public IssueAssignmentService(TelephoneService telephoneService, EmailSenderService emailSenderService, TemplateEngine emailTemplateEngine) {
        this.telephoneService = telephoneService;
        this.emailSenderService = emailSenderService;
        this.emailTemplateEngine = emailTemplateEngine;
    }

    public boolean assignIssueToPOCUser(Issue issue, Email email) {
        List<PocUser> pocUsers = issue.filter().getUsers();
        for (PocUser pocUser : pocUsers) {
            IssuePocUser issuePocUser = IssuePocUser.createIt("issue_id", issue.getId(), "poc_user_id", pocUser.getId());
            String userResponse = telephoneService.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), pocUser.getString("phone_number"), issue.filter().getInteger("retries"));
            if (userResponse.compareTo("accepted") == 0) {
                sendIssueAssignedEmail(issue, email, pocUser);
                return true;
            }
        }
        sendIssueUnassignedEmail(issue, email);
        return false;
    }

    public void sendIssueUnassignedEmail(Issue issue, Email email) {
        final Context ctx = new Context();
        ctx.setVariable("ADMIN_MESSAGE", issue.filter().getString("unassigned_email_template"));
        ctx.setVariable("ISSUE_SUBJECT", email.getEmailSubject());
        ctx.setVariable("ISSUE_EMAIL", email.getEmailText());
        String message = this.emailTemplateEngine.process(UNASSIGNED_EMAIL_TEMPLATE_NAME, ctx);

        emailSenderService.sendMailViaGmail(EmailSenderService.INOC_EMAIL_ADDRESS,
                                    issue.filter().getUnassignedMailingGroup().emails(),
                                    "Issue Not Assigned To Anyone",
                                    message);
    }

    public void sendIssueAssignedEmail(Issue issue, Email email, PocUser pocUser) {
        final Context ctx = new Context();
        ctx.setVariable("ADMIN_MESSAGE", issue.filter().getString("assigned_email_template"));
        ctx.setVariable("ISSUE_SUBJECT", email.getEmailSubject());
        ctx.setVariable("ISSUE_EMAIL", email.getEmailText());
        String message = this.emailTemplateEngine.process(ASSIGNED_EMAIL_TEMPLATE_NAME, ctx);

        message = message.replaceAll("ISSUE_ASSIGNED_USER_NAME", pocUser.getFullName());

        emailSenderService.sendMailViaGmail(EmailSenderService.INOC_EMAIL_ADDRESS,
                                    issue.filter().getAssignedMailingGroup().emails(),
                                    "Issue Assigned",
                                    message);
    }
}
