package com.mferariz.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class SingleUserResponse {

    @SerializedName("data")
    private User user;

    public User getUser() {
        return user;
    }
}
