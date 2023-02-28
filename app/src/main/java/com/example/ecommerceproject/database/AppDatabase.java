package com.example.ecommerceproject.database;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ecommerceproject.dao.AccountDao;
import com.example.ecommerceproject.dao.ProductDao;
import com.example.ecommerceproject.entities.Account;
import com.example.ecommerceproject.entities.Product;

@Database(entities = {Account.class, Product.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase appDatabase;

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context,
                    AppDatabase.class, "app").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }

        // returns the singleton object
        return appDatabase;

    }

    public abstract AccountDao accountDao();

    public abstract ProductDao productDao();
}