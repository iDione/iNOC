package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({ @BelongsTo(parent = Client.class, foreignKeyName = "client_id") })

@Table("poc_users")
public class PocUser extends Model {

    public int getClientId() {
        return getInteger("client_id");
    }

}
