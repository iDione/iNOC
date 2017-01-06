package com.idione.inoc.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.services.IssueResponseService;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class TwilioControllerTest extends AbstractIntegrationTest {

    String userResponseNumber = "2";
    int userResponseNumberInt = 2;
    String externalCallId = "w9g3j3f7hd85m";

    @Test
    public void getPocUserResponseCallsTheIssueResponseService(@Mocked IssueResponseService issueResponseService) {
        TwilioController controller = new TwilioController();
        controller.setIssueResponseService(issueResponseService);
        controller.getPocUserResponse(userResponseNumber, externalCallId);
        new Verifications() {
            {
                issueResponseService.updateIssueWithUserResponse(userResponseNumberInt, externalCallId);
                times = 1;
            }
        };
    }
}
