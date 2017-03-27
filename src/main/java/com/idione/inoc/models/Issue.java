package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;

@BelongsToParents({ @BelongsTo(parent = Filter.class, foreignKeyName = "filter_id") })

@Table("issues")
public class Issue extends Model {

    public static final String ISSUE_CREATED_STATUS = "created";
    public static final String ISSUE_ASSIGNED_STATUS = "assigned";
    public static final String ISSUE_UNASSIGNED_STATUS = "unassigned";

    public Filter filter() {
        return this.parent(Filter.class);
    }
}
