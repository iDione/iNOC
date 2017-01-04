package com.idione.inoc.models;

import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
    @BelongsTo(parent = Client.class, foreignKeyName = "client_id")
})

@Table("mailing_groups")
public class MailingGroup extends Model {
	public List<PocUser> getUsers(){
        return PocUser.findBySQL("SELECT pu.* FROM mailing_group_poc_users mgpu INNER JOIN poc_users pu ON mgpu.poc_user_id = pu.id WHERE mgpu.mailing_group_id = ? ORDER BY pu.id ASC", getId());
    }
}
