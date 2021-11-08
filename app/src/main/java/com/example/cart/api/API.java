package com.example.cart.api;

import com.example.cart.model.response.ProductResponse;

import java.util.ArrayList;

import retrofit2.Call;

import retrofit2.http.GET;

public interface API {

    @GET("/products")
    Call<ArrayList<ProductResponse>> getProducts();


}
