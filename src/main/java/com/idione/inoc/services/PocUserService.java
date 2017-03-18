package com.idione.inoc.services;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.idione.inoc.enums.Role;
import com.idione.inoc.forms.PocUserForm;
import com.idione.inoc.models.PocUser;
import com.idione.inoc.models.PocUserRole;

@Service
public class PocUserService {

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    public List<PocUser> getPocUsers(int clientId) {
        if(clientId > 0) {
            return PocUser.find("client_id = ?", clientId);
        } else {
            return PocUser.findAll();
        }
    }

    public PocUserForm getPocUser(int id) {
        PocUser pocUser = PocUser.findFirst("id = ?", id);
        if (pocUser != null) {
            return new PocUserForm(pocUser);
        } else {
            return new PocUserForm();
        }
    }

    public PocUser getPocUserByEmail(String email) {
        PocUser pocUser =  PocUser.findFirst("email_address = ?", email);
        return pocUser;
    }

    public PocUser savePocUser(PocUserForm pocUserForm) {
        if (pocUserForm.getId() > 0) {
            PocUser pocUser = PocUser.findFirst("id = ?", pocUserForm.getId());
            pocUser.set("first_name", pocUserForm.getFirstName(), "last_name", pocUserForm.getLastName(), "email_address", pocUserForm.getEmailAddress(), "phone_number", pocUserForm.getPhoneNumber()).saveIt();
            if(pocUserForm.getRoles().size() > 0){
                PocUserRole.delete("poc_user_id = ?", pocUserForm.getId());
                for(String role : pocUserForm.getRoles()){
                    PocUserRole.createIt("poc_user_id", pocUserForm.getId(), "role", role);
                }
            }
            return pocUser;
        } else {
            PocUser pocUser = PocUser.createIt("client_id", pocUserForm.getClientId(), "first_name", pocUserForm.getFirstName(), "last_name", pocUserForm.getLastName(), "email_address", pocUserForm.getEmailAddress(), "phone_number", pocUserForm.getPhoneNumber());
            if(!StringUtils.isEmpty(pocUserForm.getPassword())){
                pocUser.set("password", passwordEncoder().encode(pocUserForm.getPassword())).saveIt();
            }
            if(pocUserForm.getRoles().size() > 0){
                for(String role : pocUserForm.getRoles()){
                    PocUserRole.createIt("poc_user_id", pocUser.getId(), "role", role);
                }
            } else {
                PocUserRole.createIt("poc_user_id", pocUser.getInteger("id"), "role", Role.USER.toString());
            }
            return pocUser;
        }
    }
}
