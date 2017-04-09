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

    public String serverCode() {
        if (emailSubject != null) {
            if (emailSubject.indexOf("Down") >= 0) {
                return emailSubject.substring(6).trim();
            } else if (emailSubject.indexOf("UP") >= 0) {
                return emailSubject.substring(6, 17).trim();
            }
        }
        return "unknown";
    }

    public String serverStatus() {
        if (emailSubject != null) {
            if (emailSubject.indexOf("Down") >= 0) {
                return IssueEmail.OPEN_STATUS;
            } else if (emailSubject.indexOf("UP") >= 0) {
                return IssueEmail.RESOLVED_STATUS;
            }
        }
        return "unknown";
    }
}
