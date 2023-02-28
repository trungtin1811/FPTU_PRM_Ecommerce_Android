package com.example.ecommerceproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ecommerceproject.entities.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Product... Products);

    @Update
    void updateProducts(Product... Products);

    @Delete
    void delete(Product Product);

    @Query("SELECT * FROM products")
    List<Product> getAll();

    @Query("SELECT * FROM products p WHERE p.id == :id")
    Product getById(Long id);
}
