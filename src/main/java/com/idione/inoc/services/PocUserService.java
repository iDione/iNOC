package com.idione.inoc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.idione.inoc.forms.PocUserForm;
import com.idione.inoc.models.PocUser;

@Service
public class PocUserService {
    public List<PocUser> getPocUsers(int clientId) {
        return PocUser.find("client_id = ?", clientId);
    }

    public PocUserForm getPocUser(int id) {
        PocUser pocUser = PocUser.findFirst("id = ?", id);
        if (pocUser != null) {
            return new PocUserForm(pocUser);
        } else {
            return new PocUserForm();
        }

    }

    public PocUser savePocUser(PocUserForm pocUserForm) {
        if (pocUserForm.getId() > 0) {
            PocUser pocUser = PocUser.findFirst("id = ?", pocUserForm.getId());
            pocUser.set("first_name", pocUserForm.getFirstName(), "last_name", pocUserForm.getLastName(), "email_address", pocUserForm.getEmailAddress(), "phone_number", pocUserForm.getPhoneNumber()).saveIt();
            return pocUser;
        } else {
            return PocUser.createIt("client_id", pocUserForm.getClientId(), "first_name", pocUserForm.getFirstName(), "last_name", pocUserForm.getLastName(), "email_address", pocUserForm.getEmailAddress(), "phone_number", pocUserForm.getPhoneNumber());
        }
    }
}
