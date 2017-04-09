package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({ @BelongsTo(parent = Issue.class, foreignKeyName = "issue_id"),
        @BelongsTo(parent = Email.class, foreignKeyName = "email_id") })

@Table("issue_emails")
public class IssueEmail extends Model {

    public static final String OPEN_STATUS = "open";
    public static final String RESOLVED_STATUS = "resolved";

    public Email email() {
        return parent(Email.class);
    }

    public Issue issue() {
        return parent(Issue.class);
    }
}
