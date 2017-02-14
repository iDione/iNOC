package com.idione.inoc.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import com.idione.inoc.forms.EmailForm;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Flags.Flag;

/*
 * Code referenced from http://www.oracle.com/technetwork/java/javamail/faq/index.html#mainbody
 */
public class EmailReader {

    private String PROTOCOL = "imaps";
    private Store store;
    private Folder inbox;

    public EmailReader(String host, String userName, String password) throws MessagingException {
        Properties properties = System.getProperties();
        properties.setProperty("mail.store.protocol", PROTOCOL);

        Session session = Session.getDefaultInstance(properties, null);
        store = session.getStore("imaps");
        store.connect(host, userName, password);
    }

    public List<EmailForm> processInbox() throws MessagingException, IOException {
        List<EmailForm> newEmails = new ArrayList<EmailForm>();
        inbox = store.getFolder("Inbox");
        inbox.open(Folder.READ_WRITE);
        if (inbox.getUnreadMessageCount() > 0) {
            Message[] messages = getMessages();
            newEmails = process(messages);
        }
        inbox.close(true);
        store.close();
        return newEmails;
    }

    public Message[] getMessages() throws MessagingException {
        Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));

        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        fp.add(FetchProfile.Item.CONTENT_INFO);
        inbox.fetch(messages, fp);

        return messages;
    }

    public List<EmailForm> process(Message[] messages) throws MessagingException, IOException {
        List<EmailForm> newEmails = new ArrayList<EmailForm>();
        for (Message message : messages) {
            newEmails.add(new EmailForm(message.getHeader("Message-ID")[0], getText(message), message.getSubject()));
        }
        return newEmails;
    }

    private String getText(Part p) throws MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            return s;
        } else if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }
        return null;
    }

    public void printEnvelope(Message message) throws Exception {
        String[] to = getTo(message);
        String[] from = getFrom(message);
        String subject = message.getSubject();
        Date receivedDate = message.getReceivedDate();
    }

    private String[] getFrom(Message message) throws MessagingException {
        Address[] a;
        if ((a = message.getFrom()) != null) {
            return getEmailAddresses(a);
        } else {
            return null;
        }
    }

    private String[] getTo(Message message) throws MessagingException {
        Address[] a;
        if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
            return getEmailAddresses(a);
        } else {
            return null;
        }
    }

    private String[] getEmailAddresses(Address[] a) {
        String[] from = new String[a.length];
        for (int j = 0; j < a.length; j++) {
            from[j] = a[j].toString();
        }
        return from;
    }
}
