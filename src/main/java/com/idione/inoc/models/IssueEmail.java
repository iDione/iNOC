package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({ @BelongsTo(parent = Issue.class, foreignKeyName = "issue_id"),
        @BelongsTo(parent = Email.class, foreignKeyName = "email_id") })

@Table("issue_emails")
public class IssueEmail extends Model {

}
