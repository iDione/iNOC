package com.idione.inoc.forms;

import com.idione.inoc.models.PocUser;

public class PocUserForm {
    
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private int id;
    private int clientId;

    public PocUserForm() {}
    
    public PocUserForm(int clientId) {
        this.clientId = clientId;
    }
    
    public PocUserForm(PocUser  pocUser) {
        this.id = pocUser.getInteger("id");
        this.clientId = pocUser.getInteger("client_id");
        this.firstName = pocUser.getString("first_name");
        this.lastName = pocUser.getString("last_name");
        this.emailAddress = pocUser.getString("email_address");
        this.phoneNumber = pocUser.getString("phone_number");
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

}
