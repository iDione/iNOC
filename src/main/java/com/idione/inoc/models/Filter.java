package com.idione.inoc.models;

import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

@BelongsToParents({
    @BelongsTo(parent = MailingGroup.class, foreignKeyName = "mailing_group_id"),
    @BelongsTo(parent = Client.class, foreignKeyName = "client_id")
})

public class Filter extends Model {
    public List<PocUser> getAssignableUsers(){
    	return getAll(PocUser.class);
    }
}
