package com.idione.inoc.models;

import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({ @BelongsTo(parent = MailingGroup.class, foreignKeyName = "mailing_group_id"),
                    @BelongsTo(parent = Client.class, foreignKeyName = "client_id") })

@Table("filters")
public class Filter extends Model {
    public List<PocUser> getAssignableUsers() {
        return PocUser.findBySQL("SELECT pu.* FROM filter_poc_users fpu INNER JOIN poc_users pu ON fpu.poc_user_id = pu.id WHERE fpu.filter_id = ? ORDER BY pu.id ASC", getId());
    }

    public MailingGroup getMailingGroup() {
        return this.parent(MailingGroup.class);
    }
}
