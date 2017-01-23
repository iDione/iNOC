package com.idione.inoc.forms;

import com.idione.inoc.models.Client;

public class ClientForm {

    private String name;
    private String host;
    private String email;
    private String password;
    private int id;

    public ClientForm() {
    }

    public ClientForm(Client client) {
        this.id = client.getInteger("id");
        this.name = client.getString("name");
        this.email = client.getString("email");
        this.host = client.getString("host");
        this.password = client.getString("password");
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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
