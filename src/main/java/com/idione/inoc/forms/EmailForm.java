package com.idione.inoc.forms;

public class EmailForm {

    private String emailId;
    private String emailText;

    public EmailForm() {
    }

    public EmailForm(String emailId, String emailText) {
        this.emailId = emailId;
        this.emailText = emailText;
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
}
