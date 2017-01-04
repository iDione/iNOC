package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
    @BelongsTo(parent = Client.class, foreignKeyName = "client_id")
})

@Table("emails")
public class Email extends Model {

}
