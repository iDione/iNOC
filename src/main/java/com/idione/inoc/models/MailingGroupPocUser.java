package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

@BelongsToParents({
    @BelongsTo(parent = MailingGroup.class, foreignKeyName = "mailing_group_id"),
    @BelongsTo(parent = PocUser.class, foreignKeyName = "poc_user_id")
})

public class MailingGroupPocUser extends Model {

}
