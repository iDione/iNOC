package com.idione.inoc.telephone.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.models.Client;
import com.idione.inoc.models.Email;
import com.idione.inoc.models.Filter;
import com.idione.inoc.models.Issue;
import com.idione.inoc.models.IssuePocUser;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.models.TelephoneCall;
import com.idione.inoc.test.AbstractIntegrationTest;
import com.idione.inoc.twilio.TwilioClient;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class TelephoneServiceTest extends AbstractIntegrationTest {

    TelephoneService telephoneService;

    IssuePocUser issuePocUser;
    String telephoneNumber = "1111111111";
    int retries = 3;

    @Before
    public void createFilter() {
        Client client = Client.createIt("name", "Mickey Mouse Club House");
        Email email = Email.createIt("client_id", client.getInteger("id"));
        MailingGroup mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"));
        Filter filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", retries, "mailing_group_id", mailingGroup.getInteger("id"));
        Issue issue = Issue.createIt("email_id", email.getInteger("id"), "filter_id", filter.getInteger("id"));
        PocUser pocUser = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Mickey", "last_name", "Mouse", "phone_number", telephoneNumber);

        issuePocUser = new IssuePocUser();
        issuePocUser.set("issue_id", issue.getId());
        issuePocUser.set("poc_user_id", pocUser.getId());
        issuePocUser.set("user_response", "none");
        issuePocUser.saveIt();
    }

    @Test
    public void itCallsTheTwilioClientToMakeAPhoneCall(@Mocked TwilioClient twilioClient) {
        TelephoneCall telephoneCall = new TelephoneCall();
        telephoneCall.set("call_status", TelephoneService.COMPLETED_STATUS);
        new Expectations() {
            {
                twilioClient.makeIssueAcceptanceCall(anyInt, anyString);
                result = telephoneCall;
            }
        };

        telephoneService = new TelephoneService(twilioClient);
        telephoneService.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), telephoneNumber, retries);

        new Verifications() {
            {
                twilioClient.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), telephoneNumber);
                times = 1;
            }
        };
    }

    @Test
    public void itCallsTheUserUntilRetriesLimitReached(@Mocked TwilioClient twilioClient) {
        TelephoneCall telephoneCall = new TelephoneCall();
        telephoneCall.set("call_status", TelephoneService.FAILED_STATUS, "issue_poc_user_id", issuePocUser.getInteger("id"));
        telephoneCall.saveIt();
        new Expectations() {
            {
                twilioClient.makeIssueAcceptanceCall(anyInt, anyString);
                result = telephoneCall;
            }
        };

        telephoneService = new TelephoneService(twilioClient);
        telephoneService.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), telephoneNumber, retries);

        new Verifications() {
            {
                twilioClient.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), telephoneNumber);
                times = 3;
            }
        };
    }

    // @Test
    // public void
    // itCallsTheUserUntilRetriesLimitReachedEvenForCallCompletedIfUserDoesntRespond(@Mocked
    // TwilioClient twilioClient) {
    // TelephoneCall telephoneCall = new TelephoneCall();
    // telephoneCall.set("call_status", TelephoneService.COMPLETED_STATUS,
    // "issue_poc_user_id", issuePocUser.getInteger("id"));
    // telephoneCall.saveIt();
    // new Expectations() {{
    // twilioClient.makeIssueAcceptanceCall(anyInt, anyString); result =
    // telephoneCall;
    // }};
    //
    // telephoneService = new TelephoneService(twilioClient);
    // telephoneService.makeIssueAcceptanceCall(issuePocUser.getInteger("id"),
    // telephoneNumber, retries);
    //
    // new Verifications() {{
    // twilioClient.makeIssueAcceptanceCall(issuePocUser.getInteger("id"),
    // telephoneNumber); times = 3;
    // }};
    // }

    @Test
    public void itStopsCallingIfUserResponds(@Mocked TwilioClient twilioClient) {
        TelephoneCall telephoneCall = new TelephoneCall();
        telephoneCall.set("call_status", TelephoneService.FAILED_STATUS, "issue_poc_user_id", issuePocUser.getInteger("id"));
        telephoneCall.saveIt();
        new Expectations() {
            {
                twilioClient.makeIssueAcceptanceCall(anyInt, anyString);
                result = telephoneCall;
            }
        };

        telephoneService = new TelephoneService(twilioClient);
        issuePocUser.set("user_response", "accepted");
        issuePocUser.saveIt();
        telephoneService.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), telephoneNumber, retries);

        new Verifications() {
            {
                twilioClient.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), telephoneNumber);
                times = 1;
            }
        };
    }

    @Test
    public void itReturnsTheUserResponse(@Mocked TwilioClient twilioClient) {
        TelephoneCall telephoneCall = new TelephoneCall();
        telephoneCall.set("call_status", TelephoneService.FAILED_STATUS, "issue_poc_user_id", issuePocUser.getInteger("id"));
        telephoneCall.saveIt();
        new Expectations() {
            {
                twilioClient.makeIssueAcceptanceCall(anyInt, anyString);
                result = telephoneCall;
            }
        };

        telephoneService = new TelephoneService(twilioClient);
        issuePocUser.set("user_response", "accepted");
        issuePocUser.saveIt();
        String userResponse = telephoneService.makeIssueAcceptanceCall(issuePocUser.getInteger("id"), telephoneNumber, retries);

        assertThat(userResponse, is(equalTo("accepted")));
    }
}
