package com.idione.inoc.forms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.PocUser;

public class MailingGroupForm {

    @NotEmpty private String name;
    private int id;
    private int clientId;
    private List<Integer> pocUserIds = new ArrayList<Integer>();
    private String includeEmailAddresses;

    public MailingGroupForm() {
    }

    public MailingGroupForm(int clientId) {
        this.clientId = clientId;
    }

    public MailingGroupForm(MailingGroup mailingGroup) {
        this.id = mailingGroup.getInteger("id");
        this.clientId = mailingGroup.getInteger("client_id");
        this.name = mailingGroup.getString("name");
        this.includeEmailAddresses = mailingGroup.getString("include_email_addresses");
        for (PocUser pocUser : mailingGroup.getUsers()) {
            pocUserIds.add(pocUser.getInteger("id"));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public List<Integer> getPocUserIds() {
        return pocUserIds;
    }

    public void setPocUserIds(List<Integer> pocUserIds) {
        this.pocUserIds = pocUserIds;
    }

    public String getIncludeEmailAddresses() {
        return includeEmailAddresses;
    }

    public void setIncludeEmailAddresses(String includeEmailAddresses) {
        this.includeEmailAddresses = includeEmailAddresses;
    }

}
