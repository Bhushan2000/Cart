package com.example.cart.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Cart.class}, version = 1)

public abstract class MyDatabase extends RoomDatabase {
    public abstract CartDao cartDao();


}
