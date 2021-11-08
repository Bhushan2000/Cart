package com.example.cart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cart.room.Cart;

public class ProductDetailsActivity extends AppCompatActivity {
    String title;
    String price;
    float rating;
    String count;
    String image;
    String category;
    String description;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        title = getIntent().getStringExtra("KEY1");
        price = getIntent().getStringExtra("KEY2");
        rating = getIntent().getFloatExtra("KEY3", 0.0f);
        count = getIntent().getStringExtra("KEY4");
        image = getIntent().getStringExtra("KEY5");
        category = getIntent().getStringExtra("KEY6");
        description = getIntent().getStringExtra("KEY7");
        id = getIntent().getStringExtra("KEY8");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ImageView ivProductImage = findViewById(R.id.ivImage);

        RatingBar ratingBar = findViewById(R.id.ratingBar);

        TextView tvCount = findViewById(R.id.tvCount);

        TextView tvCategory = findViewById(R.id.tvCategory);

        TextView tvName = findViewById(R.id.tvName);

        TextView tvPrice = findViewById(R.id.tvPrice);

        TextView tvDescription = findViewById(R.id.tvDescription);


        Glide.with(this).load(image).into(ivProductImage);
        ratingBar.setRating(rating);
        tvCount.setText(count);
        tvCategory.setText(category);
        tvName.setText(title);
        tvPrice.setText("Rs. " + price);
        tvDescription.setText(description);

    }

    public void onAddToCart(View view) {
        Cart cart = new Cart();
        cart.setId(Integer.valueOf(id));
        cart.setImage(image);
        cart.setTitle(title);
        cart.setPrice(Double.valueOf(price));
        if (MainActivity.myDatabase.cartDao().isAddToCart(Integer.valueOf(id)) != 1) {
            MainActivity.myDatabase.cartDao().addToCart(cart);
            Toast.makeText(this, "Added to cart.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "You are Already added to cart!", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);

    }
}