package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({
    @BelongsTo(parent = Filter.class, foreignKeyName = "filter_id")
})

@Table("issues")
public class Issue extends Model {
	public Filter filter(){
        return this.parent(Filter.class);
    }
}
