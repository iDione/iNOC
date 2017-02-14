package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({ @BelongsTo(parent = Client.class, foreignKeyName = "client_id") })

@Table("emails")
public class Email extends Model {
    private String emailSubject;
    private String emailText;

    public int getEmailId() {
        return getInteger("id");
    }
    public String getEmailSubject() {
        return emailSubject;
    }
    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }
    public String getEmailText() {
        return emailText;
    }
    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }
}
