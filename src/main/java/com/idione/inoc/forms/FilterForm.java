package com.idione.inoc.forms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.idione.inoc.models.Filter;
import com.idione.inoc.models.FilterKeyword;
import com.idione.inoc.models.PocUser;

public class FilterForm {

    @NotEmpty private String name;
    private Integer id;
    private Integer clientId;
    private Integer timeInterval = 0;
    private Integer retries = 0;
    private Integer assignedMailingGroupId;
    private Integer unassignedMailingGroupId;
    @NotEmpty private String assignedEmailTemplate;
    @NotEmpty private String unassignedEmailTemplate;
    @NotEmpty private List<Integer> pocUserIds = new ArrayList<Integer>();
    @NotEmpty private List<String> keywords = new ArrayList<String>();
    private Integer holdIssueCreationsFor = 0;
    
    public FilterForm() {
    }

    public FilterForm(Integer clientId) {
        this.clientId = clientId;
    }

    public FilterForm(Filter filter) {
        this.id = filter.getInteger("id");
        this.clientId = filter.getInteger("client_id");
        this.name = filter.getString("name");
        this.assignedMailingGroupId = filter.getInteger("assigned_mailing_group_id");
        this.unassignedMailingGroupId = filter.getInteger("unassigned_mailing_group_id");
        this.assignedEmailTemplate = filter.getString("assigned_email_template");
        this.unassignedEmailTemplate = filter.getString("unassigned_email_template");       
        this.retries = filter.getInteger("retries");
        this.timeInterval = filter.getInteger("time_interval");
        this.holdIssueCreationsFor = filter.getInteger("hold_issue_creations_for");
        for (PocUser pocUser : filter.getUsers()) {
            pocUserIds.add(pocUser.getInteger("id"));
        }
        for (FilterKeyword keyword : filter.getKeywords()) {
            keywords.add(keyword.getString("keyword"));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public List<Integer> getPocUserIds() {
        return pocUserIds;
    }

    public void setPocUserIds(List<Integer> pocUserIds) {
        this.pocUserIds = pocUserIds;
    }

    public Integer getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public Integer getAssignedMailingGroupId() {
        return assignedMailingGroupId;
    }

    public void setAssignedMailingGroupId(Integer assignedMailingGroupId) {
        this.assignedMailingGroupId = assignedMailingGroupId;
    }

    public Integer getUnassignedMailingGroupId() {
        return unassignedMailingGroupId;
    }

    public void setUnassignedMailingGroupId(Integer unassignedMailingGroupId) {
        this.unassignedMailingGroupId = unassignedMailingGroupId;
    }

    public String getAssignedEmailTemplate() {
        return assignedEmailTemplate;
    }

    public void setAssignedEmailTemplate(String assignedEmailTemplate) {
        this.assignedEmailTemplate = assignedEmailTemplate;
    }

    public String getUnassignedEmailTemplate() {
        return unassignedEmailTemplate;
    }

    public void setUnassignedEmailTemplate(String unassignedEmailTemplate) {
        this.unassignedEmailTemplate = unassignedEmailTemplate;
    }

    public Integer getHoldIssueCreationsFor() {
        return holdIssueCreationsFor;
    }

    public void setHoldIssueCreationsFor(Integer holdIssueCreationsFor) {
        this.holdIssueCreationsFor = holdIssueCreationsFor;
    }
}
