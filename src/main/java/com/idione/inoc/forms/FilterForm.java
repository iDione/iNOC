package com.idione.inoc.forms;

import java.util.ArrayList;
import java.util.List;

import com.idione.inoc.models.Filter;
import com.idione.inoc.models.FilterKeyword;
import com.idione.inoc.models.PocUser;

public class FilterForm {
    
    private String name;
    private int id;
    private int clientId;
    private int timeInterval;
    private int retries;
    private int mailingGroupId;
    private List<Integer> pocUserIds = new ArrayList<Integer>();
    private List<String> keywords = new ArrayList<String>();
    
    public FilterForm() {}
    
    public FilterForm(int clientId) {
        this.clientId = clientId;
    }
    
    public FilterForm(Filter  filter) {
        this.id = filter.getInteger("id");
        this.clientId = filter.getInteger("client_id");
        this.name = filter.getString("name");
        this.mailingGroupId = filter.getInteger("mailing_group_id");
        this.retries = filter.getInteger("retries");
        this.timeInterval = filter.getInteger("time_interval");
        for(PocUser pocUser : filter.getUsers()) {
            pocUserIds.add(pocUser.getInteger("id"));
        }
        for(FilterKeyword keyword : filter.getKeywords()) {
            keywords.add(keyword.getString("keyword"));
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

    public int getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getMailingGroupId() {
        return mailingGroupId;
    }

    public void setMailingGroupId(int mailingGroupId) {
        this.mailingGroupId = mailingGroupId;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
