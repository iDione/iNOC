package com.idione.inoc.controllers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.models.TelephoneCall;
import com.idione.inoc.services.IssueResponseService;
import com.idione.inoc.services.TelephoneService;
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
    public void updateUserResponseCallsTheIssueResponseService(@Mocked IssueResponseService issueResponseService) {
        TwilioController controller = new TwilioController();
        controller.setIssueResponseService(issueResponseService);
        controller.updateUserResponse(userResponseNumber, externalCallId);
        new Verifications() {
            {
                issueResponseService.updateIssueWithUserResponse(userResponseNumberInt, externalCallId);
                times = 1;
            }
        };
    }

    @Test
    public void updateCallStatusUpdatesTheCallStatus(@Mocked TelephoneService telephoneService) {
        TelephoneCall telephoneCall = new TelephoneCall();
        telephoneCall.set("external_call_id", externalCallId, "call_status", TelephoneService.RINGING_STATUS.toString());
        telephoneCall.saveIt();

        TwilioController controller = new TwilioController();
        controller.updateCallStatus(TelephoneService.COMPLETED_STATUS.toString(), externalCallId);

        telephoneCall.refresh();

        assertThat(telephoneCall.getString("call_status"), is(equalTo(TelephoneService.COMPLETED_STATUS.toString())));
    }

    @Test
    public void updateCallStatusCreatesTheCallIfItDoesntExist(@Mocked TelephoneService telephoneService) {
        TwilioController controller = new TwilioController();
        controller.updateCallStatus(TelephoneService.RINGING_STATUS.toString(), externalCallId);

        TelephoneCall telephoneCall = TelephoneCall.findFirst("external_call_id = ?", externalCallId);

        assertThat(telephoneCall.getString("call_status"), is(equalTo(TelephoneService.RINGING_STATUS.toString())));
    }
}
