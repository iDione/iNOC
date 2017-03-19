package com.idione.inoc.validators;

import org.springframework.validation.BindingResult;

import com.idione.inoc.forms.MailingGroupForm;

public class MailingGroupValidator {

    public static void validate(MailingGroupForm mailingGroupForm, BindingResult bindingResult) {
        if(mailingGroupForm == null){
            return;
        }
        if(mailingGroupForm.getIncludeEmailAddresses().isEmpty() && mailingGroupForm.getPocUserIds().isEmpty()){
            bindingResult.rejectValue("includeEmailAddresses", "error.user", "Please select a POC User to email or add an email address");
        }
    }
}