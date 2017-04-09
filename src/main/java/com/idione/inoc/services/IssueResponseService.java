package com.idione.inoc.services;

import org.javalite.activejdbc.Base;
import org.springframework.stereotype.Service;

import com.idione.inoc.models.Email;
import com.idione.inoc.models.IssueEmail;
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

    public void updateIssueWithEmailResponse(int clientId, Email email) {
        Base.exec("update issue_emails set status = ?, updated_at = CURRENT_TIMESTAMP "
                + "from issue_emails ie inner join issues i on ie.issue_id = i.id "
                + "inner join filters f on i.filter_id = f.id " + "where f.client_id = ? "
                + "and ie.server_code = ? and ie.status != ?", email.serverStatus(), clientId, email.serverCode(), IssueEmail.RESOLVED_STATUS);
    }
}
