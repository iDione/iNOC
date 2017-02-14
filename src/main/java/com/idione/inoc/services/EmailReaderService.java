package com.idione.inoc.services;

import org.springframework.stereotype.Service;

import com.idione.inoc.forms.EmailForm;
import com.idione.inoc.models.Email;

@Service
public class EmailReaderService {

    private FilterMatchingService filterMatchingService;

    public EmailReaderService(FilterMatchingService filterMatchingService) {
        this.filterMatchingService = filterMatchingService;
    }

    public void processEmail(int clientId, EmailForm emailForm) {
        Email email = Email.createIt("client_id", clientId, "external_email_id", emailForm.getEmailId());
        email.setEmailSubject(emailForm.getEmailSubject());
        email.setEmailText(emailForm.getEmailText());
        filterMatchingService.matchFiltersForEmail(clientId, email);
    }
}
