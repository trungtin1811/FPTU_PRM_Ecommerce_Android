package com.example.ecommerceproject.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity(tableName = "order_items")
public class OrderItem {
    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;
    @ColumnInfo(name = "product_id")
    public Long productId;
    @ColumnInfo(name = "order_id")
    public Long orderId;
    public Integer quantity;
}
