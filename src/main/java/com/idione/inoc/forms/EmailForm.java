package com.idione.inoc.forms;

public class EmailForm {

    private String emailId;
    private String emailText;
    private String emailSubject;

    public EmailForm() {
    }

    public EmailForm(String emailId, String emailText, String emailSubject) {
        this.emailId = emailId;
        this.emailText = emailText;
        this.emailSubject = emailSubject;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getEmailText() {
        return emailText;
    }

    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }
}
