package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({ @BelongsTo(parent = Filter.class, foreignKeyName = "filter_id"),
        @BelongsTo(parent = PocUser.class, foreignKeyName = "poc_user_id") })

@Table("filter_poc_users")
public class FilterPocUser extends Model {

}
