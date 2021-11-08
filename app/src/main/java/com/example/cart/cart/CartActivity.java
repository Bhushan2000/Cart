package com.example.cart.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.number.Precision;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cart.MainActivity;
import com.example.cart.R;
import com.example.cart.room.Cart;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.Calculation {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> carts;
    private Button btnBuy;
    private TextView tvTotal;


    private double subTotal = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);

        subTotal = Double.valueOf(subTotal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Cart");

        recyclerView = findViewById(R.id.recyclerview);

        tvTotal = findViewById(R.id.tvTotal);


        btnBuy = findViewById(R.id.btnBuy);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(linearLayoutManager);

        getCartData();


        for (int i = 0; i < carts.size(); i++) {


            subTotal += carts.get(i).getPrice();

        }


        tvTotal.setText("Rs. " + subTotal);


    }

    private void getCartData() {
        carts = MainActivity.myDatabase.cartDao().getData();
        cartAdapter = new CartAdapter(carts, this);
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public double increase(String price) {
        subTotal = subTotal + Double.parseDouble(price);
        tvTotal.setText("Rs. " + subTotal);

        return subTotal;
    }

    @Override
    public double delete(String price) {
        subTotal = subTotal - Double.parseDouble(price);
        tvTotal.setText("Rs. " + subTotal);

        return subTotal;
    }

    @Override
    public double decrease(String price) {
        subTotal = subTotal - Double.parseDouble(price);
        tvTotal.setText("Rs. " + subTotal);
        return subTotal;
    }
}