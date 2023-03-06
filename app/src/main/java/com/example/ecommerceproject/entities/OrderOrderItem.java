package com.example.ecommerceproject.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OrderOrderItem {
    @Embedded
    public Order order;
    @Relation(
            parentColumn = "id",
            entityColumn = "orderId"
    )
    public List<OrderItem> orderItems;
}
