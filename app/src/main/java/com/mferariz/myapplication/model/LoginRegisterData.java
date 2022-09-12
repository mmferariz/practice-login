package com.mferariz.myapplication.model;

public class LoginRegisterData {

    private String email;
    private String password;

    public LoginRegisterData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
