package com.idione.inoc.test;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.BeforeClass;

import com.idione.inoc.controllers.ApplicationController;
import com.idione.inoc.models.PocUser;

import mockit.Mock;
import mockit.MockUp;

public abstract class AbstractIntegrationTest extends DBSpec {

    protected static PocUser currentPosUser;
    protected static int pocUserId = 1011;
    protected static int clientId = 1;

    @BeforeClass
    public static void setupUser()
    {
       new MockUp<ApplicationController>() {
           @Mock PocUser getCurrentUser() {
               return getLoggedInUser();
           }
       };
    }

    private static PocUser getLoggedInUser(){
        currentPosUser = new PocUser();
        currentPosUser.set("id", pocUserId);
        currentPosUser.set("client_id", clientId);
        return currentPosUser;
    }
}
