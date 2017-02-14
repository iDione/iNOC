package com.idione.inoc.models;

import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({ @BelongsTo(parent = MailingGroup.class, foreignKeyName = "assigned_mailing_group_id"),
                    @BelongsTo(parent = MailingGroup.class, foreignKeyName = "unassigned_mailing_group_id"),
                    @BelongsTo(parent = Client.class, foreignKeyName = "client_id") })

@Table("filters")
public class Filter extends Model {
    public List<PocUser> getUsers() {
        return PocUser.findBySQL("SELECT pu.* FROM filter_poc_users fpu INNER JOIN poc_users pu ON fpu.poc_user_id = pu.id WHERE fpu.filter_id = ? ORDER BY pu.id ASC", getId());
    }

    public List<FilterKeyword> getKeywords() {
        return getAll(FilterKeyword.class);
    }

    public MailingGroup getAssignedMailingGroup() {
        return MailingGroup.findFirst("id = ?", get("assigned_mailing_group_id"));
    }

    public MailingGroup getUnassignedMailingGroup() {
        return MailingGroup.findFirst("id = ?", get("unassigned_mailing_group_id"));
    }
}
