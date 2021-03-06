package com.idione.inoc.models;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToParents;
import org.javalite.activejdbc.annotations.Table;
import org.springframework.util.StringUtils;


@BelongsToParents({ @BelongsTo(parent = IssuePocUser.class, foreignKeyName = "issue_poc_user_id") })

@Table("telephone_calls")
public class TelephoneCall extends Model {

    public static TelephoneCall createOrUpdate(int issuePocUserId, String externalCallId, String callStatus, boolean forceStatusUpdate) {
        TelephoneCall telephoneCall = findFirst("external_call_id = ?", externalCallId);
        if (telephoneCall != null) {
            if (forceStatusUpdate) {
                if (!StringUtils.isEmpty(callStatus)) {
                    telephoneCall.set("call_status", callStatus);
                }
                if (issuePocUserId > 0) {
                    telephoneCall.set("issue_poc_user_id", issuePocUserId);
                }
            } else {
                if (telephoneCall.getInteger("issue_poc_user_id") == null) {
                    telephoneCall.set("issue_poc_user_id", issuePocUserId);
                }
            }
            telephoneCall.saveIt();
        } else {
            Integer cleanedUpIssuePocUserId = null;
            if (issuePocUserId > 0) {
                cleanedUpIssuePocUserId = new Integer(issuePocUserId);
            }
            telephoneCall = TelephoneCall.createIt("issue_poc_user_id", cleanedUpIssuePocUserId, "external_call_id", externalCallId, "call_status", callStatus);
        }
        return telephoneCall;
    }

    public IssuePocUser getIssuePocUser() {
        return this.parent(IssuePocUser.class);
    }
}
