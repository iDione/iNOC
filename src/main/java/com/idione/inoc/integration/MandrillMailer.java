package com.idione.inoc.integration;


import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;

/*
 * https://github.com/rschreijer/lutung
 */
@Service
public class MandrillMailer {

    private static String API_KEY = "6739bec65499ecf772ddcc119ed69320-us15";
    private MandrillApi mandrillApi;
    
    public MandrillMailer() {
        mandrillApi = new MandrillApi(API_KEY);
    }

    public String[] sendMail(String from, String[] tos, String subject, String messageText) throws MandrillApiError, IOException {
        MandrillMessage message = new MandrillMessage();
        message.setSubject(subject);
        message.setHtml(messageText);
        message.setAutoText(true);
        message.setFromEmail(from);
        ArrayList<Recipient> recipients = new ArrayList<Recipient>();
        for(String to : tos) {
            Recipient recipient = new Recipient();
            recipient.setEmail(to);
            recipients.add(recipient);    
        }
        message.setTo(recipients);
        message.setPreserveRecipients(true);
        MandrillMessageStatus[] messageStatusReports = mandrillApi.messages().send(message, false);        
        return processMessageStatus(messageStatusReports);
    }

    private String[] processMessageStatus(MandrillMessageStatus[] messageStatusReports) {
        String [] emailStatuses = new String[messageStatusReports.length];
        int i = 0;
        for(MandrillMessageStatus mms : messageStatusReports) {
            emailStatuses[i] = mms.getStatus();
            i++;
        }
        return emailStatuses;
    }
}