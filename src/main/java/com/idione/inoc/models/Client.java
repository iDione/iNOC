package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

@Table("clients")
public class Client extends Model {

    public int getClientId() {
        return getInteger("id");
    }
}
