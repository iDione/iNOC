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
        //TODO figure out why data is not deleting
        Base.exec("delete from telephone_calls; delete from issue_poc_users; delete from issues; delete from filter_poc_users; delete from filter_keywords; delete from filters; delete from mailing_group_poc_users; delete from mailing_groups; delete from poc_users; delete from clients; delete from emails;");
        Base.close();
    }
}
