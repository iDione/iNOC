package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;

@BelongsToParents({
    @BelongsTo(parent = Filter.class, foreignKeyName = "filter_id")
})

public class FilterKeyword extends Model {

}
