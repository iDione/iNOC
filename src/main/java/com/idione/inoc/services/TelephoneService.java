package com.idione.inoc.services;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.idione.inoc.integration.TwilioClient;
import com.idione.inoc.models.IssuePocUser;
import com.idione.inoc.models.TelephoneCall;

@Service
public class TelephoneService {
    public static final String RINGING_STATUS = "ringing";
    public static final String QUEUED_STATUS = "queued";
    public static final String BUSY_STATUS = "busy";
    public static final String PROGRESS_STATUS = "progress";
    public static final String COMPLETED_STATUS = "completed";
    public static final String FAILED_STATUS = "failed";
    public static final String NO_ANSWER_STATUS = "no-answer";

    public static ArrayList<String> callStatuses;
    public static ArrayList<String> callCompletedStatuses;
    public static ArrayList<String> callFailedStatuses;

    static {
        callFailedStatuses = new ArrayList<String>();
        callFailedStatuses.add(FAILED_STATUS);
        callFailedStatuses.add(NO_ANSWER_STATUS);
    }

    static {
        callCompletedStatuses = new ArrayList<String>();
        callCompletedStatuses.add(COMPLETED_STATUS);
        callCompletedStatuses.addAll(callFailedStatuses);
    }

    static {
        callStatuses = new ArrayList<String>();
        callStatuses.add(RINGING_STATUS);
        callStatuses.add(QUEUED_STATUS);
        callStatuses.add(BUSY_STATUS);
        callStatuses.add(PROGRESS_STATUS);
        callStatuses.addAll(callCompletedStatuses);
    }

    private TwilioClient twilioClient;

    public TelephoneService(TwilioClient twilioClient) {
        this.twilioClient = twilioClient;
    }

    public String makeIssueAcceptanceCall(int issuePocUserId, String phoneNumber, int retriesLimit, int intervalBetweenCalls) {
        int retries = 1;
        TelephoneCall telephoneCall = twilioClient.makeIssueAcceptanceCall(issuePocUserId, phoneNumber);
        IssuePocUser issuePocUser = IssuePocUser.findById(issuePocUserId);
        while ((retries < retriesLimit && callFailedStatuses.contains(telephoneCall.getString("call_status"))) && issuePocUser.getString("user_response").compareTo("none") == 0) {
            if(retries!=1){
                try {
                    TimeUnit.SECONDS.sleep(intervalBetweenCalls);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            telephoneCall = twilioClient.makeIssueAcceptanceCall(issuePocUserId, phoneNumber);
            issuePocUser = IssuePocUser.findById(issuePocUserId);
            retries++;
        }
        return IssuePocUser.findById(issuePocUserId).getString("user_response");
    }
}
