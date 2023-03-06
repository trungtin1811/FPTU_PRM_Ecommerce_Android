package com.example.ecommerceproject.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OrderItemProduct {
    @Embedded
    public Product product;

    @Relation(
            parentColumn = "id",
            entityColumn = "productId"
    )
    public OrderItem orderItem;
}
