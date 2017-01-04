package com.idione.inoc.email.services;

import org.springframework.stereotype.Service;

import com.idione.inoc.filter.services.FilterMatchingService;
import com.idione.inoc.models.Email;

@Service
public class EmailReaderService {

    private FilterMatchingService filterMatchingService;

    public EmailReaderService(FilterMatchingService filterMatchingService) {
        this.filterMatchingService = filterMatchingService;
    }

    public void processEmail(int clientId, String emailText, String emailId) {
        Email email = Email.createIt("client_id", clientId, "external_email_id", emailId);
        filterMatchingService.matchFiltersForEmail(email.getInteger("id"), clientId, emailText);
    }
}
