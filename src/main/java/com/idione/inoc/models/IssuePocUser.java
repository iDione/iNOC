package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({ @BelongsTo(parent = Issue.class, foreignKeyName = "issue_id"),
                    @BelongsTo(parent = PocUser.class, foreignKeyName = "poc_user_id") })

@Table("issue_poc_users")
public class IssuePocUser extends Model {

}
