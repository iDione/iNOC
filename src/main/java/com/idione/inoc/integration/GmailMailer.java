package com.idione.inoc.integration;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class GmailMailer {
    private Properties properties;
    private Session session;
    private final String from = "inocissues@gmail.com";
    private final String password = "Canyouhearmenow.";

    public GmailMailer() {
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
    }

    public void send(String[] tos, String subject, String messageText) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        for (String to : tos) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        }
        message.setSubject(subject);
        message.setContent(messageText, "text/html");
        Transport.send(message);
    }
}