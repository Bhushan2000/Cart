package com.example.cart;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cart.cart.CartActivity;
import com.example.cart.model.response.ProductResponse;
import com.example.cart.room.Cart;
import com.example.cart.room.CartDao;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    List<ProductResponse> list;
    Context context;

    public ProductsAdapter(List<ProductResponse> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);


        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {

        ProductResponse productResponse = list.get(position);
        holder.tvProductName.setText(productResponse.getTitle());
        String upperStringCategory = productResponse.getCategory().substring(0, 1).toUpperCase() + productResponse.getCategory().substring(1).toLowerCase();

        holder.tvCategory.setText(upperStringCategory);
        holder.tvPrice.setText("Rs. " + String.valueOf(productResponse.getPrice()));
        Glide.with(context).load(productResponse.getImage()).centerCrop().into(holder.ivProductImage);
        holder.ratingBar.setRating((float) productResponse.getRating().getRate());
        holder.tvCount.setText("(" + String.valueOf(productResponse.getRating().getCount()) + ")");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("KEY1", productResponse.getTitle());
                intent.putExtra("KEY2", String.valueOf(productResponse.getPrice()));
                intent.putExtra("KEY3", (float) productResponse.getRating().getRate());
                intent.putExtra("KEY4", String.valueOf(productResponse.getRating().getCount()));
                intent.putExtra("KEY5", productResponse.getImage());
                intent.putExtra("KEY6", upperStringCategory);
                intent.putExtra("KEY7", productResponse.getDescription());
                intent.putExtra("KEY8", String.valueOf(productResponse.getId()));

                holder.itemView.getContext().startActivity(intent);

            }
        });

        holder.imageButtonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cart cart = new Cart();
                cart.setId(productResponse.getId());
                cart.setImage(productResponse.getImage());
                cart.setTitle(productResponse.getTitle());
                cart.setPrice(productResponse.getPrice());
                cart.setCategory(upperStringCategory);
                if (MainActivity.myDatabase.cartDao().isAddToCart(productResponse.getId()) != 1) {
                    MainActivity.myDatabase.cartDao().addToCart(cart);
                    Toast.makeText(context, "Added to cart.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "You are Already added to cart!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductsViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductName, tvCategory, tvPrice, tvCount;
        ImageView ivProductImage;
        RatingBar ratingBar;
        ImageButton imageButtonAddToCart;


        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);


            tvProductName = itemView.findViewById(R.id.tvProductName);

            tvCategory = itemView.findViewById(R.id.tvCategory);

            tvPrice = itemView.findViewById(R.id.tvPrice);

            ivProductImage = itemView.findViewById(R.id.ivProductImage);

            ratingBar = itemView.findViewById(R.id.ratingBar);

            tvCount = itemView.findViewById(R.id.tvCount);

            imageButtonAddToCart = itemView.findViewById(R.id.imageButtonAddToCart);

        }
    }
}
