package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

@BelongsToParents({
    @BelongsTo(parent = Filter.class, foreignKeyName = "filter_id"),
    @BelongsTo(parent = PocUser.class, foreignKeyName = "poc_user_id")
})

public class FilterPocUser extends Model {

}
