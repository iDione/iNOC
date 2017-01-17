package com.idione.inoc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idione.inoc.forms.MailingGroupForm;
import com.idione.inoc.models.MailingGroup;
import com.idione.inoc.models.MailingGroupPocUser;

@Service
public class MailingGroupService {
    public List<MailingGroup> getMailingGroups(int clientId) {
        return MailingGroup.find("client_id = ?", clientId);
    }

    public MailingGroupForm getMailingGroup(int id) {
        MailingGroup mailingGroup = MailingGroup.findFirst("id = ?", id);
        if (mailingGroup != null) {
            return new MailingGroupForm(mailingGroup);
        } else {
            return new MailingGroupForm();
        }

    }

    public MailingGroup saveMailingGroup(MailingGroupForm mailingGroupForm) {
        if (mailingGroupForm.getId() > 0) {
            MailingGroup mailingGroup = MailingGroup.findFirst("id = ?", mailingGroupForm.getId());
            mailingGroup.set("name", mailingGroupForm.getName()).saveIt();
            deletePocUsers(mailingGroupForm.getId());
            createPocUsers(mailingGroupForm.getId(), mailingGroupForm.getPocUserIds());
            return mailingGroup;
        } else {
            MailingGroup mailingGroup = MailingGroup.createIt("client_id", mailingGroupForm.getClientId(), "name", mailingGroupForm.getName());
            createPocUsers(mailingGroup.getInteger("id"), mailingGroupForm.getPocUserIds());
            return mailingGroup;
        }
    }
    
    private void deletePocUsers(int mailingGroupId) {
        MailingGroupPocUser.delete("mailing_group_id = ?", mailingGroupId);
    }
    
    private void createPocUsers(int mailingGroupId, List<Integer> pocUserIds) {
        for(Integer pocUserId : pocUserIds) {
            MailingGroupPocUser.createIt("mailing_group_id", mailingGroupId, "poc_user_id", pocUserId);
        }
    }
}
