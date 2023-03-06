package com.example.ecommerceproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ecommerceproject.entities.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM orders o WHERE o.type == 'CART' AND o.account_id == :accountId")
    Order getCart(String accountId);

    @Query("DELETE FROM orders WHERE orders.type == 'CART'")
    void clearCart();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Order order);

    @Query("SELECT SUM(oi.quantity * p.price) FROM orders o JOIN order_items oi JOIN products p ON o.id == oi.order_id AND p.id == oi.product_id WHERE o.type == 'CART' AND o.account_id == :accountId")
    Double sumPrice(String accountId);

    @Query("SELECT * FROM orders o WHERE o.type == 'ORDER' AND o.account_id == :accountId ORDER BY o.id DESC")
    List<Order> getAllOrder(String accountId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Order order);

    @Query("SELECT SUM(oi.quantity) FROM orders o JOIN order_items oi ON o.id == oi.order_id WHERE o.id == :orderId")
    Long countItems(Long orderId);

    @Query("SELECT SUM(oi.quantity * p.price) FROM orders o JOIN order_items oi JOIN products p ON o.id == oi.order_id AND p.id == oi.product_id WHERE o.id == :orderId")
    Double sumPriceOrder(Long orderId);
}
