package com.idione.inoc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idione.inoc.forms.EmailForm;
import com.idione.inoc.models.Email;

@Service
public class EmailReaderService {

    private FilterMatchingService filterMatchingService;
    private IssueResponseService issueResponseService;

    @Autowired
    public void setFilterMatchingService(FilterMatchingService filterMatchingService) {
        this.filterMatchingService = filterMatchingService;
    }

    @Autowired
    public void setIssueResponseService(IssueResponseService issueResponseService) {
        this.issueResponseService = issueResponseService;
    }

    public void processIssueCreationEmail(int clientId, EmailForm emailForm) {
        Email email = Email.createIt("client_id", clientId, "external_email_id", emailForm.getEmailId());
        email.setEmailSubject(emailForm.getEmailSubject());
        email.setEmailText(emailForm.getEmailText());
        filterMatchingService.matchFiltersForEmail(clientId, email);
    }

    public void processIssueStatusEmail(int clientId, EmailForm emailForm) {
        Email email = Email.createIt("client_id", clientId, "external_email_id", emailForm.getEmailId());
        email.setEmailSubject(emailForm.getEmailSubject());
        email.setEmailText(emailForm.getEmailText());
        issueResponseService.updateIssueWithEmailResponse(clientId, email);
    }
}
