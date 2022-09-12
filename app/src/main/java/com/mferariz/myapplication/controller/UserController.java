package com.mferariz.myapplication.controller;

import android.content.Context;

import com.mferariz.myapplication.model.LoginRegisterData;
import com.mferariz.myapplication.model.LoginResult;
import com.mferariz.myapplication.model.RegisterResult;
import com.mferariz.myapplication.model.Session;
import com.mferariz.myapplication.model.SingleUserResponse;
import com.mferariz.myapplication.model.UsersResponse;
import com.mferariz.myapplication.request.RetrofitClient;
import com.mferariz.myapplication.request.UserRequest;
import com.mferariz.myapplication.sqllite.SqlLiteAdmin;

import retrofit2.Call;

public class UserController {

    private final UserRequest userRequest = RetrofitClient.getUserRequest();
    private SqlLiteAdmin sqlLiteAdmin;

    public UserController() {
    }

    public UserController(Context context) {
        this.sqlLiteAdmin = new SqlLiteAdmin(context);
    }

    public Call<LoginResult> login(LoginRegisterData data){
        return userRequest.login(data);
    }

    public Call<RegisterResult> register(LoginRegisterData data){
        return userRequest.register(data);
    }

    public Call<UsersResponse> getUsers(){
        return userRequest.getUsers();
    }

    public Call<SingleUserResponse> getUser(int id){
        return userRequest.getUser(id);
    }

    public Long saveSession(Session session){
        if(sqlLiteAdmin != null){
            return sqlLiteAdmin.saveSession(session.getEmail(), session.getPassword(), session.getToken());
        }
        return 0L;
    }

    public int clearSession(){
        if(sqlLiteAdmin != null){
            return sqlLiteAdmin.clearSession();
        }
        return 0;
    }

    public Session getSession() {
        return sqlLiteAdmin.getSession();
    }
}
