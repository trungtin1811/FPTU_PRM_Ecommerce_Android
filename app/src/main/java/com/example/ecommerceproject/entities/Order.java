package com.example.ecommerceproject.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.ecommerceproject.enums.OrderType;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity(tableName = "orders")
public class Order {
    @NonNull
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    public Long id;
    @ColumnInfo(name = "account_id")
    public String accountId;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "phone")
    public String phone;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "date_time")
    public String dateTime;
    @ColumnInfo(name = "type")
    public OrderType type;
}
