package com.idione.inoc.twilio;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.idione.inoc.models.TelephoneCall;
import com.idione.inoc.services.TelephoneService;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.CallCreator;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioClient {

    public static final String ACCOUNT_SID = "ACe869cb5b1c250d93176735067e0203cc";
    public static final String AUTH_TOKEN = "a0db3f876d4530added4b1933e952013";
    public static final String ISSUE_URL = "https://handler.twilio.com/twiml/EH74f53755776b341e126a03a58d9ca849";
    public static final String FROM_PHONE_NUMBER = "(312) 273-5098";
    public static final String ISSUE_CALL_STATUS_CALLBACK_URL = "http://adarsh.hopto.org:8080/twilio/callStatus";

    public TelephoneCall makeIssueAcceptanceCall(int issuePocUserId, String phoneNumber) {
        TwilioRestClient client = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();
        PhoneNumber to = new PhoneNumber(phoneNumber);
        PhoneNumber from = new PhoneNumber(FROM_PHONE_NUMBER);
        URI uri = URI.create(ISSUE_URL);
        CallCreator callCreator = new CallCreator(to, from, uri);
        callCreator.setStatusCallbackEvent(TelephoneService.callCompletedStatuses);
        callCreator.setStatusCallback(ISSUE_CALL_STATUS_CALLBACK_URL);
        Call call = callCreator.create(client);
        TelephoneCall.createOrUpdate(issuePocUserId, call.getSid(), "initiated", false);
        while (!TelephoneService.callCompletedStatuses.contains(call.getStatus().toString())) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                // do nothing
            }
            call = Call.updater(call.getSid()).update(client);
        }
        TelephoneCall telephoneCall = TelephoneCall.createOrUpdate(issuePocUserId, call.getSid(), call.getStatus().toString(), true);
        return telephoneCall;
    }
}
