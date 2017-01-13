package com.idione.inoc.forms;

import com.idione.inoc.models.Client;

public class ClientForm {

    private String name;
    private String email;
    private int id;

    public ClientForm() {}
    
    public ClientForm(Client  client) {
        this.id = client.getInteger("id");
        this.name = client.getString("name");
        this.email = client.getString("email");
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
