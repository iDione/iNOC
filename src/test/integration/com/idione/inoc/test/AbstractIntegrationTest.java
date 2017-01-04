package com.idione.inoc.test;

import org.javalite.activejdbc.Base;
import org.junit.After;
import org.junit.Before;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration
public abstract class AbstractIntegrationTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Before
    public void setup() {
        Base.open("org.postgresql.Driver", "jdbc:postgresql://localhost/inoc_test", "adarsh", "adarsh");
    }

    @After
    public void closeConnection() {
        Base.close();
    }
}
