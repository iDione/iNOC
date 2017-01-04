package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
    @BelongsTo(parent = MailingGroup.class, foreignKeyName = "mailing_group_id"),
    @BelongsTo(parent = PocUser.class, foreignKeyName = "poc_user_id")
})

@Table("mailing_group_poc_users")
public class MailingGroupPocUser extends Model {

}
