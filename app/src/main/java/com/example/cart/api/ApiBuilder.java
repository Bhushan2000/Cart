package com.example.cart.api;


import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {

    private static ApiBuilder instance = null;
    private String BASE_URL = "https://fakestoreapi.com";


    private API api;

    private ApiBuilder() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(API.class);


    }

    public static synchronized ApiBuilder getInstance() {
        if (instance == null) {
            instance = new ApiBuilder();
        }
        return instance;

    }

    public API getApi() {
        return api;
    }


}
