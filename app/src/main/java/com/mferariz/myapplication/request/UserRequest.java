package com.mferariz.myapplication.request;

import com.mferariz.myapplication.model.RegisterResult;
import com.mferariz.myapplication.model.SingleUserResponse;
import com.mferariz.myapplication.model.LoginResult;
import com.mferariz.myapplication.model.LoginRegisterData;
import com.mferariz.myapplication.model.UsersResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserRequest {

    @POST("login")
    Call<LoginResult> login(@Body LoginRegisterData data);

    @POST("register")
    Call<RegisterResult> register(@Body LoginRegisterData data);

    @GET("users")
    Call<UsersResponse> getUsers();

    @GET("users/{id}")
    Call<SingleUserResponse> getUser(@Path("id") int id);

}
