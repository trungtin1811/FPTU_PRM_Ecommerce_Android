package com.example.ecommerceproject.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import com.example.ecommerceproject.entities.OrderItem;

import java.util.List;

@Dao
public interface OrderItemDao {
    @Query("SELECT oi.id, oi.product_id, oi.quantity, oi.order_id FROM " +
            "order_items oi " +
            "JOIN orders o " +
            "ON oi.order_id == o.id " +
            "AND oi.product_id == :productId " +
            "AND oi.order_id == :cartId " +
            "LIMIT 1")
    OrderItem getItemCart(Long cartId, Long productId);

    @Query("SELECT oi.id, oi.product_id, oi.quantity, oi.order_id FROM " +
            "order_items oi " +
            "JOIN orders o " +
            "ON oi.order_id == o.id " +
            "WHERE o.account_id == :accountId " +
            "AND o.type == 'CART'")
    List<OrderItem> getAllCartItems(String accountId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(OrderItem... orderItems);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OrderItem orderItem);

    @Upsert
    void upsert(OrderItem orderItem);

    @Update
    void update(OrderItem orderItem);

    @Delete(entity = OrderItem.class)
    void delete(OrderItem orderItem);
}
