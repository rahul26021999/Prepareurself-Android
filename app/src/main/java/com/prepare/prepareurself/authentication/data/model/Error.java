package com.prepare.prepareurself.authentication.data.model;

import java.util.List;

public class Error {

    private List<String> first_name = null;
    private List<String> password = null;
    private List<String> email = null;

    public Error(){

    }

    public List<String> getFirst_name() {
        return first_name;
    }

    public void setFirst_name(List<String> first_name) {
        this.first_name = first_name;
    }

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password) {
        this.password = password;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }
}
