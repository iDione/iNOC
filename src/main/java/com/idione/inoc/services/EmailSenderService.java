package com.idione.inoc.services;


import java.io.IOException;
import java.util.ArrayList;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idione.inoc.exceptions.EmailException;
import com.idione.inoc.integration.GmailMailer;
import com.idione.inoc.integration.MandrillMailer;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;

@Service
public class EmailSenderService {
    public static final String INOC_EMAIL_ADDRESS = "inoc@idione.com";
    private MandrillMailer mandrillMailer;
    private GmailMailer gmailMailer;
    
    public static ArrayList<String> failureEmailStatuses;
    private static String REJECTED_EMAIL_STATUS = "rejected";
    private static String INVALID_EMAIL_STATUS = "invalid";
    
    static {
        failureEmailStatuses = new ArrayList<String>();
        failureEmailStatuses.add(REJECTED_EMAIL_STATUS);
        failureEmailStatuses.add(INVALID_EMAIL_STATUS);
    }

    @Autowired
    public void setMandrillMailer(MandrillMailer mandrillMailer) {
        this.mandrillMailer = mandrillMailer;
    }

    @Autowired
    public void setGmailMailer(GmailMailer gmailMailer) {
        this.gmailMailer = gmailMailer;
    }
    
    public boolean sendMailViaMandrill(String from, String[] tos, String subject, String messageText) throws EmailException {
        String[] emailStatuses;
        try {
            emailStatuses = mandrillMailer.sendMail(from, tos, subject, messageText);
            for(String emailStatus : emailStatuses){
                if(failureEmailStatuses.contains(emailStatus)) {
                    return false;
                }
            }
            return true;
        } catch (MandrillApiError | IOException e) {
            e.printStackTrace();
            throw new EmailException();
        }
    }

    public void sendMailViaGmail(String from, String[] tos, String subject, String messageText) throws EmailException {
        try {
            gmailMailer.send(tos, subject, messageText);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new EmailException();
        }
    }
}