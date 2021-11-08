package com.example.cart;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cart.api.API;
import com.example.cart.api.ApiBuilder;
import com.example.cart.cart.CartActivity;
import com.example.cart.cart.CartAdapter;
import com.example.cart.model.response.ProductResponse;
import com.example.cart.room.MyDatabase;
import com.example.cart.room.MyDatabase_Impl;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductsAdapter productsAdapter;

    private String TAG = getClass().getSimpleName();
    private ProgressBar progressBar;
    public static MyDatabase myDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);


        // Database
        myDatabase = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "My_Cart").allowMainThreadQueries().build();


        progressBar = findViewById(R.id.progressBar);

        API api = ApiBuilder.getInstance().getApi();

        Call<ArrayList<ProductResponse>> call = api.getProducts();

        call.enqueue(new Callback<ArrayList<ProductResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductResponse>> call, Response<ArrayList<ProductResponse>> response) {

                if (response != null) {
                    progressBar.setVisibility(View.GONE);

                    Log.d(TAG, "onResponse: " + response.body());

                    productsAdapter = new ProductsAdapter(response.body(), MainActivity.this);

                    recyclerView.setAdapter(productsAdapter);

                    productsAdapter.notifyDataSetChanged();
                } else {

                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(MainActivity.this, "Error While Fetching data !!", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "onResponse: " + response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<ArrayList<ProductResponse>> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Error While Fetching data !! " + t.getMessage(), Toast.LENGTH_SHORT).show();

                progressBar.setVisibility(View.GONE);

                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });




    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.menu_cart:

                // write your code here

                startActivity(new Intent(MainActivity.this, CartActivity.class));

                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}