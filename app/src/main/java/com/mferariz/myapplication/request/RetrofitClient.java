package com.mferariz.myapplication.request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String baseURL = "https://reqres.in/api/";
    private static final Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(baseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    public static UserRequest getUserRequest() {
        return retrofit.create(UserRequest.class);
    }

}
