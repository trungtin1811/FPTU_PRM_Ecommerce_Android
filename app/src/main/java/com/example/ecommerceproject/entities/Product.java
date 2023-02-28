package com.example.ecommerceproject.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity(tableName = "products")
public class Product {
    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "email")
    public String description;
    @ColumnInfo(name = "price")
    public Double price;
    @ColumnInfo(name = "quantity")
    public Long quantity;
    @ColumnInfo(name = "image_url")
    public String imageUrl;
}
