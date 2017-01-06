package com.idione.inoc.services;

import org.springframework.stereotype.Service;

import com.idione.inoc.models.TelephoneCall;

@Service
public class IssueResponseService {
    public static final int ISSUE_ACCEPTED_RESPONSE = 1;
    public static final int ISSUE_DECLINED_RESPONSE = 2;

    public static final String ISSUE_ACCEPTED_STATUS = "accepted";
    public static final String ISSUE_DECLINED_STATUS = "declined";

    public void updateIssueWithUserResponse(int userResponseNumber, String callSid) {
        TelephoneCall telephoneCall = TelephoneCall.findFirst("external_call_id = ?", callSid);
        telephoneCall.set("user_response", userResponseNumber).saveIt();
        telephoneCall.saveIt();

        if (userResponseNumber == ISSUE_ACCEPTED_RESPONSE) {
            telephoneCall.getIssuePocUser().set("user_response", ISSUE_ACCEPTED_STATUS).saveIt();
        } else if (userResponseNumber == ISSUE_DECLINED_RESPONSE) {
            telephoneCall.getIssuePocUser().set("user_response", ISSUE_DECLINED_STATUS).saveIt();
        }
    }
}
