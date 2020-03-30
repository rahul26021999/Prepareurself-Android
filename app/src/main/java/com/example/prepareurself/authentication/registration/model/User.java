package com.example.prepareurself.authentication.registration.model;

public class User {
    public String email, first_name, last_name, password, username, updated_at, created_at;
    public int id;

    public User(String email, String first_name, String last_name, String password ,String username, String updated_at, String created_at, int id) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.username = username;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.id = id;
    }
}
