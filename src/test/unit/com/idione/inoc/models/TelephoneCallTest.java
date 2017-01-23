package com.idione.inoc.models;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.javalite.activejdbc.LazyList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.idione.inoc.services.TelephoneService;
import com.idione.inoc.test.AbstractIntegrationTest;

import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class TelephoneCallTest extends AbstractIntegrationTest {

    IssuePocUser issuePocUser;
    String externalCallId1 = "call_1";

    @Before
    public void createFilter() {
        Client client = Client.createIt("name", "Mickey Mouse Club House");
        Email email = Email.createIt("client_id", client.getInteger("id"));
        MailingGroup mailingGroup = MailingGroup.createIt("client_id", client.getInteger("id"));
        Filter filter = Filter.createIt("name", "A Filter", "client_id", client.getInteger("id"), "time_interval", 5, "retries", 3, "mailing_group_id", mailingGroup.getInteger("id"));
        Issue issue = Issue.createIt("email_id", email.getInteger("id"), "filter_id", filter.getInteger("id"));
        PocUser pocUser = PocUser.createIt("client_id", client.getInteger("id"), "first_name", "Mickey", "last_name", "Mouse", "phone_number", "");

        issuePocUser = new IssuePocUser();
        issuePocUser.set("issue_id", issue.getId());
        issuePocUser.set("poc_user_id", pocUser.getId());
        issuePocUser.set("user_response", "none");
        issuePocUser.saveIt();
    }

    @Test
    public void createOrUpdateCreatesANewRecord() {
        TelephoneCall.createOrUpdate(issuePocUser.getInteger("id"), externalCallId1, TelephoneService.RINGING_STATUS.toString(), false);
        LazyList<TelephoneCall> telephoneCalls = TelephoneCall.find("external_call_id = ?", externalCallId1);
        assertThat(telephoneCalls.size(), is(equalTo(1)));
        assertThat(telephoneCalls.get(0).getString("external_call_id"), is(equalTo(externalCallId1)));
        assertThat(telephoneCalls.get(0).getInteger("issue_poc_user_id"), is(equalTo(issuePocUser.getInteger("id"))));
    }

    @Test
    public void createOrUpdateUpdatesTheRecord() {
        TelephoneCall.createOrUpdate(0, externalCallId1, TelephoneService.RINGING_STATUS.toString(), false);
        TelephoneCall.createOrUpdate(issuePocUser.getInteger("id"), externalCallId1, TelephoneService.COMPLETED_STATUS.toString(), true);

        LazyList<TelephoneCall> telephoneCalls = TelephoneCall.find("external_call_id = ?", externalCallId1);
        assertThat(telephoneCalls.size(), is(equalTo(1)));
        assertThat(telephoneCalls.get(0).getString("call_status"), is(equalTo(TelephoneService.COMPLETED_STATUS.toString())));
        assertThat(telephoneCalls.get(0).getInteger("issue_poc_user_id"), is(equalTo(issuePocUser.getInteger("id"))));
    }

    @Test
    public void createOrUpdateDoesNotUpdateStatusIfFlagIsFalse() {
        TelephoneCall.createOrUpdate(0, externalCallId1, TelephoneService.RINGING_STATUS.toString(), false);
        TelephoneCall.createOrUpdate(issuePocUser.getInteger("id"), externalCallId1, TelephoneService.COMPLETED_STATUS.toString(), false);

        LazyList<TelephoneCall> telephoneCalls = TelephoneCall.find("external_call_id = ?", externalCallId1);
        assertThat(telephoneCalls.size(), is(equalTo(1)));
        assertThat(telephoneCalls.get(0).getString("call_status"), is(equalTo(TelephoneService.RINGING_STATUS.toString())));
        assertThat(telephoneCalls.get(0).getInteger("issue_poc_user_id"), is(equalTo(issuePocUser.getInteger("id"))));
    }
}
