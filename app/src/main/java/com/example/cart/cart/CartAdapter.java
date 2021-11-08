package com.example.cart.cart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cart.MainActivity;
import com.example.cart.model.response.ProductResponse;
import com.example.cart.R;
import com.example.cart.room.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    List<Cart> list;

    Context context;

    int count = 1;

    int MAX = 10;

    int MIN = 1;


    interface Calculation {

        public double increase(String price);

        public double delete(String price);

        public double decrease(String price);
    }


    public CartAdapter(List<Cart> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = list.get(position);


        holder.tvName.setText(cart.getTitle());
        holder.tvCategory.setText(cart.getCategory());
        double price = cart.getPrice();

        String priceStr = String.format("%1.2f", price);

        holder.tvPrice.setText("Rs. " + priceStr);
        Glide.with(context).load(cart.getImage()).into(holder.ivImage);
        holder.tvCounter.setText(String.valueOf(count));


        holder.imageButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (count == MIN) {
                    Toast.makeText(context, "Select At least one quantity.", Toast.LENGTH_SHORT).show();
                } else {

                    if (context instanceof Calculation) {
                        ((Calculation) context).decrease(priceStr);
                    }
                    count--;
                    holder.tvCounter.setText(String.valueOf(count));
                }
            }
        });
        holder.imageButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (count == MAX) {
                    Toast.makeText(context, "We have Limited amount of products.", Toast.LENGTH_SHORT).show();
                } else {

                    if (context instanceof Calculation) {
                        ((Calculation) context).increase(priceStr);
                    }

                    count++;
                    holder.tvCounter.setText(String.valueOf(count));
                }
            }
        });

        holder.imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        //set icon
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        //set title
                        .setTitle("Are you want to remove this product?")
                        //set message
                        .setMessage("Product removing from cart")
                        //set positive button
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what would happen when positive button is clicked
                                list.remove(position);
                                notifyDataSetChanged();
                                MainActivity.myDatabase.cartDao().deleteItem(cart.getId());
                                int cartcount = MainActivity.myDatabase.cartDao().countCart();

                                Intent intent = new Intent("mymsg");
                                intent.putExtra("cartcount", cartcount);

                                if (context instanceof Calculation) {
                                    ((Calculation) context).delete(priceStr);
                                }
                                holder.tvCounter.setText(String.valueOf(count));
                            }
                        })
                        //set negative button
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                                dialogInterface.dismiss();
                            }
                        })
                        .show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvPrice, tvCounter;
        ImageButton imageButtonMinus, imageButtonPlus, imageButtonDelete;
        ImageView ivImage;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCounter = itemView.findViewById(R.id.tvCounter);
            imageButtonMinus = itemView.findViewById(R.id.imageButtonMinus);
            imageButtonPlus = itemView.findViewById(R.id.imageButtonPlus);
            imageButtonDelete = itemView.findViewById(R.id.imageButtonDelete);

            tvName = itemView.findViewById(R.id.tvName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivImage = itemView.findViewById(R.id.ivImage);

        }
    }


    static class CartDiff extends DiffUtil.ItemCallback<Cart> {

        @Override
        public boolean areItemsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Cart oldItem, @NonNull Cart newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }


}
