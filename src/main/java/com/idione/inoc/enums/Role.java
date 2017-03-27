package com.idione.inoc.enums;

public enum Role {
    ADMIN, SUPER;

    public static String defaultRole(){
       return "USER";
    }
}
