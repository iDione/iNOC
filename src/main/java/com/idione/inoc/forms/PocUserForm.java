package com.idione.inoc.forms;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.idione.inoc.models.PocUser;
import com.idione.inoc.models.PocUserRole;

public class PocUserForm {

    @NotEmpty private String firstName;
    @NotEmpty private String lastName;
    @NotEmpty @Email private String emailAddress;
    @NotEmpty private String phoneNumber;
    private int id;
    private int clientId;

    private String password;
    private List<String> roles = new ArrayList<String>();

    public PocUserForm() {
    }

    public PocUserForm(int clientId) {
        this.clientId = clientId;
    }

    public PocUserForm(PocUser pocUser) {
        this.id = pocUser.getInteger("id");
        this.clientId = pocUser.getInteger("client_id");
        this.firstName = pocUser.getString("first_name");
        this.lastName = pocUser.getString("last_name");
        this.emailAddress = pocUser.getString("email_address");
        this.phoneNumber = pocUser.getString("phone_number");
        this.password = pocUser.getString("password");

        for(PocUserRole pocUserRole: pocUser.getRoles()){
            this.roles.add(pocUserRole.getString("role"));
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
