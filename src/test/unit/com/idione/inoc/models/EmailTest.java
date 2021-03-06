package com.idione.inoc.models;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.idione.inoc.test.AbstractIntegrationTest;

public class EmailTest extends AbstractIntegrationTest {

    Email email;

    @Before
    public void setup() {
        email = new Email();
    }
    
    @Test
    public void itReturnsTheRightServerCodeForDown(){
        email.setEmailSubject("Down: Boca-AS-101 ");
        assertThat("Boca-AS-101", is(equalTo(email.serverCode())));
    }

    @Test
    public void itReturnsTheRightServerCodeForUp(){
        email.setEmailSubject("Node: Boca-AS-101  UP");
        assertThat("Boca-AS-101", is(equalTo(email.serverCode())));
    }
}
